package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;
import com.bibliotheque.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les livres avec MySQL.
 */
public class LivreDAOImpl implements LivreDAO {

    /**
     * Mappe un ResultSet à un objet Livre.
     *
     * @param rs le ResultSet
     * @return l'objet Livre
     * @throws SQLException si une erreur SQL survient
     */
    private Livre mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Livre(
                rs.getString("isbn"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getInt("annee_publication"),
                rs.getBoolean("disponible")
        );
    }

    @Override
    public void save(Livre livre) throws SQLException {
        String sql = "INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, livre.getIsbn());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.isDisponible());
            stmt.executeUpdate();
        }
    }

    @Override
    public Livre findById(String isbn) throws SQLException {
        return findByISBN(isbn);
    }

    @Override
    public Livre findByISBN(String isbn) throws SQLException {
        String sql = "SELECT * FROM livres WHERE isbn = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Livre> findAll() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                livres.add(mapResultSetToEntity(rs));
            }
        }
        return livres;
    }

    @Override
    public List<Livre> findByAuteur(String auteur) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE auteur LIKE ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + auteur + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToEntity(rs));
                }
            }
        }
        return livres;
    }

    @Override
    public List<Livre> findByTitre(String titre) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE titre LIKE ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + titre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToEntity(rs));
                }
            }
        }
        return livres;
    }

    @Override
    public List<Livre> findDisponibles() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = true";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                livres.add(mapResultSetToEntity(rs));
            }
        }
        return livres;
    }

    @Override
    public void update(Livre livre) throws SQLException {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, annee_publication = ?, disponible = ? WHERE isbn = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setInt(3, livre.getAnneePublication());
            stmt.setBoolean(4, livre.isDisponible());
            stmt.setString(5, livre.getIsbn());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String isbn) throws SQLException {
        String sql = "DELETE FROM livres WHERE isbn = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean existsByISBN(String isbn) throws SQLException {
        return findByISBN(isbn) != null;
    }
}
