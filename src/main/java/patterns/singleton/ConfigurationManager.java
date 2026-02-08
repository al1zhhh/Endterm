package patterns.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton Pattern Implementation
 * Purpose: Ensures only one instance of configuration manager exists throughout the application
 * Thread-safe implementation using Bill Pugh Singleton Design
 */
public class ConfigurationManager {
    
    // Properties to store configuration
    private Properties properties;
    
    // Private constructor prevents instantiation from outside
    private ConfigurationManager() {
        properties = new Properties();
        loadDefaultConfiguration();
    }
    
    // Bill Pugh Singleton - thread-safe without synchronization overhead
    private static class SingletonHelper {
        private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }
    
    /**
     * Returns the single instance of ConfigurationManager
     * @return ConfigurationManager instance
     */
    public static ConfigurationManager getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    /**
     * Load default configuration values
     */
    private void loadDefaultConfiguration() {
        // Default application settings
        properties.setProperty("app.name", "Game Character Management API");
        properties.setProperty("app.version", "2.0");
        properties.setProperty("app.environment", "development");
        
        // Character settings
        properties.setProperty("character.max.level", "100");
        properties.setProperty("character.min.level", "1");
        properties.setProperty("character.name.min.length", "3");
        properties.setProperty("character.name.max.length", "50");
        
        // Guild settings
        properties.setProperty("guild.max.members", "50");
        properties.setProperty("guild.min.members", "1");
        
        // API settings
        properties.setProperty("api.rate.limit", "100");
        properties.setProperty("api.timeout", "30000");
        
        // Pagination
        properties.setProperty("pagination.default.size", "10");
        properties.setProperty("pagination.max.size", "100");
    }
    
    /**
     * Load configuration from properties file
     * @param filename properties file name
     */
    public void loadFromFile(String filename) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (input != null) {
                properties.load(input);
                System.out.println("Configuration loaded from: " + filename);
            } else {
                System.out.println("Configuration file not found: " + filename + ", using defaults");
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    /**
     * Get configuration property as String
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get configuration property with default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get configuration property as Integer
     */
    public int getIntProperty(String key) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : 0;
    }
    
    /**
     * Get configuration property as Integer with default
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
    
    /**
     * Get configuration property as Boolean
     */
    public boolean getBooleanProperty(String key) {
        String value = properties.getProperty(key);
        return value != null && Boolean.parseBoolean(value);
    }
    
    /**
     * Set configuration property
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Get all properties
     */
    public Properties getAllProperties() {
        return new Properties(properties);
    }
    
    /**
     * Display all configuration settings
     */
    public void displayConfiguration() {
        System.out.println("\n========== Application Configuration ==========");
        properties.forEach((key, value) -> 
            System.out.println(key + " = " + value)
        );
        System.out.println("===============================================\n");
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton instance");
    }
}
