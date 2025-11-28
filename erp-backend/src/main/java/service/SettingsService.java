package service;

import java.sql.SQLException;

import dao.SettingsDAO;

public class SettingsService {

    private SettingsDAO settingsDAO;

    public SettingsService() throws SQLException {
        this.settingsDAO = new SettingsDAO();
    }



   
    public void enableMaintenance() throws SQLException {
        settingsDAO.enableMaintenance();
    }

 
    public void disableMaintenance() throws SQLException {
        settingsDAO.disableMaintenance();
    }


    public boolean isMaintenanceOn() throws SQLException {
        return settingsDAO.isMaintenanceOn();
    }

    public String getMaintenanceStatus() throws SQLException {
        return settingsDAO.getSetting("maintenance_mode");
    }

 
    public String getModeMessage() throws SQLException {
        return isMaintenanceOn() ?
                "System is currently in maintenance mode." :
                "System is fully operational.";
    }
}
