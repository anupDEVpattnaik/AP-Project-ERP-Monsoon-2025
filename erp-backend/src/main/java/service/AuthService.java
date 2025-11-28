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

        authDAO.resetFailedLoginAttempts(user.getUserId());
        authDAO.updateLastLogin(user.getUserId());

        return user;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {

        
        AuthUser user = authDAO.getUserByUserId(userId); 

        if (user == null) return false;

    
        boolean oldMatch = BCrypt.checkpw(oldPassword, user.getPasswordHash());
        if (!oldMatch) {
            throw new SQLException("Old password does not match.");
        }

     
        String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        
        boolean reused = passwordHistoryDAO.isPasswordReused(userId, newPassword, 3); 
        
        if (reused) {
            throw new SQLException("New password cannot match last 3 passwords.");
        }


        boolean updated = authDAO.updatePasswordHash(userId, newHash);

        if (!updated) return false;

        
        passwordHistoryDAO.savePasswordHistory(userId, newHash);

        return true;
    }


    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean deleteUser(int userId) throws SQLException {
       
        return authDAO.deleteUser(userId);
    }
}
