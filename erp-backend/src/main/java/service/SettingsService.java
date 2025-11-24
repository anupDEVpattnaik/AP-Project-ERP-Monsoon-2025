package service;

import java.sql.SQLException;

import dao.SettingsDAO;

public class SettingsService {

    private SettingsDAO settingsDAO;

    public SettingsService() throws SQLException {
        this.settingsDAO = new SettingsDAO();
    }

    // -------------------------
    //  MAINTENANCE MODE ACTIONS
    // -------------------------

    /** Turn maintenance mode ON (admin only, checked in AdminService) */
    public void enableMaintenance() throws SQLException {
        settingsDAO.enableMaintenance();
    }

    /** Turn maintenance mode OFF */
    public void disableMaintenance() throws SQLException {
        settingsDAO.disableMaintenance();
    }

    /** Get current state of maintenance mode */
    public boolean isMaintenanceOn() throws SQLException {
        return settingsDAO.isMaintenanceOn();
    }

    /** Get maintenance mode string: "on" or "off" */
    public String getMaintenanceStatus() throws SQLException {
        return settingsDAO.getSetting("maintenance_mode");
    }

    /** Helper that UI or AccessService can use */
    public String getModeMessage() throws SQLException {
        return isMaintenanceOn() ?
                "System is currently in maintenance mode." :
                "System is fully operational.";
    }
}
