package com.bibliotheque.dao;

import com.bibliotheque.model.Membre;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les membres.
 */
public interface MembreDAO extends DAO<Membre> {
    /**
     * Recherche un membre par email.
     *
     * @param email l'email du membre
     * @return le membre trouvé, null sinon
     * @throws SQLException si une erreur SQL survient
     */
    void save(Membre membre) throws SQLException();
    void update(Membre membre) throws SQLException();
    void delete(int id) throws SQLException();

    Membre findByEmail(String email) throws SQLException;

    /**
     * Récupère tous les membres actifs.
     *
     * @return une liste de membres actifs
     * @throws SQLException si une erreur SQL survient
     */
    List<Membre> findActifs() throws SQLException;

    /**
     * Recherche un membre par son ID (int).
     *
     * @param id l'identifiant du membre
     * @return le membre trouvé, null sinon
     * @throws SQLException si une erreur SQL survient
     */
    Membre findByIntId(int id) throws SQLException;

    /**
     * Vérifie si un email existe déjà dans la base de données.
     *
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     * @throws SQLException si une erreur SQL survient
     */
    boolean existsByEmail(String email) throws SQLException;
}

