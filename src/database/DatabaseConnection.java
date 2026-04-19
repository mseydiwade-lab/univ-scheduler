package database;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:univscheduler.db";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
        return connection;
    }

    public static void fermerConnexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Erreur fermeture : " + e.getMessage());
        }
    }
}
