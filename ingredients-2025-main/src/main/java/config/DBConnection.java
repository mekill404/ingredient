package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public Connection getConnection() {
        try {
            String jdbcURL = "jdbc:postgresql://localhost:5432/mini_dish_db"; 
            String user = "mini_dish_db_manager";
            String password = "mini_Dish2026!";
            
            return DriverManager.getConnection(jdbcURL, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion : Vérifiez si Postgres est lancé et si la base 'mini_dish_db' existe.", e);
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}