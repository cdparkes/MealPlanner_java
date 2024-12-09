package mealplanner;

import mealplanner.dbHandler.ConnectionManager;
import mealplanner.dbHandler.DataManager;

import java.sql.Connection;
import java.util.*;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static Set<Meal> mealList = new LinkedHashSet<>();

    public static boolean inputMenu() {
        System.out.println("What would you like to do (add, show, exit)?");
        String input = scanner.nextLine();
        switch (input) {
            case "add" -> addMeal();
            case "show" -> showMeals();
            case "exit" -> {
                System.out.println("Bye!");
                return false;
            }
        }
        return true;
    }

    private static void addMeal() {
        String regexString = "^[A-Za-z ]+$";
        String mealCategory = "", mealName = "", input = "";
        boolean invalidInput = true;
        List<String> ingredients = new ArrayList();

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        while (invalidInput) {
            input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "breakfast", "lunch", "dinner" -> {
                    mealCategory = input;
                    invalidInput = false;
                }
                default -> System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        }

        while (true) {
            System.out.println("Input the meal's name:");
            input = scanner.nextLine();
            if (input.matches(regexString)) {
                mealName = input;
                break;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }

        System.out.println("Input the ingredients:");
        while (!invalidInput) {
            ingredients.clear();
            input = scanner.nextLine();
            boolean allValid = true;
            if (input.endsWith(", ")) {
                System.out.println("Wrong format. Use letters only!");
                allValid = false;
            } else {
                String[] inputArray = input.split(", ");
                for (String ingredient : inputArray) {
                    if (ingredient.matches(regexString)) {
                        ingredients.add(ingredient);
                    } else {
                        System.out.println("Wrong format. Use letters only!");
                        allValid = false;
                        break;
                    }
                }
            }

            if (!allValid) {
                continue;
            }

            invalidInput = true;
        }

        DataManager dataManager = new DataManager();
        Connection connection = ConnectionManager.getConnection();

        if (connection != null) {
            dataManager.insertNewMealRecord("meals",
                    "category", mealCategory,
                    "meal", mealName,
                    "meal_id", connection);
        } else {
            System.out.println("Failed to connect to the database.");
        }

    }

    private static void showMeals() {
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
