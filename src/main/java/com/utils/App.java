package com.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load Roboto font
        try {
            Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
            if (font != null) {
                System.out.println("Roboto font loaded successfully from: /fonts/Roboto-Regular.ttf");
            } else {
                System.out.println("Roboto font could not be loaded. Using system fallback.");
            }
        } catch (Exception e) {
            System.err.println("Error loading Roboto font: " + e.getMessage());
            System.out.println("Using system fallback font.");
        }

        // Load application logo
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/Logo.png"));
            stage.getIcons().add(logo);
            System.out.println("Application logo loaded successfully");
        } catch (Exception e) {
            System.err.println("Could not load application logo: " + e.getMessage());
        }
        
        // Set main stage for NavigationManager
        NavigationManager.setStage(stage);

        // Load Login page as the application entry point
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(mainLoader.load(), 1200, 800);

        // Load CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            System.out.println("CSS loaded successfully from /css/styles.css");
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
            try {
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                System.out.println("CSS loaded from /styles.css as fallback");
            } catch (Exception e2) {
                System.err.println("Could not load CSS from any location");
            }
        }

        stage.setScene(scene);
        stage.setTitle("Smart Money Manager");
        stage.setResizable(true);
        stage.setMaximized(false);
        
        // Remember window size and position
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (stage.isShowing() && !stage.isMaximized()) {
                // Store width for next launch
            }
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (stage.isShowing() && !stage.isMaximized()) {
                // Store height for next launch
            }
        });
        
        stage.show();

        // If we have a MainLayoutController, load initial page
        try {
            Object controller = mainLoader.getController();
            if (controller != null) {
                try {
                    controller.getClass().getMethod("loadInitialPage", String.class).invoke(controller, "dashboard");
                } catch (NoSuchMethodException ignored) {
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}