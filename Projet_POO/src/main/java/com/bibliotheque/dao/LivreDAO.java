package com.bibliotheque.dao;

import com.bibliotheque.model.Livre;
import java.sql.SQLException;
import java.util.List;

// Interface DAO pour les livres.
 
public interface LivreDAO extends DAO<Livre> {
  
    List<Livre> findByAuteur(String auteur) throws SQLException;

    // Recherche des livres par titre.
     
    List<Livre> findByTitre(String titre) throws SQLException;

    // Récupère tous les livres disponibles.
     
    List<Livre> findDisponibles() throws SQLException;

    // Recherche un livre par ISBN.

    Livre findByISBN(String isbn) throws SQLException;

    //Vérifie si un livre avec cet ISBN existe déjà
  
    boolean existsByISBN(String isbn) throws SQLException;
}



