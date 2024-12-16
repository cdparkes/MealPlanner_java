package mealplanner;

import mealplanner.dbHandler.DataReader;
import mealplanner.dbHandler.DataWriter;

import java.sql.Connection;
import java.util.*;

public class Menu {
    private static final String REGEX_ALPHABETS = "^[A-Za-z ]+$";
    private static final String END_MESSAGE = "Bye!";
    private static final String FAIL_MESSAGE = "Failed to connect to the database.";
    private static final String INGREDIENTS_INPUT_PROMPT = "Input the ingredients:";
    private static final String CATEGORY_PROMPT = "Which meal do you want to add (breakfast, lunch, dinner)?";
    private static final String NAME_PROMPT = "Input the meal's name:";

    private final Scanner scanner = new Scanner(System.in);
    private final Connection connection;
    private final DataReader dataReader;
    private final DataWriter dataWriter;

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
            case "show plan" -> handleShowPlan();
            case "exit" -> exitMenu();
            default -> true;
        };
    }

    private boolean handleShowPlan() {
        showPlan();
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
    }

    private void showPlan() {
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