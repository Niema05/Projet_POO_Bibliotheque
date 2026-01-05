package com.bibliotheque.exception;

/**
 * Exception levée quand un membre n'est pas actif.
 */
public class MembreInactifException extends Exception {
    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public MembreInactifException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public MembreInactifException(String message, Throwable cause) {
        super(message, cause);
    }
}
