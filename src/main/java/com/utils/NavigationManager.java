package com.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationManager {

    private static Stage primaryStage;

    // Called from App.java so every screen can use the same stage
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    // Load an FXML and show it
    public static void navigateTo(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    NavigationManager.class.getResource("/fxml/" + fxmlName)
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Preserve CSS if it exists
            if (primaryStage.getScene() != null && !primaryStage.getScene().getStylesheets().isEmpty()) {
                scene.getStylesheets().addAll(primaryStage.getScene().getStylesheets());
            }

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load: " + fxmlName);
        }
    }
}