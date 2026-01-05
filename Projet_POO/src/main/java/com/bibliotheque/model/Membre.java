package com.bibliotheque.model;

import java.time.LocalDate;

/**
 * Classe représentant un membre de la bibliothèque.
 */
public class Membre extends Personne {
    private int id;
    private boolean actif;
    private LocalDate dateInscription;

    /**
     * Constructeur d'un membre.
     *
     * @param nom               le nom du membre
     * @param prenom            le prénom du membre
     * @param email             l'email du membre
     * @param actif             si le membre est actif
     * @param dateInscription   la date d'inscription
     */
    public Membre(String nom, String prenom, String email, boolean actif, LocalDate dateInscription) {
        super(nom, prenom, email);
        this.actif = actif;
        this.dateInscription = dateInscription;
    }

    /**
     * Constructeur d'un membre avec ID.
     *
     * @param id                l'identifiant du membre
     * @param nom               le nom du membre
     * @param prenom            le prénom du membre
     * @param email             l'email du membre
     * @param actif             si le membre est actif
     * @param dateInscription   la date d'inscription
     */
    public Membre(int id, String nom, String prenom, String email, boolean actif, LocalDate dateInscription) {
        super(nom, prenom, email);
        this.id = id;
        this.actif = actif;
        this.dateInscription = dateInscription;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    @Override
    public String toString() {
        return "Membre{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", actif=" + actif +
                ", dateInscription=" + dateInscription +
                '}';
    }
}
