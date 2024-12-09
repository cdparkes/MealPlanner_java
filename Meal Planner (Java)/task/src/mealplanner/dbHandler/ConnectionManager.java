package mealplanner.dbHandler;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/meals_db";
    private static final String DRIVER_NAME = "org.postgresql.Driver";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1111";
    private static Connection con;
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    public static Connection getConnection() {
        if (con == null || isClosed(con)) {
            try {
                Class.forName(DRIVER_NAME);
                con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.out.println("Driver not found.");
            } catch (SQLException e) {
                System.out.println("Failed to create the database connection.");
            }
        }
        return con;
    }

    private static boolean isClosed(Connection con) {
        try {
            return con.isClosed();
        } catch (SQLException e) {
            logger.error("Failed to determine if connection is closed", e);
            return true;
        }
    }

    public boolean isConnectionEstablished() {
        return con != null && !isClosed(con);
    }

    public static void closeConnection() {
        if (con != null && !isClosed(con)) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("Could not close connection to database", e);
            }
        }
    }
}



