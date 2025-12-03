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
            
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load: " + fxmlName);
        }
    }
}
