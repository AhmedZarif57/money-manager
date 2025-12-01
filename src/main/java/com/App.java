package com.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file you want to preview
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/moneymanager/Dashboard.fxml"));
        Parent root = fxmlLoader.load();

        // Create scene and apply CSS files
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().addAll(
                getClass().getResource("/com/example/moneymanager/css/styles.css").toExternalForm(),
                getClass().getResource("/com/example/moneymanager/css/dashboard.css").toExternalForm()
        );

        // Set stage
        stage.setTitle("Money Manager Preview");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
