package mealplanner;

import mealplanner.dbHandler.ConnectionManager;
import mealplanner.dbHandler.DataManager;
import mealplanner.dbHandler.TableManager;

import java.sql.Connection;

public class Main {

    public final ConnectionManager dbConnection = new ConnectionManager();
    public final TableManager tbManager = new TableManager();

    public static void main(String[] args) {
        Main app = new Main();

        Connection connection = ConnectionManager.getConnection();

        app.tbManager.createTableIfNotExists("meals",
                "category", "VARCHAR(50)",
                "meal", "VARCHAR(50)",
                "meal_id", "INTEGER");

        app.tbManager.createTableIfNotExists("ingredients",
                "ingredient", "VARCHAR(50)",
                "ingredient_id", "INTEGER",
                "meal_id", "INTEGER");

        if (app.dbConnection.isConnectionEstablished()) {
            System.out.println("Connection successfully established.");
            Menu menu = new Menu(connection);
            while (menu.inputMenu()) {
            }
        } else {
            System.out.println("Failed to establish connection.");
        }

        ConnectionManager.closeConnection();
    }
}