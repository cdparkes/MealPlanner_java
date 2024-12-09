package mealplanner;

import mealplanner.dbHandler.ConnectionManager;

public class Main {

  public ConnectionManager dbConnection = new ConnectionManager();

  public static void main(String[] args) {
    ConnectionManager.getConnection();

    ConnectionManager.createTable("meals",
            "category", "VARCHAR(50)",
            "meal", "VARCHAR(50)",
            "meal_id", "INTEGER");

    ConnectionManager.createTable("ingredients",
            "ingredient", "VARCHAR(50)",
            "ingredient_id", "INTEGER",
            "meal_id", "INTEGER");

    Main app = new Main();
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