package dao.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DatabaseConnection;

public class AuthDAO {
    private Connection conn;

    public AuthDAO() throws SQLException {
        this.conn = DatabaseConnection.getAuthConnection();
    }

    public ResultSet getUserByUsername(String username) throws SQLException {
        String query = "SELECT user_id, username, role, password_hash, status, last_login FROM users_auth WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);
        return ps.executeQuery();
    }

    public boolean createUser(String username, String role, String passwordHash) throws SQLException {
        String query = "INSERT INTO users_auth(username, role, password_hash, status) VALUES (?, ?, ?, 'active')";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, role);
        ps.setString(3, passwordHash);
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
