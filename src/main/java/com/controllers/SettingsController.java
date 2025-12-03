package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.utils.NavigationManager;

public class SettingsController {
    
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;
    @FXML private Button btnSaveSettings;
    @FXML private ComboBox<String> cmbCurrency, cmbTheme;
    @FXML private CheckBox chkNotifications;

    @FXML
    public void initialize() {
        setupNavigation();
        setupSettings();
    }

    private void setupNavigation() {
        // Highlight active page
        setActiveButton(settingsButton);
        
        dashboardButton.setOnAction(e -> NavigationManager.navigateTo("dashboard.fxml"));
        transactionsButton.setOnAction(e -> NavigationManager.navigateTo("transactions.fxml"));
        analyticsButton.setOnAction(e -> NavigationManager.navigateTo("analytics.fxml"));
        budgetButton.setOnAction(e -> NavigationManager.navigateTo("budget.fxml"));
        settingsButton.setOnAction(e -> NavigationManager.navigateTo("settings.fxml"));
        logoutButton.setOnAction(e -> NavigationManager.navigateTo("login.fxml"));
    }
    
    private void setActiveButton(Button activeButton) {
        if (dashboardButton != null) dashboardButton.getStyleClass().remove("nav-button-active");
        if (transactionsButton != null) transactionsButton.getStyleClass().remove("nav-button-active");
        if (analyticsButton != null) analyticsButton.getStyleClass().remove("nav-button-active");
        if (budgetButton != null) budgetButton.getStyleClass().remove("nav-button-active");
        if (settingsButton != null) settingsButton.getStyleClass().remove("nav-button-active");
        
        if (activeButton != null && !activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }

    private void setupSettings() {
        if (cmbCurrency != null) {
            cmbCurrency.getItems().addAll("USD ($)", "EUR (€)", "GBP (£)", "BDT (৳)", "INR (₹)");
            cmbCurrency.setValue("USD ($)");
        }
        
        if (cmbTheme != null) {
            cmbTheme.getItems().addAll("Light", "Dark", "Auto");
            cmbTheme.setValue("Light");
        }
        
        if (chkNotifications != null) {
            chkNotifications.setSelected(true);
        }
    }

    @FXML
    private void handleSaveSettings() {
        System.out.println("Settings saved!");
        System.out.println("Currency: " + (cmbCurrency != null ? cmbCurrency.getValue() : "N/A"));
        System.out.println("Theme: " + (cmbTheme != null ? cmbTheme.getValue() : "N/A"));
        System.out.println("Notifications: " + (chkNotifications != null ? chkNotifications.isSelected() : "N/A"));
        // TODO: Save settings to database or preferences
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Settings saved successfully!");
        alert.showAndWait();
    }
}