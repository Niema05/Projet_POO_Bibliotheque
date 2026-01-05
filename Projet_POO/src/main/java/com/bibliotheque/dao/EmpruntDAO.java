package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les emprunts.
 */
public interface EmpruntDAO extends DAO<Emprunt> {
    /**
     * Récupère tous les emprunts d'un membre.
     *
     * @param membreId l'identifiant du membre
     * @return une liste des emprunts du membre
     * @throws SQLException si une erreur SQL survient
     */
    List<Emprunt> findByMembre(int membreId) throws SQLException;

    /**
     * Récupère tous les emprunts en cours (non retournés).
     *
     * @return une liste des emprunts en cours
     * @throws SQLException si une erreur SQL survient
     */
    List<Emprunt> findEnCours() throws SQLException;

    /**
     * Récupère tous les emprunts en retard.
     *
     * @return une liste des emprunts en retard
     * @throws SQLException si une erreur SQL survient
     */
    List<Emprunt> findEnRetard() throws SQLException;

    /**
     * Compte le nombre d'emprunts en cours pour un membre.
     *
     * @param membreId l'identifiant du membre
     * @return le nombre d'emprunts en cours
     * @throws SQLException si une erreur SQL survient
     */
    int countEmpruntsEnCours(int membreId) throws SQLException;
}
