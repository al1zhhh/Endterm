package patterns.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton Pattern Implementation
 * Purpose: Manages database configuration and connection parameters
 * Ensures consistent database settings across the entire application
 */
public class DatabaseConfigManager {
    
    // Database configuration properties
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    private int maxPoolSize;
    private int minPoolSize;
    private int connectionTimeout;
    private boolean autoCommit;
    private Map<String, String> additionalProperties;
    
    // Private constructor
    private DatabaseConfigManager() {
        loadDefaultConfiguration();
    }
    
    // Bill Pugh Singleton implementation
    private static class SingletonHelper {
        private static final DatabaseConfigManager INSTANCE = new DatabaseConfigManager();
    }
    
    /**
     * Returns the single instance of DatabaseConfigManager
     */
    public static DatabaseConfigManager getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    /**
     * Load default database configuration
     */
    private void loadDefaultConfiguration() {
        // Default PostgreSQL configuration
        this.jdbcUrl = "jdbc:postgresql://localhost:5432/game_characters_db";
        this.username = "postgres";
        this.password = "postgres";
        this.driverClassName = "org.postgresql.Driver";
        this.maxPoolSize = 10;
        this.minPoolSize = 2;
        this.connectionTimeout = 30000; // 30 seconds
        this.autoCommit = true;
        this.additionalProperties = new HashMap<>();
        
        // Additional connection properties
        additionalProperties.put("ssl", "false");
        additionalProperties.put("characterEncoding", "UTF-8");
        additionalProperties.put("useUnicode", "true");
    }
    
    /**
     * Configure for PostgreSQL database
     */
    public void configurePostgreSQL(String host, int port, String database, 
                                    String username, String password) {
        this.jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
        this.username = username;
        this.password = password;
        this.driverClassName = "org.postgresql.Driver";
        
        LoggingService.getInstance().info("Database configured for PostgreSQL: " + host);
    }
    
    /**
     * Configure for MySQL database
     */
    public void configureMySQL(String host, int port, String database, 
                              String username, String password) {
        this.jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", host, port, database);
        this.username = username;
        this.password = password;
        this.driverClassName = "com.mysql.cj.jdbc.Driver";
        
        LoggingService.getInstance().info("Database configured for MySQL: " + host);
    }
    
    /**
     * Configure for SQLite database
     */
    public void configureSQLite(String databasePath) {
        this.jdbcUrl = "jdbc:sqlite:" + databasePath;
        this.username = "";
        this.password = "";
        this.driverClassName = "org.sqlite.JDBC";
        
        LoggingService.getInstance().info("Database configured for SQLite: " + databasePath);
    }
    
    /**
     * Set custom JDBC URL
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
    
    /**
     * Set database credentials
     */
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Set connection pool size
     */
    public void setPoolSize(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min pool size cannot be greater than max pool size");
        }
        this.minPoolSize = min;
        this.maxPoolSize = max;
    }
    
    /**
     * Set connection timeout in milliseconds
     */
    public void setConnectionTimeout(int timeoutMs) {
        if (timeoutMs <= 0) {
            throw new IllegalArgumentException("Connection timeout must be positive");
        }
        this.connectionTimeout = timeoutMs;
    }
    
    /**
     * Set auto-commit mode
     */
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
    
    /**
     * Add additional connection property
     */
    public void addProperty(String key, String value) {
        additionalProperties.put(key, value);
    }
    
    /**
     * Remove additional connection property
     */
    public void removeProperty(String key) {
        additionalProperties.remove(key);
    }
    
    // Getters
    public String getJdbcUrl() {
        return jdbcUrl;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getDriverClassName() {
        return driverClassName;
    }
    
    public int getMaxPoolSize() {
        return maxPoolSize;
    }
    
    public int getMinPoolSize() {
        return minPoolSize;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public boolean isAutoCommit() {
        return autoCommit;
    }
    
    public Map<String, String> getAdditionalProperties() {
        return new HashMap<>(additionalProperties);
    }
    
    /**
     * Get connection URL with all properties
     */
    public String getFullConnectionUrl() {
        StringBuilder url = new StringBuilder(jdbcUrl);
        
        if (!additionalProperties.isEmpty()) {
            url.append("?");
            additionalProperties.forEach((key, value) -> 
                url.append(key).append("=").append(value).append("&")
            );
            // Remove last '&'
            url.setLength(url.length() - 1);
        }
        
        return url.toString();
    }
    
    /**
     * Display current database configuration
     */
    public void displayConfiguration() {
        System.out.println("\n========== Database Configuration ==========");
        System.out.println("JDBC URL: " + jdbcUrl);
        System.out.println("Username: " + username);
        System.out.println("Password: " + maskPassword(password));
        System.out.println("Driver: " + driverClassName);
        System.out.println("Pool Size: " + minPoolSize + " - " + maxPoolSize);
        System.out.println("Connection Timeout: " + connectionTimeout + "ms");
        System.out.println("Auto Commit: " + autoCommit);
        
        if (!additionalProperties.isEmpty()) {
            System.out.println("\nAdditional Properties:");
            additionalProperties.forEach((key, value) -> 
                System.out.println("  " + key + " = " + value)
            );
        }
        System.out.println("==========================================\n");
    }
    
    /**
     * Mask password for security
     */
    private String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "****";
        }
        return "*".repeat(password.length());
    }
    
    /**
     * Validate current configuration
     */
    public boolean validateConfiguration() {
        boolean isValid = true;
        
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            LoggingService.getInstance().error("JDBC URL is not configured");
            isValid = false;
        }
        
        if (driverClassName == null || driverClassName.isEmpty()) {
            LoggingService.getInstance().error("Driver class name is not configured");
            isValid = false;
        }
        
        if (maxPoolSize < minPoolSize) {
            LoggingService.getInstance().error("Max pool size is less than min pool size");
            isValid = false;
        }
        
        if (isValid) {
            LoggingService.getInstance().info("Database configuration is valid");
        }
        
        return isValid;
    }
    
    /**
     * Reset to default configuration
     */
    public void resetToDefaults() {
        loadDefaultConfiguration();
        LoggingService.getInstance().info("Database configuration reset to defaults");
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton instance");
    }
}
