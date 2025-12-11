package com.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.controllers.MainLayoutController;

public class NavigationManager {

    private static Stage primaryStage;

    // Called from App.java so every screen can use the same stage
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    // Load an FXML and show it with window state preservation
    public static void navigateTo(String fxmlName) {
        try {
            // Preserve window state
            boolean wasMaximized = primaryStage.isMaximized();
            double width = primaryStage.getWidth();
            double height = primaryStage.getHeight();
            double x = primaryStage.getX();
            double y = primaryStage.getY();

            FXMLLoader loader = new FXMLLoader(
                    NavigationManager.class.getResource("/fxml/" + fxmlName)
            );
            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);

            // Preserve CSS if it exists
            if (primaryStage.getScene() != null && !primaryStage.getScene().getStylesheets().isEmpty()) {
                scene.getStylesheets().addAll(primaryStage.getScene().getStylesheets());
            }

            primaryStage.setScene(scene);
            
            // Restore window state
            primaryStage.setX(x);
            primaryStage.setY(y);
            primaryStage.setMaximized(wasMaximized);
            
            primaryStage.show();

            // If loading MainLayout, load dashboard as initial page
            if (fxmlName.equals("MainLayout.fxml")) {
                MainLayoutController controller = loader.getController();
                if (controller != null) {
                    controller.loadInitialPage("dashboard");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load: " + fxmlName);
        }
    }
}