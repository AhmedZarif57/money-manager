package com.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Set main stage for NavigationManager
        NavigationManager.setStage(stage);

<<<<<<< Updated upstream
        // Load first screen (login page)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
=======
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
>>>>>>> Stashed changes

        stage.setScene(scene);
        stage.setTitle("Money Manager");
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.show();
<<<<<<< Updated upstream
=======

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
>>>>>>> Stashed changes
    }

    /**
     * Application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
