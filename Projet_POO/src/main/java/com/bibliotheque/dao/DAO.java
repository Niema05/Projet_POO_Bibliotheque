package com.bibliotheque.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface générique pour les opérations CRUD.
 *
 * @param <T> le type d'entité
 */
public interface DAO<T> {
    /**
     * Sauvegarde une entité.
     *
     * @param entity l'entité à sauvegarder
     * @throws SQLException si une erreur SQL survient
     */
    void save(T entity) throws SQLException;

    /**
     * Récupère une entité par son ID.
     *
     * @param id l'identifiant de l'entité
     * @return l'entité trouvée, null sinon
     * @throws SQLException si une erreur SQL survient
     */
    T findById(String id) throws SQLException;

    /**
     * Récupère toutes les entités.
     *
     * @return une liste de toutes les entités
     * @throws SQLException si une erreur SQL survient
     */
    List<T> findAll() throws SQLException;

    /**
     * Met à jour une entité.
     *
     * @param entity l'entité à mettre à jour
     * @throws SQLException si une erreur SQL survient
     */
    void update(T entity) throws SQLException;

    /**
     * Supprime une entité par son ID.
     *
     * @param id l'identifiant de l'entité à supprimer
     * @throws SQLException si une erreur SQL survient
     */
    void delete(String id) throws SQLException;
}
