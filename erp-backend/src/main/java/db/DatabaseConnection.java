package db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnection {

    private static final HikariDataSource erpDataSource;
    private static final HikariDataSource authDataSource;

    private static final String USER = "root";
    private static final String PASS = "sqlisshit"; 

    static {
        // --- ERP Database Configuration ---
        HikariConfig erpConfig = new HikariConfig();
        erpConfig.setJdbcUrl("jdbc:mysql://localhost:3306/erp_db");
        erpConfig.setUsername(USER);
        erpConfig.setPassword(PASS);
        erpConfig.addDataSourceProperty("cachePrepStmts", "true");
        erpConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        erpConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        erpConfig.setMaximumPoolSize(15); 
        erpDataSource = new HikariDataSource(erpConfig);

        // --- AUTH Database Configuration ---
        HikariConfig authConfig = new HikariConfig();
        authConfig.setJdbcUrl("jdbc:mysql://localhost:3306/auth_db");
        authConfig.setUsername(USER);
        authConfig.setPassword(PASS);
        authConfig.addDataSourceProperty("cachePrepStmts", "true");
        authConfig.setMaximumPoolSize(5);
        authDataSource = new HikariDataSource(authConfig);
    }

    // Public method to get a connection for the ERP database
    public static Connection getERPConnection() throws SQLException {
        return erpDataSource.getConnection();
    }

    // Public method to get a connection for the AUTH database
    public static Connection getAuthConnection() throws SQLException {
        return authDataSource.getConnection();
    }
}