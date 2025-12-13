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

    public static Stage getStage() {
        return primaryStage;
    }

    // Load an FXML and show it
    public static void navigateTo(String fxmlName) {
        try {
            // Store current window dimensions
            double currentWidth = primaryStage.getWidth();
            double currentHeight = primaryStage.getHeight();
            double currentX = primaryStage.getX();
            double currentY = primaryStage.getY();
            
            // If navigating to dashboard, load MainLayout and then request it to load dashboard
            if (fxmlName.equals("dashboard.fxml") || fxmlName.equals("transactions.fxml") || 
                fxmlName.equals("analytics.fxml") || fxmlName.equals("budget.fxml") || fxmlName.equals("settings.fxml")) {
                
                Object mainController = null;
                // Load MainLayout as app shell if not already loaded or if coming from login
                if (primaryStage.getScene() == null || 
                    primaryStage.getScene().getRoot().getUserData() == null ||
                    !(primaryStage.getScene().getRoot().getUserData() instanceof com.controllers.MainLayoutController)) {
                    FXMLLoader mainLoader = new FXMLLoader(NavigationManager.class.getResource("/fxml/MainLayout.fxml"));
                    Parent mainRoot = mainLoader.load();
                    mainController = mainLoader.getController();
                    Scene mainScene = new Scene(mainRoot, currentWidth, currentHeight);
                    
                    // Store controller in root's userData immediately
                    mainRoot.setUserData(mainController);
                    
                    primaryStage.setScene(mainScene);
                    
                    // Restore window position
                    primaryStage.setX(currentX);
                    primaryStage.setY(currentY);
                }
                
                // Get the page name (e.g., "dashboard.fxml" → "dashboard")
                String pageName = fxmlName.replace(".fxml", "");
                
                // Tell MainLayoutController to load the page
                try {
                    if (mainController == null) {
                        // Try to get controller from scene's root userData
                        mainController = primaryStage.getScene().getRoot().getUserData();
                    }
                    if (mainController != null) {
                        mainController.getClass().getMethod("loadPage", String.class).invoke(mainController, pageName);
                    }
                } catch (Exception e) {
                    System.out.println("Could not invoke MainLayoutController.loadPage: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                // For login, signup, etc., load directly
                FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/fxml/" + fxmlName));
                Parent root = loader.load();
                Scene scene = new Scene(root, currentWidth, currentHeight);
                primaryStage.setScene(scene);
                
                // Restore window position
                primaryStage.setX(currentX);
                primaryStage.setY(currentY);
            }

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load: " + fxmlName);
        }
    }
}