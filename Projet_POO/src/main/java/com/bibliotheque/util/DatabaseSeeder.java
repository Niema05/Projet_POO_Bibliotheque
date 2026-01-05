package com.bibliotheque.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Petit utilitaire pour insérer des données de test si la table `livres` est vide.
 */
public final class DatabaseSeeder {
    private DatabaseSeeder() {}

    /**
     * Ensures there are sample books in the `livres` table.
     * @return number of rows present after seeding
     */
    public static int seedIfEmpty(Connection connection) throws SQLException {
        String countSql = "SELECT COUNT(*) AS cnt FROM livres";
        try (PreparedStatement stmt = connection.prepareStatement(countSql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int cnt = rs.getInt("cnt");
                if (cnt > 0) {
                    return cnt; // already has data
                }
            }
        }

        String insertSql = "INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            stmt.setString(1, "978-2070361563");
            stmt.setString(2, "Le Seigneur des Anneaux");
            stmt.setString(3, "J.R.R. Tolkien");
            stmt.setInt(4, 1954);
            stmt.setBoolean(5, true);
            stmt.executeUpdate();

            stmt.setString(1, "978-2070368945");
            stmt.setString(2, "Harry Potter à l'école des sorciers");
            stmt.setString(3, "J.K. Rowling");
            stmt.setInt(4, 1998);
            stmt.setBoolean(5, true);
            stmt.executeUpdate();

            stmt.setString(1, "978-2253121138");
            stmt.setString(2, "Les Misérables");
            stmt.setString(3, "Victor Hugo");
            stmt.setInt(4, 1862);
            stmt.setBoolean(5, false);
            stmt.executeUpdate();
        }

        // Return the new count
        try (PreparedStatement stmt = connection.prepareStatement(countSql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        }
        return 0;
    }
}
