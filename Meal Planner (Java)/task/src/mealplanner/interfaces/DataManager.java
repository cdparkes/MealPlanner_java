package mealplanner.interfaces;

import mealplanner.Meal;

import java.util.Set;

public interface DataManager {
    Set<Meal> fetchAllMealsAndIngredients();

    Set<Meal> fetchAllMealsAndIngredients(String input);

    int insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, String col3Value);

    void insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, int col3Value);
//    void insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, String col3Value, String col4Name, int col4Value);
    void insertNewRecord(String tableName, String col2Name, String col2Value, String col3Name, String col3Value, String col4Name, String col4Value, String col5Name, int col5Value);
}
