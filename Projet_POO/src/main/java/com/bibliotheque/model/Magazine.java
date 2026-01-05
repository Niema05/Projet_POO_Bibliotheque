package com.bibliotheque.model;


public class Magazine extends Document implements Empruntable {
    private int numero;
    private String mois;
    private boolean disponible;

    public Magazine(String id, String titre, int numero, String mois, boolean disponible) {
        super(id, titre);
        this.numero = numero;
        this.mois = mois;
        this.disponible = disponible;
    }

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

