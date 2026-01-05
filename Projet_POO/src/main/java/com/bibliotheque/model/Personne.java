package com.bibliotheque.model;

/**
 * Classe abstraite représentant une personne dans le système.
 */
public abstract class Personne {
    protected String nom;
    protected String prenom;
    protected String email;

    /**
     * Constructeur de la personne.
     *
     * @param nom     le nom de la personne
     * @param prenom  le prénom de la personne
     * @param email   l'email de la personne
     */
    public Personne(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // Getters et Setters

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le nom complet (prénom + nom).
     *
     * @return le nom complet
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
