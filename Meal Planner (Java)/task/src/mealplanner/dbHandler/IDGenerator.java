package mealplanner.dbHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDGenerator {

    private Connection con;

    public IDGenerator(Connection con) {
        this.con = con;
    }

    public int getNextId(String tableName, String idColumnName) {
        int nextId = 1; // Starting ID when no entries in the table
        String query = String.format("SELECT MAX(%s) FROM %s", idColumnName, tableName);

        try (PreparedStatement statement = con.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int currentMaxId = rs.getInt(1); // Get the current max ID
                if (!rs.wasNull()) { // Check if result is not null
                    nextId = currentMaxId + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextId;
    }
}