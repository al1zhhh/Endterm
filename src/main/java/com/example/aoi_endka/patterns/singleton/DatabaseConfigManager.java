package com.example.aoi_endka.patterns.singleton;

import java.util.ResourceBundle;

public class DatabaseConfigManager {

    private static DatabaseConfigManager instance;

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String driverClassName;

    private DatabaseConfigManager() {
        ResourceBundle rb = ResourceBundle.getBundle("application");

        this.jdbcUrl = rb.getString("spring.datasource.url");
        this.username = rb.getString("spring.datasource.username");
        this.password = rb.getString("spring.datasource.password");
        this.driverClassName = rb.getString("spring.datasource.driver-class-name");
    }

    public static synchronized DatabaseConfigManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConfigManager();
        }
        return instance;
    }

    public String getJdbcUrl() { return jdbcUrl; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDriverClassName() { return driverClassName; }
}