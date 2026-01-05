package com.bibliotheque.service;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.bibliotheque.exception.LivreIndisponibleException;
import com.bibliotheque.exception.MembreInactifException;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;
import com.bibliotheque.util.DateUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Service métier pour la gestion des emprunts.
 
public class EmpruntService {
    private final EmpruntDAO empruntDAO;
    private final LivreDAO livreDAO;
    private final MembreDAO membreDAO;

    private static final int LIMITE_EMPRUNTS = 3;
    private static final int JOURS_EMPRUNT = 14;

    // Constructeur avec injection des dépendances.
    
    public EmpruntService(EmpruntDAO empruntDAO, LivreDAO livreDAO, MembreDAO membreDAO) {
        this.empruntDAO = empruntDAO;
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
    }

    // Emprunte un livre pour un membre.
   
    public Emprunt emprunterLivre(String isbn, int membreId) 
            throws MembreInactifException, LivreIndisponibleException, 
                   LimiteEmpruntDepasseeException, SQLException {
        
        // Récupérer le livre
        Livre livre = livreDAO.findByISBN(isbn);
        if (livre == null) {
            throw new LivreIndisponibleException("Livre non trouvé : " + isbn);
        }

        // Récupérer le membre
        Membre membre = membreDAO.findByIntId(membreId);
        if (membre == null) {
            throw new MembreInactifException("Membre non trouvé : " + membreId);
        }

        // Vérifier que le membre est actif
        if (!membre.isActif()) {
            throw new MembreInactifException("Le membre " + membre.getNomComplet() + " n'est pas actif");
        }

        // Vérifier que le livre est disponible
        if (!livre.peutEtreEmprunte()) {
            throw new LivreIndisponibleException("Le livre '" + livre.getTitre() + "' n'est pas disponible");
        }

        // Vérifier que le membre n'a pas déjà 3 emprunts en cours
        int empruntEnCours = empruntDAO.countEmpruntsEnCours(membreId);
        if (empruntEnCours >= LIMITE_EMPRUNTS) {
            throw new LimiteEmpruntDepasseeException(
                    "Le membre " + membre.getNomComplet() + " a atteint la limite de " + LIMITE_EMPRUNTS + " emprunts"
            );
        }

        // Créer l'emprunt
        LocalDate dateEmprunt = LocalDate.now();
        LocalDate dateRetourPrevue = DateUtils.ajouterJours(dateEmprunt, JOURS_EMPRUNT);
        
        Emprunt emprunt = new Emprunt(dateEmprunt, dateRetourPrevue, livre, membre);
        
        // Sauvegarder l'emprunt
        empruntDAO.save(emprunt);
        
        // Marquer le livre comme non disponible
        livre.emprunter();
        livreDAO.update(livre);
        
        return emprunt;
    }

    // Emprunte un livre en utilisant une date de retour prévue fournie par l'utilisateur.
     
    public Emprunt emprunterLivre(String isbn, int membreId, LocalDate dateRetourPrevue)
            throws MembreInactifException, LivreIndisponibleException,
            LimiteEmpruntDepasseeException, SQLException {

        Livre livre = livreDAO.findByISBN(isbn);
        if (livre == null) {
            throw new LivreIndisponibleException("Livre non trouvé : " + isbn);
        }

        Membre membre = membreDAO.findByIntId(membreId);
        if (membre == null) {
            throw new MembreInactifException("Membre non trouvé : " + membreId);
        }

        if (!membre.isActif()) {
            throw new MembreInactifException("Le membre " + membre.getNomComplet() + " n'est pas actif");
        }

        if (!livre.peutEtreEmprunte()) {
            throw new LivreIndisponibleException("Le livre '" + livre.getTitre() + "' n'est pas disponible");
        }

        int empruntEnCours = empruntDAO.countEmpruntsEnCours(membreId);
        if (empruntEnCours >= LIMITE_EMPRUNTS) {
            throw new LimiteEmpruntDepasseeException(
                    "Le membre " + membre.getNomComplet() + " a atteint la limite de " + LIMITE_EMPRUNTS + " emprunts"
            );
        }

        LocalDate dateEmprunt = LocalDate.now();
        // ensure provided return date is not before loan date
        if (dateRetourPrevue == null || dateRetourPrevue.isBefore(dateEmprunt)) {
            dateRetourPrevue = DateUtils.ajouterJours(dateEmprunt, JOURS_EMPRUNT);
        }

        Emprunt emprunt = new Emprunt(dateEmprunt, dateRetourPrevue, livre, membre);
        empruntDAO.save(emprunt);

        livre.emprunter();
        livreDAO.update(livre);

        return emprunt;
    }

    // Retourne un livre emprunté.
  
    public void retournerLivre(int empruntId) throws SQLException {
        Emprunt emprunt = empruntDAO.findById(String.valueOf(empruntId));
        if (emprunt == null) {
            throw new SQLException("Emprunt non trouvé : " + empruntId);
        }

        // Mettre à jour la date de retour effective
        emprunt.setDateRetourEffective(LocalDate.now());
        
        // Calculer la pénalité si retard
        double penalite = calculerPenalite(emprunt);
        emprunt.setPenalite(penalite);
        
        // Sauvegarder l'emprunt
        empruntDAO.update(emprunt);
        
        // Marquer le livre comme disponible
        Livre livre = emprunt.getLivre();
        livre.retourner();
        livreDAO.update(livre);
    }

    //Récupère tous les emprunts en retard.
     
    public List<Emprunt> getEmpruntsEnRetard() throws SQLException {
        return empruntDAO.findEnRetard();
    }

    //Récupère tous les emprunts en cours.

    public List<Emprunt> getEmpruntsEnCours() throws SQLException {
        return empruntDAO.findEnCours();
    }

    // Récupère tous les emprunts d'un membre.
     
    public List<Emprunt> getEmpruntsParMembre(int membreId) throws SQLException {
        return empruntDAO.findByMembre(membreId);
    }

    //Calcule la pénalité pour un emprunt.
    public double calculerPenalite(Emprunt emprunt) {
        return emprunt.calculerPenalite();
    }

    //Récupère tous les emprunts.
    
    public List<Emprunt> getTousLesEmprunts() throws SQLException {
        return empruntDAO.findAll();
    }

    // Compte le nombre d'emprunts en cours pour un membre.
     
    public int countEmpruntsEnCours(int membreId) throws SQLException {
        return empruntDAO.countEmpruntsEnCours(membreId);
    }
}

