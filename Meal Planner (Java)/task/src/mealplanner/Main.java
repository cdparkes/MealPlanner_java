package mealplanner;

import mealplanner.dbHandler.ConnectionManager;

public class Main {

  public ConnectionManager dbConnection = new ConnectionManager();

  public static void main(String[] args) {
    ConnectionManager.getConnection();

    Main app = new Main();
    if (app.dbConnection.isConnectionEstablished()) {
      System.out.println("Connection successfully established.");
    } else {
      System.out.println("Failed to establish connection.");
    }

    while (Menu.inputMenu()) {
    }
  }
}