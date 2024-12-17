package mealplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Meal {
    private final String category;
    private final String name;
    private final List<String> ingredients;

    public Meal(String category, String name) {
        this.category = category;
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return category.equals(meal.category) &&
                name.equals(meal.name) &&
                ingredients.equals(meal.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, name, ingredients);
    }

    public void printMeal() {
        System.out.printf("""
                
                Name: %s
                Ingredients:%n""", name);
        for (String element : ingredients) {
            System.out.println(element);
        }
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }
}