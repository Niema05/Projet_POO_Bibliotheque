package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;
import com.bibliotheque.util.StringValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class BibliothequeService {
    private final LivreDAO livreDAO;
    private final MembreDAO membreDAO;

    
    public BibliothequeService(LivreDAO livreDAO, MembreDAO membreDAO) {
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
    }

   

   
    public void ajouterLivre(Livre livre) throws ValidationException, SQLException {
        // Validation
        StringValidator.validateISBN(livre.getIsbn());
        StringValidator.validateTitre(livre.getTitre());
        StringValidator.validateNotEmpty(livre.getAuteur(), "L'auteur");
        StringValidator.validateAnneePublication(livre.getAnneePublication());

        // Vérifier que le livre n'existe pas déjà
        if (livreDAO.existsByISBN(livre.getIsbn())) {
            throw new ValidationException("Un livre avec cet ISBN existe déjà : " + livre.getIsbn());
        }

        livreDAO.save(livre);
    }

    public void modifierLivre(Livre livre) throws ValidationException, SQLException {
        StringValidator.validateISBN(livre.getIsbn());
        StringValidator.validateTitre(livre.getTitre());
        StringValidator.validateNotEmpty(livre.getAuteur(), "L'auteur");
        StringValidator.validateAnneePublication(livre.getAnneePublication());

        livreDAO.update(livre);
    }

    
    public void supprimerLivre(String isbn) throws SQLException {
        livreDAO.delete(isbn);
    }

    
    public List<Livre> rechercherLivres(String critere) throws SQLException {
        List<Livre> resultats = new ArrayList<>();
        resultats.addAll(livreDAO.findByTitre(critere));
        
        List<Livre> parAuteur = livreDAO.findByAuteur(critere);
        for (Livre livre : parAuteur) {
            if (!resultats.contains(livre)) {
                resultats.add(livre);
            }
        }
        
        return resultats;
    }

    public List<Livre> getLivresDisponibles() throws SQLException {
        return livreDAO.findDisponibles();
    }

   
    public List<Livre> getTousLesLivres() throws SQLException {
        return livreDAO.findAll();
    }

    //  MÉTHODES POUR LES MEMBRES 

    public void ajouterMembre(Membre membre) throws ValidationException, SQLException {
        // Validation
        StringValidator.validateNomPrenom(membre.getNom(), membre.getPrenom());
        StringValidator.validateEmail(membre.getEmail());

        // Vérifier que l'email n'existe pas déjà
        if (membreDAO.existsByEmail(membre.getEmail())) {
            throw new ValidationException("Un membre avec cet email existe déjà : " + membre.getEmail());
        }

        membreDAO.save(membre);
    }


    public void modifierMembre(Membre membre) throws ValidationException, SQLException {
        StringValidator.validateNomPrenom(membre.getNom(), membre.getPrenom());
        StringValidator.validateEmail(membre.getEmail());

        membreDAO.update(membre);
    }

    
    public void activerDesactiverMembre(int id, boolean actif) throws SQLException {
        Membre membre = membreDAO.findByIntId(id);
        if (membre != null) {
            membre.setActif(actif);
            membreDAO.update(membre);
        }
    }

   
    public List<Membre> rechercherMembres(String critere) throws SQLException {
        List<Membre> resultats = new ArrayList<>();
        List<Membre> tous = membreDAO.findAll();
        String q = critere == null ? "" : critere.trim().toLowerCase();
        for (Membre membre : tous) {
            String nom = membre.getNom() == null ? "" : membre.getNom().toLowerCase();
            String prenom = membre.getPrenom() == null ? "" : membre.getPrenom().toLowerCase();
            String email = membre.getEmail() == null ? "" : membre.getEmail().toLowerCase();
            String nomComplet = (prenom + " " + nom).trim();

            if (nom.contains(q) || prenom.contains(q) || email.contains(q) || nomComplet.contains(q)) {
                resultats.add(membre);
            }
        }
        
        return resultats;
    }

    
    public List<Membre> getMembresActifs() throws SQLException {
        return membreDAO.findActifs();
    }

    
    public List<Membre> getTousLesMembres() throws SQLException {
        return membreDAO.findAll();
    }

  
    public Membre getMembre(int id) throws SQLException {
        return membreDAO.findByIntId(id);
    }

    
    public List<Emprunt> getHistoriqueEmprunts(int membreId) throws SQLException {
        return new ArrayList<>();
    }

   
    public String getStatistiques() throws SQLException {
        int totalLivres = livreDAO.findAll().size();
        int livresDisponibles = livreDAO.findDisponibles().size();
        int totalMembres = membreDAO.findAll().size();
        int membresActifs = membreDAO.findActifs().size();
        
        StringBuilder stats = new StringBuilder();
        stats.append("📊STATISTIQUES\n");
        stats.append("================\n");
        stats.append("Livres totaux: ").append(totalLivres).append("\n");
        stats.append("Livres disponibles: ").append(livresDisponibles).append("\n");
        stats.append("Livres empruntés: ").append(totalLivres - livresDisponibles).append("\n");
        stats.append("Membres totaux: ").append(totalMembres).append("\n");
        stats.append("Membres actifs: ").append(membresActifs).append("\n");
        stats.append("Membres inactifs: ").append(totalMembres - membresActifs);
        
        return stats.toString();
    }
}


