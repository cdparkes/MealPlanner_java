package mealplanner.dbHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataManager {
    private int mealID;

    private final Connection con = ConnectionManager.getConnection();

    public void insertNewMealRecord(String tableName,
                                    String col1Name, String col1Value,
                                    String col2Name, String col2Value,
                                    String idColName, Connection connection) {
        IDGenerator idGen = new IDGenerator(connection);
        int newId = idGen.getNextId(tableName, idColName);

        String insertQuery = "INSERT INTO %s (%s, %s, %s)VALUES (?, ?, ?)".formatted(tableName, col1Name, col2Name, idColName);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, col1Value);
            preparedStatement.setString(2, col2Value);
            preparedStatement.setInt(3, newId);
            preparedStatement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
