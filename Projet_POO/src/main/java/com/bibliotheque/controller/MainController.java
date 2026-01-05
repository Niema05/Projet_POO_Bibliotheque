package com.bibliotheque.controller;

import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.dao.impl.MembreDAOImpl;
import com.bibliotheque.dao.impl.EmpruntDAOImpl;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.service.EmpruntService;
import com.bibliotheque.util.DatabaseConnection;
import com.bibliotheque.util.DatabaseSeeder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * Contrôleur principal de l'application JavaFX.
 */
public class MainController {

    @FXML
    private TabPane tabPane;

    private BibliothequeService bibliothequeService;
    private EmpruntService empruntService;
    private LivreController livreController;
    private MembreController membreController;
    private EmpruntController empruntController;

    /**
     * Initialise le contrôleur et charge les données.
     */
    @FXML
    public void initialize() {
        try {
            var livreDAO = new LivreDAOImpl();
            var membreDAO = new MembreDAOImpl();
            var empruntDAO = new EmpruntDAOImpl(livreDAO, membreDAO);

            // Seed de la base si nécessaire (insert données exemples si table vide)
            try {
                int total = DatabaseSeeder.seedIfEmpty(DatabaseConnection.getInstance().getConnection());
                if (total == 0) {
                    afficherErreur("Base vide", "La table `livres` est vide et le seed n'a pas pu insérer d'exemples.");
                } else {
                    afficherSucces("Base initialisée", "La table `livres` contient maintenant " + total + " ligne(s).");
                }
            } catch (Exception se) {
                // Affiche une alerte pour aider au debug (connexion, permissions, absence de table...)
                afficherErreur("Warning: Database seeding failed", se.getMessage());
            }

            bibliothequeService = new BibliothequeService(livreDAO, membreDAO);
            empruntService = new EmpruntService(empruntDAO, livreDAO, membreDAO);

            // Charger et injecter contrôleurs enfants après que la scène soit visible
            Platform.runLater(this::chargerControleurs);

        } catch (Exception e) {
            afficherErreur("Erreur d'initialisation", "Impossible d'initialiser l'application : " + e.getMessage());
        }
    }

    /**
     * Charge les contrôleurs enfants et les services.
     */
    private void chargerControleurs() {
        try {
            System.out.println("DEBUG: Starting chargerControleurs()");
            // Charger Livre Controller
            FXMLLoader loaderLivres = new FXMLLoader(getClass().getResource("/fxml/livres.fxml"));
            var rootLivres = loaderLivres.<javafx.scene.Node>load();
            this.livreController = loaderLivres.getController();
            System.out.println("DEBUG: Loaded livreController, calling setService()");
            this.livreController.setService(bibliothequeService);
            if (tabPane.getTabs().size() > 0) {
                tabPane.getTabs().get(0).setContent(rootLivres);
            }

            // Charger Membre Controller
            FXMLLoader loaderMembres = new FXMLLoader(getClass().getResource("/fxml/membres.fxml"));
            var rootMembres = loaderMembres.<javafx.scene.Node>load();
            this.membreController = loaderMembres.getController();
            System.out.println("DEBUG: Loaded membreController, calling setService()");
            this.membreController.setService(bibliothequeService);
            if (tabPane.getTabs().size() > 1) {
                tabPane.getTabs().get(1).setContent(rootMembres);
            }

            // Charger Emprunt Controller
            FXMLLoader loaderEmprunts = new FXMLLoader(getClass().getResource("/fxml/emprunts.fxml"));
            var rootEmprunts = loaderEmprunts.<javafx.scene.Node>load();
            this.empruntController = loaderEmprunts.getController();
            System.out.println("DEBUG: Loaded empruntController, calling setServices()");
            this.empruntController.setServices(bibliothequeService, empruntService);
            if (tabPane.getTabs().size() > 2) {
                tabPane.getTabs().get(2).setContent(rootEmprunts);
            }

            // Refresh relevant controller data when user switches tabs
            tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                try {
                    int idx = newIndex.intValue();
                    if (idx == 0 && this.livreController != null) {
                        this.livreController.refreshData();
                    } else if (idx == 1 && this.membreController != null) {
                        this.membreController.refreshData();
                    } else if (idx == 2 && this.empruntController != null) {
                        this.empruntController.refreshData();
                    }
                } catch (Exception ignored) {
                }
            });
            System.out.println("DEBUG: chargerControleurs() completed");

        } catch (IOException e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les contrôleurs : " + e.getMessage());
        }
    }

    /**
     * Affiche une alerte d'erreur.
     *
     * @param titre   le titre de l'alerte
     * @param message le message d'erreur
     */
    protected void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de succès.
     *
     * @param titre   le titre de l'alerte
     * @param message le message de succès
     */
    protected void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getters pour les services (utilisés par les contrôleurs enfants)

    public BibliothequeService getBibliothequeService() {
        return bibliothequeService;
    }

    public EmpruntService getEmpruntService() {
        return empruntService;
    }
}
