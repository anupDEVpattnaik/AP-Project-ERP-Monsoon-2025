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
     */
    public AuthUser login(String username, String plainPassword) throws SQLException {

        AuthUser user = authDAO.getUserByUsername(username);

        if (user == null) {
            return null; // user not found
        }

        // Verify password using BCrypt
        boolean match = BCrypt.checkpw(plainPassword, user.getPasswordHash());

        if (!match) {
            return null; // wrong password
        }

        // Update last login timestamp
        authDAO.updateLastLogin(user.getUserId());

        return user;
    }

    /**
     * Changes a user's password after verifying the old one and checking history.
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {

        AuthUser user = authDAO.getUserByUserId(userId); // we need to add this in AuthDAO if missing

        if (user == null) return false;

        // Step 1: verify old password
        boolean oldMatch = BCrypt.checkpw(oldPassword, user.getPasswordHash());
        if (!oldMatch) {
            throw new SQLException("Old password does not match.");
        }

        // Step 2: hash new password
        String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Step 3: check for password reuse (limit = last 3)
        boolean reused = passwordHistoryDAO.isPasswordReused(userId, newHash, 3);
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

}
