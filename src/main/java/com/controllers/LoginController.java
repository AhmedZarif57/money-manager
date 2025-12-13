package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.models.User;
import com.utils.DataStore;
import com.utils.NavigationManager;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField passwordTextField;
    
    @FXML
    private Button togglePasswordButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label createAccountLabel;
    
    private final DataStore dataStore = DataStore.getInstance();

    @FXML
    public void initialize() {
        // Bind the text fields together
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        
        // Toggle password visibility
        togglePasswordButton.setOnAction(event -> {
            boolean isPasswordVisible = passwordTextField.isVisible();
            passwordField.setVisible(isPasswordVisible);
            passwordField.setManaged(isPasswordVisible);
            passwordTextField.setVisible(!isPasswordVisible);
            passwordTextField.setManaged(!isPasswordVisible);
            togglePasswordButton.setText(isPasswordVisible ? "🙈" : "👁");
        });
        
        loginButton.setOnAction(event -> handleLogin());
        
        // Allow login with Enter key
        usernameField.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());
        passwordTextField.setOnAction(event -> handleLogin());

        // Click to go to Signup Page
        createAccountLabel.setOnMouseClicked(event ->
                NavigationManager.navigateTo("signup.fxml")
        );
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }

        // Authenticate user
        User user = dataStore.authenticateUser(username, password);
        
        if (user != null) {
            // Set current user
            dataStore.setCurrentUser(user);
            System.out.println("Login successful: " + user.getUsername());
            
            // Navigate to dashboard
            NavigationManager.navigateTo("dashboard.fxml");
        } else {
            showError("Invalid username or password.\n\nDemo credentials:\nUsername: demo\nPassword: demo123");
            usernameField.clear();
            passwordField.clear();
            usernameField.requestFocus();
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply dark theme to alert
        alert.getDialogPane().getStylesheets().add(
            getClass().getResource("/css/styles.css").toExternalForm()
        );
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        
        alert.showAndWait();
    }
}