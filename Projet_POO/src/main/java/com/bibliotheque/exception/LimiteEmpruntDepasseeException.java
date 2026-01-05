package com.bibliotheque.exception;

/**
 * Exception levée quand la limite d'emprunt est dépassée.
 */
public class LimiteEmpruntDepasseeException extends Exception {
    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public LimiteEmpruntDepasseeException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public LimiteEmpruntDepasseeException(String message, Throwable cause) {
        super(message, cause);
    }
}
