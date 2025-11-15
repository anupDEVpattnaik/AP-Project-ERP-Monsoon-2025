package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String USER = "root";
    private static final String PASS = "sqlisshit";

    // Connection to ERP database
    public static Connection getErpConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/erp_db";
        return DriverManager.getConnection(url, USER, PASS);
    }

    // Connection to AUTH database
    public static Connection getAuthConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/auth_db";
        return DriverManager.getConnection(url, USER, PASS);
    }
}