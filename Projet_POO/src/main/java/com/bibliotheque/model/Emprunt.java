package com.bibliotheque.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Classe représentant un emprunt dans la bibliothèque.
 
public class Emprunt {
    private int id;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private double penalite;
    private Livre livre;
    private Membre membre;

    // Constructeur d'un emprunt sans ID.
   
    public Emprunt(LocalDate dateEmprunt, LocalDate dateRetourPrevue, Livre livre, Membre membre) {
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = null;
        this.penalite = 0.0;
        this.livre = livre;
        this.membre = membre;
    }

    // Constructeur d'un emprunt avec ID.
   
    public Emprunt(int id, LocalDate dateEmprunt, LocalDate dateRetourPrevue, 
                   LocalDate dateRetourEffective, double penalite, Livre livre, Membre membre) {
        this.id = id;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.penalite = penalite;
        this.livre = livre;
        this.membre = membre;
    }

    // Calcule le nombre de jours de retard.
    
    public long calculerJoursRetard() {
        if (dateRetourEffective == null) {
            return 0;
        }
        long jours = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
        return Math.max(0, jours);
    }

    // Calcule la pénalité basée sur les jours de retard.
  
    public double calculerPenalite() {
        long joursRetard = calculerJoursRetard();
        return joursRetard * livre.calculerPenaliteRetard();
    }

    // Vérifie si l'emprunt est en retard.
     
    public boolean estEnRetard() {
        if (dateRetourEffective == null) {
            return LocalDate.now().isAfter(dateRetourPrevue);
        }
        return dateRetourEffective.isAfter(dateRetourPrevue);
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public double getPenalite() {
        return penalite;
    }

    public void setPenalite(double penalite) {
        this.penalite = penalite;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", livre='" + livre.getTitre() + '\'' +
                ", membre='" + membre.getNomComplet() + '\'' +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue=" + dateRetourPrevue +
                ", penalite=" + penalite +
                '}';
    }
}

