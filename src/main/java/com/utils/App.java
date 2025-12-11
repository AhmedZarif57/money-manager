package com.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load Montserrat font
        try {
            String[] fontPaths = {
                    "/fonts/Montserrat-Regular.ttf",
                    "/css/fonts/Montserrat-Regular.ttf",
                    "/fxml/fonts/Montserrat-Regular.ttf",
                    "fonts/Montserrat-Regular.ttf"
            };

            boolean fontLoaded = false;
            for (String fontPath : fontPaths) {
                try {
                    Font font = Font.loadFont(getClass().getResourceAsStream(fontPath), 14);
                    if (font != null) {
                        System.out.println("Montserrat font loaded from: " + fontPath);
                        fontLoaded = true;
                        break;
                    }
                } catch (Exception e) {
                    // Try next path
                }
            }

            if (!fontLoaded) {
                System.out.println("Montserrat font not found in resources. Using system fallback.");
            }
        } catch (Exception e) {
            System.err.println("Error loading Montserrat font: " + e.getMessage());
        }

        // Set main stage for NavigationManager
        NavigationManager.setStage(stage);

        // Start with Login page
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(mainLoader.load(), 1200, 800);

        // Get the MainLayoutController (NOT DashboardController!)
        // Remove this line or fix the casting issue

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
        stage.setTitle("Money Manager");
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.show();

        // Optional: If you need to access MainLayoutController later
        // MainLayoutController mainController = mainLoader.getController();
        // mainController.loadInitialPage("dashboard");
    }

    /**
     * Application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}