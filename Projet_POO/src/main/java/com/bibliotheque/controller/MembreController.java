package com.bibliotheque.controller;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Membre;
import com.bibliotheque.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur pour la gestion des membres.
 */
public class MembreController {

    @FXML
    private TableView<Membre> tableViewMembres;
    @FXML
    private TableColumn<Membre, Integer> colId;
    @FXML
    private TableColumn<Membre, String> colNom;
    @FXML
    private TableColumn<Membre, String> colPrenom;
    @FXML
    private TableColumn<Membre, String> colEmail;
    @FXML
    private TableColumn<Membre, Boolean> colActif;
    @FXML
    private TableColumn<Membre, LocalDate> colDateInscription;

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfEmail;
    @FXML
    private CheckBox cbActif;

    @FXML
    private TextField tfRecherche;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnRechercher;

    private BibliothequeService service;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue() != null ? cell.getValue().getId() : null));
        colNom.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue() != null ? cell.getValue().getNom() : ""));
        colPrenom.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue() != null ? cell.getValue().getPrenom() : ""));
        colEmail.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue() != null ? cell.getValue().getEmail() : ""));
        colActif.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue() != null ? cell.getValue().isActif() : null));
        colDateInscription.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue() != null ? cell.getValue().getDateInscription() : null));
    }

    /**
     * Définit le service.
     *
     * @param service le service de bibliothèque
     */
    public void setService(BibliothequeService service) {
        System.out.println("DEBUG: setService() called in MembreController");
        this.service = service;
        if (service != null) {
            chargerMembres();
        } else {
            System.err.println("DEBUG: setService() called with null service in MembreController!");
        }
    }

    /**
     * Refresh data from service (public entrypoint for external triggers).
     */
    public void refreshData() {
        chargerMembres();
    }

    /**
     * Charge tous les membres dans le tableau.
     */
    private void chargerMembres() {
        if (service == null) {
            System.err.println("DEBUG: chargerMembres() called but service is null!");
            return;
        }
        if (tableViewMembres == null) {
            System.err.println("DEBUG: chargerMembres() called but tableViewMembres is null!");
            return;
        }
        try {
            List<Membre> membres = service.getTousLesMembres();
            System.out.println("DEBUG: chargerMembres() retrieved " + membres.size() + " members from service");
            for (int i = 0; i < Math.min(5, membres.size()); i++) {
                System.out.println("DEBUG: membre[" + i + "] = " + membres.get(i));
            }
            ObservableList<Membre> data = FXCollections.observableArrayList(membres);
            javafx.application.Platform.runLater(() -> {
                tableViewMembres.setItems(data);
                System.out.println("DEBUG: chargerMembres() set " + data.size() + " members to tableView (UI thread)");
            });
        } catch (SQLException e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les membres : " + e.getMessage());
        }
    }

    /**
     * Ajoute un nouveau membre.
     */
    @FXML
    public void handleAjouter() {
        try {
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String email = tfEmail.getText();
            boolean actif = cbActif.isSelected();

            Membre membre = new Membre(nom, prenom, email, actif, LocalDate.now());
            service.ajouterMembre(membre);

            afficherSucces("Succès", "Membre ajouté avec succès!");
            nettoyerFormulaire();
            chargerMembres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Modifie le membre sélectionné.
     */
    @FXML
    public void handleModifier() {
        Membre selected = tableViewMembres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un membre");
            return;
        }

        try {
            selected.setNom(tfNom.getText());
            selected.setPrenom(tfPrenom.getText());
            selected.setEmail(tfEmail.getText());
            selected.setActif(cbActif.isSelected());

            service.modifierMembre(selected);

            afficherSucces("Succès", "Membre modifié avec succès!");
            nettoyerFormulaire();
            chargerMembres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Supprime le membre sélectionné.
     */
    @FXML
    public void handleSupprimer() {
        Membre selected = tableViewMembres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un membre");
            return;
        }

        try {
            // Dans une vraie application, on pourrait avoir une méthode supprimant le membre
            // Pour l'instant, on le désactive
            service.activerDesactiverMembre(selected.getId(), false);
            afficherSucces("Succès", "Membre désactivé avec succès!");
            nettoyerFormulaire();
            chargerMembres();
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Recherche des membres.
     */
    @FXML
    public void handleRechercher() {
        String critere = tfRecherche.getText();
        if (critere.isEmpty()) {
            chargerMembres();
            return;
        }

        try {
            List<Membre> membres = service.rechercherMembres(critere);
            ObservableList<Membre> data = FXCollections.observableArrayList(membres);
            tableViewMembres.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }

    /**
     * Affiche l'historique des emprunts du membre sélectionné.
     */
    @FXML
    public void handleHistoriqueEmprunts() {
        Membre selected = tableViewMembres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un membre");
            return;
        }

        try {
            // Placeholder: appelle une méthode future pour récupérer l'historique
            afficherSucces("Historique", "Historique des emprunts de " + selected.getNomComplet() + 
                          "\n(Fonctionnalité à compléter avec EmpruntService)");
        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible de charger l'historique : " + e.getMessage());
        }
    }

    /**
     * Nettoie le formulaire.
     */
    private void nettoyerFormulaire() {
        tfNom.clear();
        tfPrenom.clear();
        tfEmail.clear();
        cbActif.setSelected(true);
    }

    /**
     * Affiche une alerte d'erreur.
     */
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de succès.
     */
    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
