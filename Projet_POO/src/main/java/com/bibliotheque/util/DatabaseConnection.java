package com.bibliotheque.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton pour gérer la connexion à la base de données MySQL.
 * Utilise le pattern Double-Checked Locking pour la thread-safety.
 */
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Constructeur privé pour éviter l'instanciation.
     *
     * @throws SQLException si la connexion échoue
     */
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            throw new SQLException("Impossible de se connecter à la base de données", e);
        }
    }

    /**
     * Retourne l'instance unique de DatabaseConnection (Singleton avec Double-Checked Locking).
     *
     * @return l'instance unique
     * @throws SQLException si la connexion échoue
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Retourne la connexion active.
     *
     * @return la connexion MySQL
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Ferme la connexion à la base de données.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée avec succès");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    /**
     * Teste la connexion à la base de données.
     *
     * @return true si la connexion est valide
     */
    public boolean testConnection() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
