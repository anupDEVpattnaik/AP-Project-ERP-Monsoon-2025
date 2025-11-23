package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;

public class PasswordHistoryDAO {
    private Connection conn;

    public PasswordHistoryDAO() throws SQLException {
        this.conn = DatabaseConnection.getAuthConnection();
    }

    public boolean savePasswordHistory(int userId, String passwordHash) throws SQLException {
        String query = "INSERT INTO password_history(user_id, password_hash) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setString(2, passwordHash);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public List<String> getPasswordHistory(int userId, int limit) throws SQLException {
        String query = "SELECT password_hash FROM password_history WHERE user_id = ? ORDER BY changed_at DESC LIMIT ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        List<String> history = new ArrayList<>();
        while (rs.next()) {
            history.add(rs.getString("password_hash"));
        }
        return history;
    }

    public boolean isPasswordReused(int userId, String newPasswordHash, int limit) throws SQLException {
        List<String> recentPasswords = getPasswordHistory(userId, limit);
        return recentPasswords.contains(newPasswordHash);
    }
}
