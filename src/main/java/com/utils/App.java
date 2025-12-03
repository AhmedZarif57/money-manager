package com.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Load the MAIN application layout
        // Example: dashboard.fxml (contains sidebar + main content)
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/dashboard.fxml")
        );

        Parent root = loader.load();

        // Scene + Apply CSS
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm()
        );

        // Stage settings
        stage.setTitle("Money Manager - Personal Finance Tracker");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(1100);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
