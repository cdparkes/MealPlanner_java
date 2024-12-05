package mealplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Meal {
    private String category;
    private String name;
    private List<String> ingredients;

    public Meal(String category, String name, List<String> ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = new ArrayList<>(ingredients);
    }

    public String getMealCategory() {
        return category;
    }

    public void setMealCategory(String category) {
        this.category = category;
    }

    public String getMealName() {
        return name;
    }

    public void setMealName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
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
                
                Category: %s
                Name: %s
                Ingredients:%n""", category, name);
        for (String element : ingredients) {
            System.out.println(element);
        }
    }
}
