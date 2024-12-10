package mealplanner.interfaces;

import mealplanner.Meal;

import java.util.Set;

public interface DataManager {
    Set<Meal> fetchAllMealsAndIngredients();
    int insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, String col3Value);
    void insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, int col3Value);
}
