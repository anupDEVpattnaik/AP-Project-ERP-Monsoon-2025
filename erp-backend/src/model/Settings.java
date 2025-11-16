package model;

public class Settings {
    private String setting_key;
    private String setting_value;
    private String maintenance_mode;

    public String getSetting_key() {return setting_key;}
    public void setSetting_key(String setting_key) {this.setting_key = setting_key;}

    public String getSetting_value() {return setting_value;}
    public void setSetting_value(String setting_value) {this.setting_value = setting_value;}

    public String getMaintenance_mode() {return maintenance_mode;}
    public void setMaintenance_mode(String maintenance_mode) {this.maintenance_mode = maintenance_mode;}
}