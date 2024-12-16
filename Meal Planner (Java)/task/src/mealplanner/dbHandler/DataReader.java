package mealplanner.dbHandler;

import mealplanner.Meal;
import mealplanner.interfaces.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DataReader implements DataManager {

    private static final Logger logger = LoggerFactory.getLogger(DataReader.class);
    private final Connection connection;

    public DataReader(Connection connection) {
        this.connection = connection;
    }

    public int getMealId(String mealName) {
        String query = "SELECT meal_id FROM meals WHERE meal = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, mealName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("meal_id");
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving meal_id  for meal '{}': {}", mealName, e.getMessage());
        }

        throw new IllegalArgumentException("Meal not found in the database.");
    }

    public List<String> getMeals(String category) {
        String mealQuery = "SELECT meal FROM meals WHERE category = '" + category.toLowerCase() + "' ORDER BY meal;";

        List<String> meals = new ArrayList<>();

        try (PreparedStatement mealStatement = connection.prepareStatement(mealQuery);
             ResultSet mealResultSet = mealStatement.executeQuery()) {

            while (mealResultSet.next()) {
                String meal = mealResultSet.getString("meal");

                meals.add(meal);
            }
        } catch (SQLException e) {
            logger.error("SQL Exception while fetching meals and ingredients: {}", e.getMessage(), e);
        }
        return meals;
    }

    public void listPlan() {
        String query = """
                SELECT day_of_week, meal_category, meals.meal
                FROM plan
                JOIN meals ON plan.meal_id = meals.meal_id
                ORDER BY
                  CASE day_of_week
                      WHEN 'MONDAY' THEN 1
                      WHEN 'TUESDAY' THEN 2
                      WHEN 'WEDNESDAY' THEN 3
                      WHEN 'THURSDAY' THEN 4
                      WHEN 'FRIDAY' THEN 5
                      WHEN 'SATURDAY' THEN 6
                      WHEN 'SUNDAY' THEN 7
                      ELSE 8 -- Handles unexpected or null day_of_week values
                  END,
                  CASE meal_category
                      WHEN 'BREAKFAST' THEN 1
                      WHEN 'LUNCH' THEN 2
                      WHEN 'DINNER' THEN 3
                      ELSE 4 -- Handles unexpected or null meal_category values
                  END;
                """;

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            String currentDay = "";
            while (resultSet.next()) {
                String dayOfWeek = resultSet.getString("day_of_week");
                String mealCategory = resultSet.getString("meal_category");
                String mealName = resultSet.getString("meal");

                if (!dayOfWeek.equals(currentDay)) {
                    currentDay = dayOfWeek;
                    System.out.println("\n" + dayOfWeek);
                }

                System.out.printf("%s, %s%n", mealCategory.substring(0, 1) + mealCategory.substring(1).toLowerCase(), mealName);
            }
        } catch (SQLException e) {
            logger.error("Error fetching the plan: {}", e.getMessage());
            System.out.println("Database does not contain any meal plans.");
        }
    }


    @Override
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

    @Override
    public Set<Meal> fetchAllMealsAndIngredients(String input) {
        String mealQuery = "SELECT * FROM meals WHERE category = '" + input + "';";
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

    @Override
    public int insertNewRecord(String tableName,
                               String col2Name, String col2Value,
                               String col3Name, String col3Value) {
        throw new UnsupportedOperationException("This class does not support inserting new records");
    }

    @Override
    public void insertNewRecord(String tableName,
                                String col2Name, String col2Value,
                                String col3Name, int col3Value) {
        throw new UnsupportedOperationException("This class does not support inserting new records");
    }

//    @Override
//    public void insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, String col3Value, String col4Name, int col4Value) {
//        throw new UnsupportedOperationException("This class does not support inserting new records");
//    }

    @Override
    public void insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, String col3Value, String col4Name, String col4Value, String col5Name, int col5Value) {
        throw new UnsupportedOperationException("This class does not support inserting new records");
    }
}
