package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;
import com.bibliotheque.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les emprunts avec MySQL.
 */
public class EmpruntDAOImpl implements EmpruntDAO {

    private final LivreDAO livreDAO;
    private final MembreDAO membreDAO;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param livreDAO   le DAO des livres
     * @param membreDAO  le DAO des membres
     */
    public EmpruntDAOImpl(LivreDAO livreDAO, MembreDAO membreDAO) {
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
    }

    /**
     * Mappe un ResultSet à un objet Emprunt.
     *
     * @param rs le ResultSet
     * @return l'objet Emprunt
     * @throws SQLException si une erreur SQL survient
     */
    private Emprunt mapResultSetToEntity(ResultSet rs) throws SQLException {
        Livre livre = livreDAO.findByISBN(rs.getString("isbn"));
        Membre membre = membreDAO.findByIntId(rs.getInt("membre_id"));

        LocalDate dateRetourEffective = null;
        Date dateRetourEffectiveSQL = rs.getDate("date_retour_effective");
        if (dateRetourEffectiveSQL != null) {
            dateRetourEffective = dateRetourEffectiveSQL.toLocalDate();
        }

        return new Emprunt(
                rs.getInt("id"),
                rs.getDate("date_emprunt").toLocalDate(),
                rs.getDate("date_retour_prevue").toLocalDate(),
                dateRetourEffective,
                rs.getDouble("penalite"),
                livre,
                membre
        );
    }

    @Override
    public void save(Emprunt emprunt) throws SQLException {
        String sql = "INSERT INTO emprunts (isbn, membre_id, date_emprunt, date_retour_prevue, date_retour_effective, penalite) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, emprunt.getLivre().getIsbn());
            stmt.setInt(2, emprunt.getMembre().getId());
            stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));

            if (emprunt.getDateRetourEffective() != null) {
                stmt.setDate(5, Date.valueOf(emprunt.getDateRetourEffective()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setDouble(6, emprunt.getPenalite());
            stmt.executeUpdate();
        }
    }

    @Override
    public Emprunt findById(String id) throws SQLException {
        try {
            int idInt = Integer.parseInt(id);
            String sql = "SELECT * FROM emprunts WHERE id = ?";
            try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
                stmt.setInt(1, idInt);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new SQLException("ID invalide : " + id);
        }
        return null;
    }

    @Override
    public List<Emprunt> findAll() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                emprunts.add(mapResultSetToEntity(rs));
            }
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> findByMembre(int membreId) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE membre_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, membreId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(mapResultSetToEntity(rs));
                }
            }
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> findEnCours() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                emprunts.add(mapResultSetToEntity(rs));
            }
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> findEnRetard() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE date_retour_effective > date_retour_prevue " +
                "OR (date_retour_effective IS NULL AND date_retour_prevue < CURDATE())";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                emprunts.add(mapResultSetToEntity(rs));
            }
        }
        return emprunts;
    }

    @Override
    public int countEmpruntsEnCours(int membreId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE membre_id = ? AND date_retour_effective IS NULL";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, membreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public void update(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE emprunts SET isbn = ?, membre_id = ?, date_emprunt = ?, date_retour_prevue = ?, " +
                "date_retour_effective = ?, penalite = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, emprunt.getLivre().getIsbn());
            stmt.setInt(2, emprunt.getMembre().getId());
            stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));

            if (emprunt.getDateRetourEffective() != null) {
                stmt.setDate(5, Date.valueOf(emprunt.getDateRetourEffective()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setDouble(6, emprunt.getPenalite());
            stmt.setInt(7, emprunt.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try {
            int idInt = Integer.parseInt(id);
            String sql = "DELETE FROM emprunts WHERE id = ?";
            try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
                stmt.setInt(1, idInt);
                stmt.executeUpdate();
            }
        } catch (NumberFormatException e) {
            throw new SQLException("ID invalide : " + id);
        }
    }
}
