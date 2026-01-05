package com.bibliotheque.model;


public class Livre extends Document implements Empruntable {
    private String isbn;
    private String auteur;
    private int anneePublication;
    private boolean disponible;

  
    public Livre(String isbn, String titre, String auteur, int anneePublication, boolean disponible) {
        super(isbn, titre);
        this.isbn = isbn;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

   
    @Override
    public double calculerPenaliteRetard() {
        return 2.0; // 2 DH par jour
    }

    @Override
    public boolean peutEtreEmprunte() {
        return disponible;
    }

    @Override
    public void emprunter() {
        this.disponible = false;
    }

    @Override
    public void retourner() {
        this.disponible = true;
    }

    // Getters et Setters

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Livre{" +
                "isbn='" + isbn + '\'' +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}

