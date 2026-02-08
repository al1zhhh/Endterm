package utils;

import patterns.singleton.DatabaseConfigManager;
import patterns.singleton.LoggingService;
import exceptions.DatabaseOperationException;  // ДОБАВЬТЕ этот импорт
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final DatabaseConfigManager dbConfig = DatabaseConfigManager.getInstance();
    private static final LoggingService logger = LoggingService.getInstance();

    // ДОБАВЬТЕ throws к сигнатуре
    public static Connection getConnection() throws SQLException, DatabaseOperationException {
        try {
            Class.forName(dbConfig.getDriverClassName());

            Connection conn = DriverManager.getConnection(
                    dbConfig.getJdbcUrl(),
                    dbConfig.getUsername(),
                    dbConfig.getPassword()
            );

            logger.info("Database connection established");
            return conn;

        } catch (SQLException e) {
            logger.error("Failed to connect to database", e);
            throw new DatabaseOperationException("Cannot connect to database", e);
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found", e);
            throw new DatabaseOperationException("Database driver not found", e);
        }
    }
}