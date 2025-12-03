package com.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SignupSuccessfulController {

    @FXML
    private Button btnGoToLogin;

    @FXML
    public void initialize() {
        // debug help
        System.out.println("[SignupSuccessfulController] initialize() called");
        if (btnGoToLogin == null) {
            System.out.println("[SignupSuccessfulController] WARNING: btnGoToLogin is null (fx:id mismatch or FXML didn't load)");
            return;
        }

        btnGoToLogin.setOnAction(this::handleGoToLogin);
    }

    private void handleGoToLogin(ActionEvent event) {
        System.out.println("[SignupSuccessfulController] Go to Login button clicked");

        // First: try your NavigationManager if present
        try {
            Class<?> nmClass = Class.forName("com.utils.NavigationManager");
            // try calling navigateTo(String)
            try {
                nmClass.getMethod("navigateTo", String.class)
                        .invoke(null, "login.fxml");
                System.out.println("[SignupSuccessfulController] NavigationManager.navigateTo(\"login.fxml\") invoked");
                return;
            } catch (NoSuchMethodException nsme) {
                System.out.println("[SignupSuccessfulController] NavigationManager.navigateTo(String) not found; will fallback");
            }
        } catch (ClassNotFoundException cnfe) {
            System.out.println("[SignupSuccessfulController] No NavigationManager found in com.utils; falling back to manual load");
        } catch (Exception ex) {
            System.out.println("[SignupSuccessfulController] Error invoking NavigationManager: " + ex);
        }

        // Fallback: manually load login.fxml and replace scene on the current stage
        try {
            // Best-effort: locate the current Stage from the event source
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("[SignupSuccessfulController] Fallback navigation to /fxml/login.fxml succeeded");
        } catch (IOException ioe) {
            System.out.println("[SignupSuccessfulController] Failed to load login.fxml (IOException): " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (Exception e) {
            System.out.println("[SignupSuccessfulController] Unexpected error during fallback navigation: " + e);
            e.printStackTrace();
        }
    }
}
