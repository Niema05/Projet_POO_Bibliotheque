package com.bibliotheque.exception;

/**
 * Exception levée lors d'une validation échouée.
 */
public class ValidationException extends Exception {
    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
