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

<<<<<<< Updated upstream
    // Load an FXML and show it while preserving window state
    public static void navigateTo(String fxmlName) {
        try {
            // Store current window state
            boolean wasMaximized = primaryStage.isMaximized();
            double width = primaryStage.getWidth();
            double height = primaryStage.getHeight();
            double x = primaryStage.getX();
            double y = primaryStage.getY();
            
            FXMLLoader loader = new FXMLLoader(
                    NavigationManager.class.getResource("/fxml/" + fxmlName)
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            
            // Restore window state
            if (!wasMaximized) {
                primaryStage.setWidth(width);
                primaryStage.setHeight(height);
                primaryStage.setX(x);
                primaryStage.setY(y);
            }
            primaryStage.setMaximized(wasMaximized);
            
=======
    public static Stage getStage() {
        return primaryStage;
    }

    // Load an FXML and show it
    public static void navigateTo(String fxmlName) {
        try {
            // If navigating to dashboard, load MainLayout and then request it to load dashboard
            if (fxmlName.equals("dashboard.fxml") || fxmlName.equals("transactions.fxml") || 
                fxmlName.equals("analytics.fxml") || fxmlName.equals("budget.fxml") || fxmlName.equals("settings.fxml")) {
                
                // Load MainLayout as app shell if not already loaded
                if (primaryStage.getScene() == null || 
                    !primaryStage.getScene().getRoot().getStyleClass().contains("main-layout-root")) {
                    FXMLLoader mainLoader = new FXMLLoader(NavigationManager.class.getResource("/fxml/MainLayout.fxml"));
                    Parent mainRoot = mainLoader.load();
                    Scene mainScene = new Scene(mainRoot);
                    primaryStage.setScene(mainScene);
                }
                
                // Get the page name (e.g., "dashboard.fxml" → "dashboard")
                String pageName = fxmlName.replace(".fxml", "");
                
                // Tell MainLayoutController to load the page
                try {
                    Object mainController = primaryStage.getScene().getRoot().getUserData();
                    if (mainController == null) {
                        // Try to get from FXMLLoader context
                        FXMLLoader mainLoader = new FXMLLoader(NavigationManager.class.getResource("/fxml/MainLayout.fxml"));
                        mainLoader.load();
                        mainController = mainLoader.getController();
                    }
                    if (mainController != null) {
                        mainController.getClass().getMethod("loadPage", String.class).invoke(mainController, pageName);
                    }
                } catch (Exception e) {
                    System.out.println("Could not invoke MainLayoutController.loadPage: " + e.getMessage());
                }
            } else {
                // For login, signup, etc., load directly
                FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/fxml/" + fxmlName));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
            }

>>>>>>> Stashed changes
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load: " + fxmlName);
        }
    }
}
