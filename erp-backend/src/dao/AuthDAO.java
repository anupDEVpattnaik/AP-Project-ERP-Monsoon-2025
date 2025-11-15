package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DatabaseConnection;
import model.AuthUser;

public class AuthDAO {
    private Connection conn;

    public AuthDAO() throws SQLException {
        this.conn = DatabaseConnection.getAuthConnection();
    }

    public AuthUser getUserByUsername(String username) throws SQLException {
        String query = "SELECT user_id, username, role, password_hash, status, last_login FROM users_auth WHERE username = ?";
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
}
