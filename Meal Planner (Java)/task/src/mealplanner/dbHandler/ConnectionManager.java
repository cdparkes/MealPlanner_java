package mealplanner.dbHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static String url = "jdbc:postgresql://localhost:5432/meals_db";
    private static String driverName = "org.postgresql.Driver";
    private static String username = "postgres";
    private static String password = "1111";
    private static Connection con;
    private static String urlstring;

    public static Connection getConnection() {
        if (con == null || isClosed(con)) {
            try {
                Class.forName(driverName);
                con = DriverManager.getConnection(url, username, password);
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
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

}
