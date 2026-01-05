package com.bibliotheque.util;

import com.bibliotheque.exception.ValidationException;

/**
 * Utilitaire pour la validation des chaînes de caractères.
 */
public class StringValidator {

    /**
     * Valide une adresse email.
     *
     * @param email l'email à valider
     * @throws ValidationException si l'email n'est pas valide
     */
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("L'email ne peut pas être vide");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new ValidationException("Format d'email invalide : " + email);
        }
    }

    /**
     * Valide un numéro ISBN.
     *
     * @param isbn l'ISBN à valider
     * @throws ValidationException si l'ISBN n'est pas valide
     */
    public static void validateISBN(String isbn) throws ValidationException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new ValidationException("L'ISBN ne peut pas être vide");
        }

        String isbnRegex = "^978-\\d{10}$";
        if (!isbn.matches(isbnRegex)) {
            throw new ValidationException("Format ISBN invalide. Format attendu : 978-XXXXXXXXXX");
        }
    }

    /**
     * Valide qu'une chaîne n'est pas vide.
     *
     * @param value     la valeur à valider
     * @param fieldName le nom du champ (pour le message d'erreur)
     * @throws ValidationException si la chaîne est vide
     */
    public static void validateNotEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " ne peut pas être vide");
        }
    }

    /**
     * Valide que le nom et le prénom ne sont pas vides et respectent un format.
     *
     * @param nom le nom
     * @param prenom le prénom
     * @throws ValidationException si les données sont invalides
     */
    public static void validateNomPrenom(String nom, String prenom) throws ValidationException {
        validateNotEmpty(nom, "Le nom");
        validateNotEmpty(prenom, "Le prénom");

        if (nom.length() > 50) {
            throw new ValidationException("Le nom ne peut pas dépasser 50 caractères");
        }
        if (prenom.length() > 50) {
            throw new ValidationException("Le prénom ne peut pas dépasser 50 caractères");
        }
    }

    /**
     * Valide un titre de livre/magazine.
     *
     * @param titre le titre à valider
     * @throws ValidationException si le titre est invalide
     */
    public static void validateTitre(String titre) throws ValidationException {
        validateNotEmpty(titre, "Le titre");
        if (titre.length() > 200) {
            throw new ValidationException("Le titre ne peut pas dépasser 200 caractères");
        }
    }

    /**
     * Valide une année de publication.
     *
     * @param annee l'année à valider
     * @throws ValidationException si l'année est invalide
     */
    public static void validateAnneePublication(int annee) throws ValidationException {
        int anneeActuelle = java.time.LocalDate.now().getYear();
        if (annee < 1000 || annee > anneeActuelle) {
            throw new ValidationException("L'année de publication doit être entre 1000 et " + anneeActuelle);
        }
    }
}
