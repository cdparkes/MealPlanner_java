package mealplanner.enums;

public enum Categories {
    BREAKFAST(1),
    LUNCH(2),
    DINNER(3);

    private final int id;

    Categories(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Categories fromId(int id) {
        for (Categories category : values()) {
            if (category.id == id) {
                return category;
            }
        }
        throw new IllegalArgumentException("No Category found for ID: " + id);
    }
}
