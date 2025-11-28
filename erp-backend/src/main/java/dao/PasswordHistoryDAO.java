package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

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

    public boolean isPasswordReused(int userId, String plainPassword, int limit) throws SQLException {
        
        // SQL query to fetch the most recent 'limit' password hashes
        String sql = "SELECT password_hash FROM password_history WHERE user_id = ? "
                   + "ORDER BY created_at DESC LIMIT ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, userId);
        ps.setInt(2, limit);

        try (ResultSet rs = ps.executeQuery()) {
            
            // 1. Loop through all fetched history hashes
            while (rs.next()) {
                String historyHash = rs.getString("password_hash");

                // 2. CRITICAL STEP: Use BCrypt.checkpw() to compare the plaintext 
                //    password against the stored, salted hash.
                if (BCrypt.checkpw(plainPassword, historyHash)) {
                    return true; // Match found: Password reused
                }
            }
        }

        return false; // No match found: Password is new
    }
}
