package mealplanner;

import mealplanner.dbHandler.ConnectionManager;
import mealplanner.dbHandler.TableManager;

public class Main {

    public ConnectionManager dbConnection = new ConnectionManager();
    public TableManager tbManager = new TableManager();

    public static void main(String[] args) {
        Main app = new Main();

        ConnectionManager.getConnection();

        app.tbManager.createTable("meals",
                "category", "VARCHAR(50)",
                "meal", "VARCHAR(50)",
                "meal_id", "INTEGER");

        app.tbManager.createTable("ingredients",
                "ingredient", "VARCHAR(50)",
                "ingredient_id", "INTEGER",
                "meal_id", "INTEGER");

        if (app.dbConnection.isConnectionEstablished()) {
            System.out.println("Connection successfully established.");
        } else {
            System.out.println("Failed to establish connection.");
        }

        while (Menu.inputMenu()) {
        }

        ConnectionManager.closeConnection();
    }
}