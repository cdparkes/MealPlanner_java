package mealplanner.fileHandler;

import mealplanner.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Map;

public class FileWriter {

    public static final Logger logger = LoggerFactory.getLogger(FileWriter.class);

    public void saveToFile(String fileName, Map<String, Integer> shoppingList) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (Map.Entry<String, Integer> entry : shoppingList.entrySet()) {
                String ingredient = entry.getKey();
                int quantity = entry.getValue();
                if (quantity > 1) {
                    writer.printf("%s x%d%n", ingredient, quantity);
                } else {
                    writer.println(ingredient);
                }
            }
        } catch (Exception e) {
            logger.error("Error writing the shopping list to file: {}", e.getMessage());
            System.out.println("Failed to save the file. Please try again.");
        }
    }
}
