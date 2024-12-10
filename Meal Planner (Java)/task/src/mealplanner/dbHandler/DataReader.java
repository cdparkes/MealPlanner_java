package mealplanner.dbHandler;

import mealplanner.Meal;
import mealplanner.interfaces.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class DataReader implements DataManager {

    private static final Logger logger = LoggerFactory.getLogger(DataReader.class);
    private final Connection connection;

    public DataReader(Connection connection) {
        this.connection = connection;
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
}
