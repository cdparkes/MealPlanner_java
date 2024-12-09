package mealplanner.dbHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableManager {

    private final Connection connection = ConnectionManager.getConnection();

    public void createTableIfNotExists(String tableName,
                                       String col1Name, String col1Type,
                                       String col2Name, String col2Type,
                                       String col3Name, String col3Type) {

        if (connection == null) {
            System.out.println("Failed to create table because the database connection could not be established");
            return;
        }

        String createString = "CREATE TABLE IF NOT EXISTS %s (%s %s,%s %s,%s %s)".formatted(tableName, col1Name, col1Type, col2Name, col2Type, col3Name, col3Type);

        try (PreparedStatement createTable = connection.prepareStatement(createString)) {
            connection.setAutoCommit(false);
            createTable.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    // used for debugging db connection and table creation
    public boolean isTableExists(String tableName) {
        String checkTableExistsQuery = "SELECT EXISTS (" +
                "SELECT * FROM information_schema.tables " +
                "WHERE table_schema = 'public' " +
                "AND table_name = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkTableExistsQuery)) {
            preparedStatement.setString(1, tableName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
