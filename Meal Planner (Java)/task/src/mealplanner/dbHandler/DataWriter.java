package mealplanner.dbHandler;

import mealplanner.Meal;
import mealplanner.interfaces.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class DataWriter implements DataManager {

    private static final Logger logger = LoggerFactory.getLogger(DataWriter.class);
    private final Connection connection;

    public DataWriter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insertNewRecord(String tableName,
                               String col2Name, String col2Value,
                               String col3Name, String col3Value) {
        IDGenerator idGen = new IDGenerator(connection);
        int newId = idGen.getNextId(tableName, "meal_id");

        String insertQuery = "INSERT INTO %s (%s, %s)VALUES (?, ?)".formatted(tableName, col2Name, col3Name);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, col2Value);
            preparedStatement.setString(2, col3Value);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL Exception while inserting new record into the database: {}", e.getMessage(), e);
        }
        return newId;
    }

    @Override
    public void insertNewRecord(String tableName,
                                String col2Name, String col2Value,
                                String col3Name, int col3Value) {
        String insertQuery = "INSERT INTO %s (%s, %s)VALUES (?, ?)".formatted(tableName, col2Name, col3Name);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, col2Value);
            preparedStatement.setInt(2, col3Value);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL Exception while inserting new record into the database: {}", e.getMessage(), e);
        }
    }

    @Override
    public void insertNewRecord(String tableName,
                                String col2Name, String col2Value,
                                String col3Name, String col3Value,
                                String col4Name, String col4Value,
                                String col5Name, int col5Value) {
        String insertQuery = "INSERT INTO %s (%s, %s, %s, %s)VALUES (?, ?, ?, ?)".formatted(tableName, col2Name, col3Name, col4Name, col5Name);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, col2Value);
            preparedStatement.setString(2, col3Value);
            preparedStatement.setString(3, col4Value);
            preparedStatement.setInt(4, col5Value);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL Exception while inserting new record into the database: {}", e.getMessage(), e);
        }
    }

    @Override
    public Set<Meal> fetchAllMealsAndIngredients() {
        throw new UnsupportedOperationException("This class does not support reading data");
    }

    @Override
    public Set<Meal> fetchAllMealsAndIngredients(String input) {
        throw new UnsupportedOperationException("This class does not support reading data");
    }
}
