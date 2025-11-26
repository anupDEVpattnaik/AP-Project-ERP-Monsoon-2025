package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatabaseConnection;

public class SettingsDAO {

    private Connection conn;

    public SettingsDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public String getSetting(String key) throws SQLException {
        String sql = "SELECT setting_value FROM settings WHERE setting_key = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, key);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("setting_value");
        }
        return null; // not found
    }

    public void updateSetting(String key, String value) throws SQLException {
        String sql = "UPDATE settings SET setting_value = ? WHERE setting_key = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, value);
        ps.setString(2, key);

        ps.executeUpdate();
    }

    public void enableMaintenance() throws SQLException {
        updateSetting("maintenance_mode", "on");
    }

    public void disableMaintenance() throws SQLException {
        updateSetting("maintenance_mode", "off");
    }

    public boolean isMaintenanceOn() throws SQLException {
        String status = getSetting("maintenance_mode");
        return status != null && status.equalsIgnoreCase("on");
    }
}
