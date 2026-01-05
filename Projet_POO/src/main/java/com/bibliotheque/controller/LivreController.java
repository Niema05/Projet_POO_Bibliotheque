package com.bibliotheque.controller;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrôleur pour la gestion des livres.
 */
public class LivreController {

    @FXML
    private TableView<Livre> tableViewLivres;
    @FXML
    private TableColumn<Livre, String> colISBN;
    @FXML
    private TableColumn<Livre, String> colTitre;
    @FXML
    private TableColumn<Livre, String> colAuteur;
    @FXML
    private TableColumn<Livre, Integer> colAnnee;
    @FXML
    private TableColumn<Livre, Boolean> colDisponible;

    @FXML
    private TextField tfISBN;
    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfAuteur;
    @FXML
    private Spinner<Integer> spinnerAnnee;
    @FXML
    private CheckBox cbDisponible;

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

    /**
     * Initialise le contrôleur.
     */
    @FXML
    public void initialize() {
        // Configure les colonnes du TableView (cell factories explicites pour fiabilité)
        colISBN.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue() != null ? cell.getValue().getIsbn() : ""));
        colTitre.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue() != null ? cell.getValue().getTitre() : ""));
        colAuteur.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue() != null ? cell.getValue().getAuteur() : ""));
        colAnnee.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue() != null ? cell.getValue().getAnneePublication() : null));
        colDisponible.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue() != null ? cell.getValue().isDisponible() : null));
        
        // Ajoute un listener pour remplir le formulaire quand on sélectionne une ligne
        tableViewLivres.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                remplirFormulaire(newVal);
            }
        });
        
        // Permet la recherche en appuyant sur Enter
        tfRecherche.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleRechercher();
            }
        });
    }

    /**
     * Définit le service.
     *
     * @param service le service de bibliothèque
     */
    public void setService(BibliothequeService service) {
        System.out.println("DEBUG: setService() called in LivreController");
        this.service = service;
        // Charger les données APRÈS que le service soit initialisé
        if (service != null) {
            chargerLivres();
        } else {
            System.err.println("DEBUG: setService() called with null service!");
        }
    }

    /**
     * Refresh data from service (public entrypoint for external triggers).
     */
    public void refreshData() {
        chargerLivres();
    }

    /**
     * Charge tous les livres dans le tableau.
     */
    private void chargerLivres() {
        if (service == null) {
            System.err.println("DEBUG: chargerLivres() called but service is null!");
            return;
        }
        if (tableViewLivres == null) {
            System.err.println("DEBUG: chargerLivres() called but tableViewLivres is null!");
            return;
        }
        try {
            List<Livre> livres = service.getTousLesLivres();
            System.out.println("DEBUG: chargerLivres() retrieved " + livres.size() + " books from service");
            for (int i = 0; i < Math.min(5, livres.size()); i++) {
                System.out.println("DEBUG: livre[" + i + "] = " + livres.get(i));
            }
            ObservableList<Livre> data = FXCollections.observableArrayList(livres);
            // S'assurer que l'update UI se fasse sur le thread JavaFX
            javafx.application.Platform.runLater(() -> {
                tableViewLivres.setItems(data);
                System.out.println("DEBUG: chargerLivres() set " + data.size() + " books to tableView (UI thread)");
            });
        } catch (SQLException e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les livres : " + e.getMessage());
        }
    }

    /**
     * Ajoute un nouveau livre.
     */
    @FXML
    public void handleAjouter() {
        try {
            String isbn = tfISBN.getText();
            String titre = tfTitre.getText();
            String auteur = tfAuteur.getText();
            int annee = spinnerAnnee.getValue();
            boolean disponible = cbDisponible.isSelected();

            Livre livre = new Livre(isbn, titre, auteur, annee, disponible);
            service.ajouterLivre(livre);

            afficherSucces("Succès", "Livre ajouté avec succès!");
            nettoyerFormulaire();
            chargerLivres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Modifie le livre sélectionné.
     */
    @FXML
    public void handleModifier() {
        Livre selected = tableViewLivres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un livre");
            return;
        }

        try {
            selected.setTitre(tfTitre.getText());
            selected.setAuteur(tfAuteur.getText());
            selected.setAnneePublication(spinnerAnnee.getValue());
            selected.setDisponible(cbDisponible.isSelected());

            service.modifierLivre(selected);

            afficherSucces("Succès", "Livre modifié avec succès!");
            nettoyerFormulaire();
            chargerLivres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Supprime le livre sélectionné.
     */
    @FXML
    public void handleSupprimer() {
        Livre selected = tableViewLivres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un livre");
            return;
        }

        try {
            service.supprimerLivre(selected.getIsbn());
            afficherSucces("Succès", "Livre supprimé avec succès!");
            nettoyerFormulaire();
            chargerLivres();
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Recherche des livres.
     */
    @FXML
    public void handleRechercher() {
        String critere = tfRecherche.getText();
        if (critere.isEmpty()) {
            chargerLivres();
            return;
        }

        try {
            List<Livre> livres = service.rechercherLivres(critere);
            ObservableList<Livre> data = FXCollections.observableArrayList(livres);
            tableViewLivres.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }

    /**
     * Affiche les statistiques de la bibliothèque.
     */
    @FXML
    public void handleStatistiques() {
        try {
            String stats = service.getStatistiques();
            afficherSucces("Statistiques", stats);
        } catch (SQLException e) {
            afficherErreur("Erreur", "Impossible de charger les statistiques : " + e.getMessage());
        }
    }

    /**
     * Remplit le formulaire avec les données d'un livre sélectionné.
     */
    private void remplirFormulaire(Livre livre) {
        tfISBN.setText(livre.getIsbn());
        tfTitre.setText(livre.getTitre());
        tfAuteur.setText(livre.getAuteur());
        spinnerAnnee.getValueFactory().setValue(livre.getAnneePublication());
        cbDisponible.setSelected(livre.isDisponible());
    }

    /**
     * Nettoie le formulaire.
     */
    private void nettoyerFormulaire() {
        tfISBN.clear();
        tfTitre.clear();
        tfAuteur.clear();
        cbDisponible.setSelected(true);
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
