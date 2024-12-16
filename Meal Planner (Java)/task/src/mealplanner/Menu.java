package mealplanner;

import mealplanner.dbHandler.DataReader;
import mealplanner.dbHandler.DataWriter;
import mealplanner.enums.Categories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.*;

public class Menu {
    private static final String REGEX_ALPHABETS = "^[A-Za-z ]+$";
    private static final String END_MESSAGE = "Bye!";
    private static final String FAIL_MESSAGE = "Failed to connect to the database.";
    private static final String INGREDIENTS_INPUT_PROMPT = "Input the ingredients:";
    private static final String CATEGORY_PROMPT = "Which meal do you want to add (breakfast, lunch, dinner)?";
    private static final String NAME_PROMPT = "Input the meal's name:";

    private final Scanner scanner = new Scanner(System.in);
    public static final Logger logger = LoggerFactory.getLogger(Menu.class);
    private final Connection connection;
    private final DataReader dataReader;
    private final DataWriter dataWriter;

    private int dayCounter = 1;

    public Menu(Connection connection) {
        this.connection = connection;
        this.dataReader = new DataReader(connection);
        this.dataWriter = new DataWriter(connection);
    }

    public boolean inputMenu() {
        System.out.println("What would you like to do (add, show, plan, list plan, exit)?");
        String selection = scanner.nextLine();
        return switch (selection) {
            case "add" -> handleAddition();
            case "show" -> handleShow();
            case "plan" -> handlePlan();
            case "list plan" -> handleListPlan();
            case "exit" -> exitMenu();
            default -> true;
        };
    }

    private boolean handleListPlan() {
        listPlan();
        return true;
    }

    private boolean handlePlan() {
        plan();
        return true;
    }

    private boolean handleAddition() {
        addMeal();
        return true;
    }

    private boolean handleShow() {
        showMeals();
        return true;
    }

    private boolean exitMenu() {
        System.out.println(END_MESSAGE);
        return false;
    }

    private void plan() {
        clearPlanTable();
        for (int day = 1; day <= 7; day++) {
            DayOfWeek dayOfWeek = DayOfWeek.of(day);
            System.out.println(dayOfWeek);

            for (Categories category : Categories.values()) {
                List<String> meals = dataReader.getMeals(category.name());
                if (meals.isEmpty()) {
                    System.out.println("No meals found for " + category.name().toLowerCase() + ".");
                    continue;
                }

                meals.sort(String::compareToIgnoreCase);
                meals.forEach(System.out::println);

                String chosenMeal = null;
                while (true) {
                    System.out.printf("Choose the %s for %s from the list above:%n", category.name().toLowerCase(), dayOfWeek);
                    String input = scanner.nextLine();

                    if (meals.contains(input)) {
                        chosenMeal = input;
                        break;
                    } else {
                        System.out.println("This meal doesn't exist. Choose a meal from the list above.");
                    }
                }

                int mealId = dataReader.getMealId(chosenMeal);

                dataWriter.insertNewRecord("plan", "day_of_week", String.valueOf(dayOfWeek), "meal_option", chosenMeal, "meal_category", category.name(), "meal_id", mealId);
            }

            System.out.printf("Yeah! We planned the meals for %s %n", dayOfWeek);
            System.out.println();
        }
        dataReader.listPlan();
    }

    private void clearPlanTable() {
        String clearQuery = "DELETE FROM plan";

        try (PreparedStatement statement = connection.prepareStatement(clearQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Could not clear the plan table: {}", e.getMessage());
        }
    }

    private void listPlan() {
        dataReader.listPlan();
    }

    private void addMeal() {
        String mealCategory = promptForCategory();
        String mealName = promptForMealName();
        List<String> ingredients = promptForIngredients();
        saveMeal(mealCategory, mealName, ingredients);
    }

    private String promptForCategory() {
        System.out.println(CATEGORY_PROMPT);
        while (true) {
            String input = scanner.nextLine().toLowerCase();
            if (List.of("breakfast", "lunch", "dinner").contains(input)) {
                return input;
            }
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
        }
    }

    private String promptForMealName() {
        System.out.println(NAME_PROMPT);
        while (true) {
            String input = scanner.nextLine();
            if (input.matches(REGEX_ALPHABETS)) {
                return input;
            }
            System.out.println("Wrong format. Use letters only!");
        }
    }

    private List<String> promptForIngredients() {
        System.out.println(INGREDIENTS_INPUT_PROMPT);
        while (true) {
            String input = scanner.nextLine();
            String[] inputArray = input.split(", ");
            List<String> ingredients = new ArrayList<>();
            boolean allValid = true;

            for (String ingredient : inputArray) {
                if (ingredient.matches(REGEX_ALPHABETS)) {
                    ingredients.add(ingredient);
                } else {
                    allValid = false;
                    break;
                }
            }

            if (allValid) {
                return ingredients;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }
    }

    private void saveMeal(String mealCategory, String mealName, List<String> ingredients) {
        if (connection != null) {
            int mealId = dataWriter.insertNewRecord("meals",
                    "category", mealCategory,
                    "meal", mealName);
            for (String ingredient : ingredients) {
                dataWriter.insertNewRecord("ingredients",
                        "ingredient", ingredient,
                        "meal_id", mealId);
            }
            System.out.println("The meal has been added!");
        } else {
            System.out.println(FAIL_MESSAGE);
        }
    }

    private void showMeals() {
        Set<Meal> mealList = dataReader.fetchAllMealsAndIngredients();
        if (mealList.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
        } else {
            for (Meal meal : mealList) {
                meal.printMeal();
                System.out.println();
            }
        }
    }
}