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

        // Load first screen (login page)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);

        stage.setScene(scene);
        stage.setTitle("Money Manager");
        stage.setResizable(true);
        stage.setMaximized(false);
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
