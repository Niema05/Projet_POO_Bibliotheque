package com.bibliotheque.model;

/**
 * Classe représentant un magazine dans la bibliothèque.
 */
public class Magazine extends Document implements Empruntable {
    private int numero;
    private String mois;
    private boolean disponible;

    /**
     * Constructeur d'un magazine.
     *
     * @param id            l'identifiant du magazine
     * @param titre         le titre du magazine
     * @param numero        le numéro du magazine
     * @param mois          le mois de publication
     * @param disponible    la disponibilité du magazine
     */
    public Magazine(String id, String titre, int numero, String mois, boolean disponible) {
        super(id, titre);
        this.numero = numero;
        this.mois = mois;
        this.disponible = disponible;
    }

    /**
     * Calcule la pénalité de retard pour un magazine.
     * Pénalité : 1 DH par jour de retard.
     *
     * @return la pénalité en DH
     */
    @Override
    public double calculerPenaliteRetard() {
        return 1.0; // 1 DH par jour
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", numero=" + numero +
                ", mois='" + mois + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
