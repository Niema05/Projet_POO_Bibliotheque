package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;
import java.sql.SQLException;
import java.util.List;

// Interface DAO pour les emprunts.
 
public interface EmpruntDAO extends DAO<Emprunt> {
    // Récupère tous les emprunts d'un membre.
     
    List<Emprunt> findByMembre(int membreId) throws SQLException;

    // Récupère tous les emprunts en cours (non retournés).
     
    List<Emprunt> findEnCours() throws SQLException;

    // Récupère tous les emprunts en retard.
     
    List<Emprunt> findEnRetard() throws SQLException;

    // Compte le nombre d'emprunts en cours pour un membre.
     
    int countEmpruntsEnCours(int membreId) throws SQLException;
}

