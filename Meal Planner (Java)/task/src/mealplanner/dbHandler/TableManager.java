package mealplanner.dbHandler;

import mealplanner.enums.SqlSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableManager {

    private static final Logger logger = LoggerFactory.getLogger(TableManager.class);
    private final Connection dbConnection = ConnectionManager.getConnection();

    public void createTable() {

        try (PreparedStatement createMealsTable = dbConnection.prepareStatement(SqlSchema.MEALS.getSql());
             PreparedStatement createIngredientsTable = dbConnection.prepareStatement(SqlSchema.INGREDIENTS.getSql());
             PreparedStatement createPlanTable = dbConnection.prepareStatement(SqlSchema.PLAN.getSql())) {
            dbConnection.setAutoCommit(false);
            createMealsTable.executeUpdate();
            createIngredientsTable.executeUpdate();
            createPlanTable.executeUpdate();
            dbConnection.commit();
        } catch (SQLException e) {
            logger.error("Error while creating tables: {}, SQLState: {}", e.getMessage(), e.getSQLState());
            try {
                dbConnection.rollback();
                logger.info("Transaction rolled back successfully.");
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback: {}", rollbackEx.getMessage(), rollbackEx);
            }
        }
    }
}
