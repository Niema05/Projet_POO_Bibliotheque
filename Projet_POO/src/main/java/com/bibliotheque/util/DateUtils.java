package com.bibliotheque.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utilitaire pour les opérations sur les dates.
 */
public class DateUtils {

    /**
     * Calcule le nombre de jours de retard.
     *
     * @param dateRetourPrevue   la date de retour prévue
     * @param dateRetourEffective la date de retour effective
     * @return le nombre de jours de retard (0 si pas de retard)
     */
    public static long calculerJoursRetard(LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        if (dateRetourEffective == null) {
            return 0;
        }
        long jours = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
        return Math.max(0, jours);
    }

    /**
     * Calcule la pénalité basée sur les jours de retard.
     * Pénalité par défaut : 2 DH par jour.
     *
     * @param joursRetard le nombre de jours de retard
     * @return la pénalité en DH
     */
    public static double calculerPenalite(long joursRetard) {
        return joursRetard * 2.0;
    }

    /**
     * Ajoute un nombre de jours à une date donnée.
     *
     * @param date  la date initiale
     * @param jours le nombre de jours à ajouter
     * @return la nouvelle date
     */
    public static LocalDate ajouterJours(LocalDate date, int jours) {
        return date.plusDays(jours);
    }

    /**
     * Vérifie si une date est dépassée par rapport à aujourd'hui.
     *
     * @param date la date à vérifier
     * @return true si la date est dans le passé
     */
    public static boolean estDepassee(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Retourne le nombre de jours entre deux dates.
     *
     * @param dateDebut la date de début
     * @param dateFin   la date de fin
     * @return le nombre de jours
     */
    public static long calculerJours(LocalDate dateDebut, LocalDate dateFin) {
        return ChronoUnit.DAYS.between(dateDebut, dateFin);
    }
}
