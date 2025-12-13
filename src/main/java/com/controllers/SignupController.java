package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import com.utils.DataStore;
import com.utils.NavigationManager;

public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField passwordTextField;
    
    @FXML
    private Button togglePasswordButton;

    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField confirmPasswordTextField;
    
    @FXML
    private Button toggleConfirmPasswordButton;

    @FXML
    private Button signupButton;
    
    @FXML
    private Button backToLoginButton;
    
    private final DataStore dataStore = DataStore.getInstance();

    @FXML
    public void initialize() {
        // Bind the password text fields together
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        confirmPasswordTextField.textProperty().bindBidirectional(confirmPasswordField.textProperty());
        
        // Toggle password visibility
        togglePasswordButton.setOnAction(event -> {
            boolean isPasswordVisible = passwordTextField.isVisible();
            passwordField.setVisible(isPasswordVisible);
            passwordField.setManaged(isPasswordVisible);
            passwordTextField.setVisible(!isPasswordVisible);
            passwordTextField.setManaged(!isPasswordVisible);
            togglePasswordButton.setText(isPasswordVisible ? "🙈" : "👁");
        });
        
        // Toggle confirm password visibility
        toggleConfirmPasswordButton.setOnAction(event -> {
            boolean isConfirmVisible = confirmPasswordTextField.isVisible();
            confirmPasswordField.setVisible(isConfirmVisible);
            confirmPasswordField.setManaged(isConfirmVisible);
            confirmPasswordTextField.setVisible(!isConfirmVisible);
            confirmPasswordTextField.setManaged(!isConfirmVisible);
            toggleConfirmPasswordButton.setText(isConfirmVisible ? "🙈" : "👁");
        });
        
        signupButton.setOnAction(event -> handleSignup());
        backToLoginButton.setOnAction(event -> NavigationManager.navigateTo("login.fxml"));
        
        // Allow signup with Enter key
        confirmPasswordField.setOnAction(event -> handleSignup());
        confirmPasswordTextField.setOnAction(event -> handleSignup());
    }

    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }

        // Register user in database
        String email = username + "@moneymanager.com"; // Auto-generate email
        boolean success = dataStore.registerUser(username, email, password);
        
        if (success) {
            System.out.println("User registered successfully: " + username);
            // Navigate to the signup successful screen
            NavigationManager.navigateTo("signupsuccessful.fxml");
        } else {
            showError("Username already exists. Please choose a different username.");
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            usernameField.requestFocus();
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Signup Error");
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
