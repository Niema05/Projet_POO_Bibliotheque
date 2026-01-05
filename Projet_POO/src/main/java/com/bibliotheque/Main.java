package com.bibliotheque;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Classe principale pour lancer l'application JavaFX.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        javafx.scene.layout.BorderPane root = loader.load();

        // Créer la scène
        Scene scene = new Scene(root, 1200, 700);

        // Configurer le stage
        primaryStage.setTitle("Système de Gestion de Bibliothèque");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Application démarrée avec succès!");
    }

    /**
     * Point d'entrée de l'application.
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}
