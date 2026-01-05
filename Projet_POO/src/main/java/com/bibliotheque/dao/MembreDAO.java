package com.bibliotheque.dao;

import com.bibliotheque.model.Membre;
import java.sql.SQLException;
import java.util.List;

public interface MembreDAO extends DAO<Membre> {
   
    void save(Membre membre) throws SQLException();
    void update(Membre membre) throws SQLException();
    void delete(int id) throws SQLException();

    Membre findByEmail(String email) throws SQLException;

    /**
     * Récupère tous les membres actifs.
     * return une liste de membres actifs
     */
    List<Membre> findActifs() throws SQLException;

    /**
     * Recherche un membre par son ID (int).
     * return le membre trouvé, null sinon
     */
    Membre findByIntId(int id) throws SQLException;

    /**
     * Vérifie si un email existe déjà dans la base de données.
     * return true si l'email existe, false sinon
     */
    boolean existsByEmail(String email) throws SQLException;
}




