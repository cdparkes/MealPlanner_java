package mealplanner.dbHandler;

import mealplanner.Meal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class DataManager {

    private static final Logger logger = LoggerFactory.getLogger(DataManager.class);
    private final Connection connection;

    public DataManager(Connection connection) {
        this.connection = connection;
    }

    public Set<Meal> fetchAllMealsAndIngredients() {
        String mealQuery = "SELECT * FROM meals";
        String ingredientQuery = "SELECT * FROM ingredients WHERE meal_id=?";

        Set<Meal> meals = new LinkedHashSet<>();

        try (PreparedStatement mealStatement = connection.prepareStatement(mealQuery);
            ResultSet mealResultSet = mealStatement.executeQuery()) {

            while (mealResultSet.next()) {
                int mealId = mealResultSet.getInt("meal_id");
                String category = mealResultSet.getString("category");
                String mealName = mealResultSet.getString("meal");

                Meal meal = new Meal(category, mealName);

                try (PreparedStatement ingredientStatement = connection.prepareStatement(ingredientQuery)) {
                    ingredientStatement.setInt(1, mealId);

                    try (ResultSet ingredientResultSet = ingredientStatement.executeQuery()) {
                        while (ingredientResultSet.next()) {
                            String ingredient = ingredientResultSet.getString("ingredient");
                            meal.addIngredient(ingredient);
                        }
                    }
                }

                meals.add(meal);
            }
        } catch (SQLException e) {
            logger.error("SQL Exception while fetching meals and ingredients: {}", e.getMessage(), e);
        }

        return meals;
    }

    public int insertNewRecord(String tableName,
                               String col2Name, String col2Value,
                               String col3Name, String col3Value,
                               Connection connection) {
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

    public void insertNewRecord(String tableName,
                                String col2Name, String col2Value,
                                String col3Name, int col3Value,
                                Connection connection) {
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
}
