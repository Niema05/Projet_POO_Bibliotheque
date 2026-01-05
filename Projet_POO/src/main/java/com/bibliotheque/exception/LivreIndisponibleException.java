package com.bibliotheque.exception;

/**
 * Exception levée quand un livre n'est pas disponible pour l'emprunt.
 */
public class LivreIndisponibleException extends Exception {
    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public LivreIndisponibleException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public LivreIndisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
