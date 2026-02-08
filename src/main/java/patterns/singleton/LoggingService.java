package patterns.singleton;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton Pattern Implementation
 * Purpose: Centralized logging service with single instance across application
 * Ensures all logs go through one consistent service
 */
public class LoggingService {
    
    // Log levels
    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR, FATAL
    }
    
    private LogLevel currentLogLevel;
    private boolean writeToFile;
    private String logFilePath;
    private DateTimeFormatter formatter;
    
    // Private constructor
    private LoggingService() {
        this.currentLogLevel = LogLevel.INFO;
        this.writeToFile = false;
        this.logFilePath = "application.log";
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
    
    // Bill Pugh Singleton implementation
    private static class SingletonHelper {
        private static final LoggingService INSTANCE = new LoggingService();
    }
    
    /**
     * Returns the single instance of LoggingService
     */
    public static LoggingService getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    /**
     * Set the minimum log level to display
     */
    public void setLogLevel(LogLevel level) {
        this.currentLogLevel = level;
        info("Log level set to: " + level);
    }
    
    /**
     * Enable or disable writing logs to file
     */
    public void setWriteToFile(boolean writeToFile) {
        this.writeToFile = writeToFile;
        if (writeToFile) {
            info("File logging enabled: " + logFilePath);
        }
    }
    
    /**
     * Set custom log file path
     */
    public void setLogFilePath(String path) {
        this.logFilePath = path;
        info("Log file path set to: " + path);
    }
    
    /**
     * Log DEBUG message
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    
    /**
     * Log INFO message
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }
    
    /**
     * Log WARN message
     */
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }
    
    /**
     * Log ERROR message
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }
    
    /**
     * Log ERROR message with exception
     */
    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message + " | Exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }
    
    /**
     * Log FATAL message
     */
    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }
    
    /**
     * Core logging method
     */
    private void log(LogLevel level, String message) {
        // Check if this level should be logged
        if (level.ordinal() < currentLogLevel.ordinal()) {
            return;
        }
        
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Always print to console
        printToConsole(level, logMessage);
        
        // Optionally write to file
        if (writeToFile) {
            writeToFile(logMessage);
        }
    }
    
    /**
     * Print log message to console with color coding
     */
    private void printToConsole(LogLevel level, String message) {
        switch (level) {
            case DEBUG:
                System.out.println("🔍 " + message);
                break;
            case INFO:
                System.out.println("ℹ️  " + message);
                break;
            case WARN:
                System.out.println("⚠️  " + message);
                break;
            case ERROR:
                System.err.println("❌ " + message);
                break;
            case FATAL:
                System.err.println("💀 " + message);
                break;
        }
    }
    
    /**
     * Write log message to file
     */
    private void writeToFile(String message) {
        try (FileWriter fw = new FileWriter(logFilePath, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(message);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    /**
     * Log method entry for debugging
     */
    public void logMethodEntry(String className, String methodName) {
        debug("Entering " + className + "." + methodName + "()");
    }
    
    /**
     * Log method exit for debugging
     */
    public void logMethodExit(String className, String methodName) {
        debug("Exiting " + className + "." + methodName + "()");
    }
    
    /**
     * Log API request
     */
    public void logApiRequest(String method, String endpoint, String clientIp) {
        info(String.format("API Request: %s %s from %s", method, endpoint, clientIp));
    }
    
    /**
     * Log API response
     */
    public void logApiResponse(String endpoint, int statusCode, long durationMs) {
        info(String.format("API Response: %s - Status: %d - Duration: %dms", 
            endpoint, statusCode, durationMs));
    }
    
    /**
     * Log database operation
     */
    public void logDatabaseOperation(String operation, String table, boolean success) {
        if (success) {
            info(String.format("Database: %s on %s - SUCCESS", operation, table));
        } else {
            error(String.format("Database: %s on %s - FAILED", operation, table));
        }
    }
    
    /**
     * Get current log level
     */
    public LogLevel getLogLevel() {
        return currentLogLevel;
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton instance");
    }
}
