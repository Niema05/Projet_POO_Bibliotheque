package com.bibliotheque.controller;

import com.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.bibliotheque.exception.LivreIndisponibleException;
import com.bibliotheque.exception.MembreInactifException;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.service.EmpruntService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class EmpruntController {

    @FXML
    private TableView<Emprunt> tableViewEmprunts;
    @FXML
    private TableColumn<Emprunt, Integer> colId;
    @FXML
    private TableColumn<Emprunt, String> colLivre;
    @FXML
    private TableColumn<Emprunt, String> colMembre;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateEmprunt;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateRetourPrevue;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateRetourEffective;
    @FXML
    private TableColumn<Emprunt, Double> colPenalite;

    @FXML
    private ComboBox<String> comboLivres;
    @FXML
    private ComboBox<String> comboMembres;
    @FXML
    private javafx.scene.control.DatePicker dpDateRetourPrevue;
    @FXML
    private Button btnEmprunter;
    @FXML
    private Button btnRetourner;
    @FXML
    private Button btnAfficherEnCours;
    @FXML
    private Button btnAfficherEnRetard;
    @FXML
    private Button btnAfficherTous;

    private BibliothequeService bibliothequeService;
    private EmpruntService empruntService;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));

        // Use explicit cell value factories for nested properties to avoid PropertyValueFactory nested-resolution issues
        colLivre.setCellValueFactory(cell -> {
            var emprunt = cell.getValue();
            String titre = emprunt != null && emprunt.getLivre() != null ? emprunt.getLivre().getTitre() : "";
            return new javafx.beans.property.ReadOnlyStringWrapper(titre);
        });

        colMembre.setCellValueFactory(cell -> {
            var emprunt = cell.getValue();
            String nom = emprunt != null && emprunt.getMembre() != null ? emprunt.getMembre().getNomComplet() : "";
            return new javafx.beans.property.ReadOnlyStringWrapper(nom);
        });

        colDateEmprunt.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateEmprunt"));
        colDateRetourPrevue.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateRetourPrevue"));
        colDateRetourEffective.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateRetourEffective"));
        colPenalite.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue() != null ? cell.getValue().getPenalite() : 0.0));
    }

    /**
     * Définit les services.
     *
     * @param bibliothequeService le service de bibliothèque
     * @param empruntService      le service d'emprunt
     */
    public void setServices(BibliothequeService bibliothequeService, EmpruntService empruntService) {
        this.bibliothequeService = bibliothequeService;
        this.empruntService = empruntService;
        chargerDonnees();
    }

    /**
     * Refresh data from service (public entrypoint for external triggers).
     */
    public void refreshData() {
        chargerDonnees();
    }

    /**
     * Charge tous les emprunts et les données des combos.
     */
    private void chargerDonnees() {
        try {
            chargerLivres();
            chargerMembres();
            chargerEmprunts();
        } catch (SQLException e) {
            afficherErreur("Erreur de chargement", e.getMessage());
        }
    }

    /**
     * Charge les livres dans le combo.
     */
    private void chargerLivres() throws SQLException {
        var livres = bibliothequeService.getTousLesLivres();
        ObservableList<String> items = FXCollections.observableArrayList();
        livres.forEach(l -> items.add(l.getIsbn() + " - " + l.getTitre()));
        comboLivres.setItems(items);
    }

    /**
     * Charge les membres dans le combo.
     */
    private void chargerMembres() throws SQLException {
        var membres = bibliothequeService.getTousLesMembres();
        ObservableList<String> items = FXCollections.observableArrayList();
        membres.forEach(m -> items.add(m.getId() + " - " + m.getNomComplet()));
        comboMembres.setItems(items);
    }

    /**
     * Charge tous les emprunts dans le tableau.
     */
    private void chargerEmprunts() throws SQLException {
        var emprunts = empruntService.getTousLesEmprunts();
        ObservableList<Emprunt> data = FXCollections.observableArrayList(emprunts);
        tableViewEmprunts.setItems(data);
    }

    /**
     * Emprunte un livre.
     */
    @FXML
    public void handleEmprunter() {
        try {
            String livreStr = comboLivres.getValue();
            String membreStr = comboMembres.getValue();

            if (livreStr == null || membreStr == null) {
                afficherErreur("Erreur", "Veuillez sélectionner un livre et un membre");
                return;
            }

            String isbn = livreStr.split(" - ")[0];
            int membreId = Integer.parseInt(membreStr.split(" - ")[0]);

            java.time.LocalDate dateRetour = null;
            if (dpDateRetourPrevue != null) {
                dateRetour = dpDateRetourPrevue.getValue();
            }

            Emprunt emprunt;
            if (dateRetour != null) {
                emprunt = empruntService.emprunterLivre(isbn, membreId, dateRetour);
            } else {
                emprunt = empruntService.emprunterLivre(isbn, membreId);
            }
            afficherSucces("Succès", "Livre emprunté avec succès!");
            chargerEmprunts();
            chargerLivres();
        } catch (MembreInactifException | LivreIndisponibleException | LimiteEmpruntDepasseeException e) {
            afficherErreur("Erreur", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Retourne un livre emprunté.
     */
    @FXML
    public void handleRetourner() {
        Emprunt selected = tableViewEmprunts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un emprunt");
            return;
        }

        try {
            empruntService.retournerLivre(selected.getId());
            afficherSucces("Succès", "Livre retourné avec succès!");
            chargerEmprunts();
            chargerLivres();
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    // Affiche les emprunts en cours.
     
    @FXML
    public void handleAfficherEnCours() {
        try {
            var emprunts = empruntService.getEmpruntsEnCours();
            ObservableList<Emprunt> data = FXCollections.observableArrayList(emprunts);
            tableViewEmprunts.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    // Affiche les emprunts en retard.
     
    @FXML
    public void handleAfficherEnRetard() {
        try {
            var emprunts = empruntService.getEmpruntsEnRetard();
            ObservableList<Emprunt> data = FXCollections.observableArrayList(emprunts);
            tableViewEmprunts.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    // Affiche tous les emprunts.
    
    @FXML
    public void handleAfficherTous() {
        try {
            chargerEmprunts();
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    // Affiche une alerte d'erreur.
    
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Affiche une alerte de succès.
     
    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

