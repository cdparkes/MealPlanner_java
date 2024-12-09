package mealplanner;

import mealplanner.dbHandler.ConnectionManager;
import mealplanner.dbHandler.DataManager;
import mealplanner.dbHandler.TableManager;

import javax.xml.crypto.Data;

public class Main {

    public ConnectionManager dbConnection = new ConnectionManager();
    public TableManager tbManager = new TableManager();
    public DataManager dataManager = new DataManager();

    public static void main(String[] args) {
        Main app = new Main();

        ConnectionManager.getConnection();

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
        } else {
            System.out.println("Failed to establish connection.");
        }

        while (Menu.inputMenu()) {
        }

        ConnectionManager.closeConnection();
    }
}