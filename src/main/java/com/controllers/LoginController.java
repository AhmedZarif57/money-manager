package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.utils.NavigationManager;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label createAccountLabel; // NEW

    @FXML
    public void initialize() {

        loginButton.setOnAction(event -> handleLogin());

        // NEW: Click → Go to Signup Page
        createAccountLabel.setOnMouseClicked(event ->
                NavigationManager.navigateTo("signup.fxml")
        );
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill all fields.");
            return;
        }

        // In real project → verify user

        NavigationManager.navigateTo("dashboard.fxml");
    }
}
