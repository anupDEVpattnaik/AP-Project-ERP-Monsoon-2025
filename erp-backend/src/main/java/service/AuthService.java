package service;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import dao.AuthDAO;
import dao.PasswordHistoryDAO;
import model.AuthUser;

public class AuthService {

    private AuthDAO authDAO;
    private PasswordHistoryDAO passwordHistoryDAO;

    public AuthService() throws SQLException {
        this.authDAO = new AuthDAO();
        this.passwordHistoryDAO = new PasswordHistoryDAO();
    }

    /**
     * Attempts to log in the user by verifying username & password.
     * Returns AuthUser if success, null if invalid credentials.
     * Tracks failed login attempts and locks account after 5 failures.
     */
    public AuthUser login(String username, String plainPassword) throws SQLException {

        AuthUser user = authDAO.getUserByUsername(username);

        if (user == null) {
            return null; // user not found
        }

        // Check if account is locked
        if ("locked".equals(user.getStatus())) {
            return null; // account locked
        }

        // Verify password using BCrypt
        boolean match = BCrypt.checkpw(plainPassword, user.getPasswordHash());

        if (!match) {
            // Increment failed login attempts
            authDAO.incrementFailedLoginAttempts(user.getUserId());

            // Check if we need to lock the account
            AuthUser updatedUser = authDAO.getUserByUsername(username);
            if (updatedUser != null && updatedUser.getFailedLoginAttempts() >= 5) {
                authDAO.lockUser(user.getUserId());
            }

            return null; // wrong password
        }

        // Successful login - reset failed attempts and update last login
        authDAO.resetFailedLoginAttempts(user.getUserId());
        authDAO.updateLastLogin(user.getUserId());

        return user;
    }

    /**
     * Changes a user's password after verifying the old one and checking history.
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {

        // Assuming AuthDAO has getUserByUserId, as noted in previous review
        AuthUser user = authDAO.getUserByUserId(userId); 

        if (user == null) return false;

        // Step 1: verify old password
        boolean oldMatch = BCrypt.checkpw(oldPassword, user.getPasswordHash());
        if (!oldMatch) {
            throw new SQLException("Old password does not match.");
        }

        // Step 2: hash new password
        String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Step 3: check for password reuse (limit = last 3)
        // CRITICAL FIX: We must pass the PLAINTEXT password for the DAO to compare it 
        // against the history hashes using BCrypt.checkpw().
        boolean reused = passwordHistoryDAO.isPasswordReused(userId, newPassword, 3); 
        
        if (reused) {
            throw new SQLException("New password cannot match last 3 passwords.");
        }

        // Step 4: update the active password
        boolean updated = authDAO.updatePasswordHash(userId, newHash);

        if (!updated) return false;

        // Step 5: save latest password into history
        passwordHistoryDAO.savePasswordHistory(userId, newHash);

        return true;
    }

    /**
     * Utility for hashing passwords for account creation.
     */
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean deleteUser(int userId) throws SQLException {
        // NOTE: No role check is done here, assuming AdminService handles permission.
        return authDAO.deleteUser(userId);
    }
}
