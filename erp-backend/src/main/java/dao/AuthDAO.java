package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import db.DatabaseConnection;
import model.AuthUser;

public class AuthDAO {
    private Connection conn;

    public AuthDAO() throws SQLException {
        this.conn = DatabaseConnection.getAuthConnection();
    }

    public AuthUser getUserByUsername(String username) throws SQLException {
        String query = "SELECT user_id, username, role, password_hash, status, last_login, failed_login_attempts FROM users_auth WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            AuthUser user = new AuthUser();
            user.setUserId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setStatus(rs.getString("status"));
            Timestamp ts = rs.getTimestamp("last_login");
            if (ts != null) {
                user.setLastLogin(ts.toLocalDateTime());
            } else {
                user.setLastLogin(null);
            }
            user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));

            return user;
        }
        return null;
    }

    public AuthUser getUserByUserId(int userId) throws SQLException {
        String query = "SELECT user_id, username, role, password_hash, status, last_login "
                    + "FROM users_auth WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            AuthUser user = new AuthUser();
            user.setUserId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setStatus(rs.getString("status"));
            user.setLastLogin(rs.getTimestamp("last_login").toLocalDateTime());
            return user;
        }
        return null;
    }

    public boolean createUser(AuthUser user) throws SQLException {
        String query = "INSERT INTO users_auth(username, role, password_hash, status) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getRole());
        ps.setString(3, user.getPasswordHash());
        ps.setString(4, user.getStatus());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean updateLastLogin(int userId) throws SQLException {
        String query = "UPDATE users_auth SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean updatePasswordHash(int userId, String newPasswordHash) throws SQLException {
        String query = "UPDATE users_auth SET password_hash = ? WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, newPasswordHash);
        ps.setInt(2, userId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean incrementFailedLoginAttempts(int userId) throws SQLException {
        String query = "UPDATE users_auth SET failed_login_attempts = failed_login_attempts + 1 WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean resetFailedLoginAttempts(int userId) throws SQLException {
        String query = "UPDATE users_auth SET failed_login_attempts = 0 WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean lockUser(int userId) throws SQLException {
        String query = "UPDATE users_auth SET status = 'locked' WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean unlockUser(int userId) throws SQLException {
        String query = "UPDATE users_auth SET status = 'active', failed_login_attempts = 0 WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean deleteUser(int userId) throws SQLException {
        // Delete dependent records first (Password History)
        String deleteHistorySql = "DELETE FROM password_history WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(deleteHistorySql);
        ps.setInt(1, userId);
        ps.executeUpdate();
        // Do not return here, continue to delete the main user record

        
        // Delete the main user record
        String deleteUserSql = "DELETE FROM users_auth WHERE user_id = ?";
        ps = conn.prepareStatement(deleteUserSql);
        ps.setInt(1, userId);
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }
}
