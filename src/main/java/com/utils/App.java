package com.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Load your FXML correctly
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/budget.fxml")
        );

        Parent root = fxmlLoader.load();

        // Apply CSS
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().addAll(
                getClass().getResource("/css/styles.css").toExternalForm()
        );

        stage.setTitle("Money Manager Preview");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
