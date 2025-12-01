package com.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Load the main FXML view
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/dashboard.fxml")
        );

        Parent root = fxmlLoader.load();

        // Create scene and apply custom CSS styling
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().addAll(
                getClass().getResource("/css/styles.css").toExternalForm()
        );

        // Configure and display the main window
        stage.setTitle("Money Manager - Personal Finance Tracker");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    /**
     * Application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
