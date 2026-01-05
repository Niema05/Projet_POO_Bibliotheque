package com.bibliotheque.dao;

import com.bibliotheque.model.Livre;
import java.sql.SQLException;
import java.util.List;

// Interface DAO pour les livres.
 
public interface LivreDAO extends DAO<Livre> {
  
    List<Livre> findByAuteur(String auteur) throws SQLException;

    /**
     * Recherche des livres par titre.
     * @return une liste de livres correspondant au titre
     * @throws SQLException si une erreur SQL survient
     */
    List<Livre> findByTitre(String titre) throws SQLException;

    /**
     * Récupère tous les livres disponibles.
     * @return une liste de livres disponibles
     * @throws SQLException si une erreur SQL survient
     */
    List<Livre> findDisponibles() throws SQLException;

    /**
     * Recherche un livre par ISBN.
     * @return le livre trouvé, null sinon
     * @throws SQLException si une erreur SQL survient
     */
    Livre findByISBN(String isbn) throws SQLException;

       /**
       Vérifie si un livre avec cet ISBN existe déjà
     * @return true si le livre existe, false sinon
     * @throws SQLException si une erreur SQL survient
     */
    boolean existsByISBN(String isbn) throws SQLException;
}


