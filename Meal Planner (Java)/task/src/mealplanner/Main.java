package mealplanner;

import mealplanner.dbHandler.ConnectionManager;
import mealplanner.dbHandler.TableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;

public class Main {

  public final ConnectionManager dbConnection = new ConnectionManager();
  public final TableManager tbManager = new TableManager();
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException {
    Main app = new Main();

    Connection connection = ConnectionManager.getConnection();

    app.tbManager.createTable();
    if (app.dbConnection.isConnectionEstablished()) {
      Menu menu = new Menu(connection);
      while (true) {
        if (!menu.inputMenu()) break;
      }
    } else {
      logger.error("Failed to establish connection.");
    }

    ConnectionManager.closeConnection();
  }
}