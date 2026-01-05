package com.bibliotheque.model;

/**
 * Interface pour les documents qui peuvent être empruntés.
 */
public interface Empruntable {
    /**
     * Vérifie si le document peut être emprunté.
     *
     * @return true si le document est disponible, false sinon
     */
    boolean peutEtreEmprunte();

    /**
     * Marque le document comme emprunté.
     */
    void emprunter();

    /**
     * Marque le document comme retourné.
     */
    void retourner();
}
