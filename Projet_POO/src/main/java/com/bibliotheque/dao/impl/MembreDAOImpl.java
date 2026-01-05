package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.model.Membre;
import com.bibliotheque.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MembreDAOImpl implements MembreDAO {
    private Membre mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Membre(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getBoolean("actif"),
                rs.getDate("date_inscription").toLocalDate()
        );
    }

    @Override
    public void save(Membre membre) throws SQLException {
        String sql = "INSERT INTO membres (nom, prenom, email, actif, date_inscription) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            stmt.setDate(5, Date.valueOf(membre.getDateInscription()));
            stmt.executeUpdate();
        }
    }

    @Override
    public Membre findById(String id) throws SQLException {
        try {
            int idInt = Integer.parseInt(id);
            return findByIntId(idInt);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Membre findByIntId(int id) throws SQLException {
        String sql = "SELECT * FROM membres WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Membre> findAll() throws SQLException {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                membres.add(mapResultSetToEntity(rs));
            }
        }
        return membres;
    }

    @Override
    public Membre findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM membres WHERE email = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Membre> findActifs() throws SQLException {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres WHERE actif = true";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                membres.add(mapResultSetToEntity(rs));
            }
        }
        return membres;
    }

    @Override
    public void update(Membre membre) throws SQLException {
        String sql = "UPDATE membres SET nom = ?, prenom = ?, email = ?, actif = ?, date_inscription = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            stmt.setDate(5, Date.valueOf(membre.getDateInscription()));
            stmt.setInt(6, membre.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try {
            int idInt = Integer.parseInt(id);
            String sql = "DELETE FROM membres WHERE id = ?";
            try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
                stmt.setInt(1, idInt);
                stmt.executeUpdate();
            }
        } catch (NumberFormatException e) {
            throw new SQLException("ID invalide : " + id);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        return findByEmail(email) != null;
    }
}

