package com.bibliotheque.model;
public abstract class Document {
    protected String id;
    protected String titre;


    public Document(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }

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

