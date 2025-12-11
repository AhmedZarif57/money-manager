package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.prefs.Preferences;

public class SettingsController {

    @FXML
    private ComboBox<String> cmbCurrency;

    @FXML
    private CheckBox chkNotifications;

    @FXML
    private Button btnSaveSettings;

    private Preferences prefs;

    @FXML
    public void initialize() {
        prefs = Preferences.userNodeForPackage(SettingsController.class);

        // Initialize currency options
        cmbCurrency.getItems().addAll("USD ($)", "EUR (€)", "GBP (£)", "INR (₹)", "JPY (¥)");

        // Load saved settings
        loadSettings();
    }

    private void loadSettings() {
        // Load currency
        String currency = prefs.get("currency", "USD ($)");
        cmbCurrency.setValue(currency);

        // Load notifications setting
        boolean notifications = prefs.getBoolean("notifications", true);
        chkNotifications.setSelected(notifications);
    }

    @FXML
    private void saveSettings() {
        try {
            // Save currency
            prefs.put("currency", cmbCurrency.getValue());

            // Save notifications
            prefs.putBoolean("notifications", chkNotifications.isSelected());

            // Force save
            prefs.flush();

            // Show success message
            showAlert("Settings Saved", "Your preferences have been saved successfully.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Error", "Failed to save settings: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method to get current currency symbol
    public static String getCurrencySymbol() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String currency = prefs.get("currency", "USD ($)");

        // Extract symbol from the string (e.g., "USD ($)" -> "$")
        if (currency.contains("(") && currency.contains(")")) {
            int start = currency.indexOf("(") + 1;
            int end = currency.indexOf(")");
            return currency.substring(start, end);
        }
        return "$"; // Default to dollar
    }

    // Utility method to check if notifications are enabled
    public static boolean areNotificationsEnabled() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        return prefs.getBoolean("notifications", true);
    }
}