package com.bibliotheque.model;

/**
 * Classe abstraite représentant un document dans la bibliothèque.
 * Tous les documents doivent calculer une pénalité de retard.
 */
public abstract class Document {
    protected String id;
    protected String titre;

    /**
     * Constructeur du document.
     *
     * @param id    l'identifiant unique du document
     * @param titre le titre du document
     */
    public Document(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    /**
     * Calcule la pénalité en cas de retard.
     * À implémenter par les classes concrètes.
     *
     * @return la pénalité en DH
     */
    public abstract double calculerPenaliteRetard();

    // Getters et Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
