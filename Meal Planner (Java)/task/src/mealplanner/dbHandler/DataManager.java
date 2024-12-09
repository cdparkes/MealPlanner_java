package mealplanner.dbHandler;

import mealplanner.Meal;

import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class DataManager {

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

                Meal meal = new Meal(category, mealName, mealId);

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
            e.printStackTrace();
        }

        return meals;
    }

    public int insertNewRecord(String tableName,
                               String col1Name, String col1Value,
                               String col2Name, String col2Value,
                               String col3Name, Connection connection) {
        IDGenerator idGen = new IDGenerator(connection);
        int newId = idGen.getNextId(tableName, col3Name);

        String insertQuery = "INSERT INTO %s (%s, %s, %s)VALUES (?, ?, ?)".formatted(tableName, col1Name, col2Name, col3Name);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, col1Value);
            preparedStatement.setString(2, col2Value);
            preparedStatement.setInt(3, newId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public void insertNewRecord(String tableName,
                                String col1Name, String col1Value,
                                String col2Name,
                                String col3Name, int col3Value,
                                Connection connection) {
        IDGenerator idGen = new IDGenerator(connection);
        int newId = idGen.getNextId(tableName, col2Name);

        String insertQuery = "INSERT INTO %s (%s, %s, %s)VALUES (?, ?, ?)".formatted(tableName, col1Name, col2Name, col3Name);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, col1Value);
            preparedStatement.setInt(2, newId);
            preparedStatement.setInt(3, col3Value);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
