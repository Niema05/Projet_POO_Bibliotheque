package com.bibliotheque.model;

/**
 * Classe abstraite représentant une personne dans le système.
 */
public abstract class Personne {
    protected String nom;
    protected String prenom;
    protected String email;

    public Personne(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }



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
     * return le nom complet
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}

