package mealplanner.dbHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IDGenerator.class);
    private final Connection connection;

    public IDGenerator(Connection connection) {
        this.connection = connection;
    }

    public int getNextId(String tableName, String idColumnName) {
        int nextId = 1; // Starting ID when no entries in the table
        String query = String.format("SELECT MAX(%s) FROM %s", idColumnName, tableName);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int currentMaxId = rs.getInt(1); // Get the current max ID
                if (!rs.wasNull()) { // Check if result is not null
                    nextId = currentMaxId + 1;
                }
            }
        } catch (SQLException e) {
            logger.error("SQL Exception getting the next ID: {}", e.getMessage(), e);
        }

        return nextId;
    }
}