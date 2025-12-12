package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
<<<<<<< Updated upstream
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
=======
import java.util.prefs.Preferences;
import com.utils.NavigationManager;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private ComboBox<String> cmbCurrency;

    @FXML
    private ComboBox<String> cmbTheme;

    @FXML
    private CheckBox chkNotifications;

    @FXML
    private Button btnSaveSettings;

    private Preferences prefs;

    @FXML
    public void initialize() {
        prefs = Preferences.userNodeForPackage(SettingsController.class);

        // Initialize currency options
        cmbCurrency.getItems().addAll("USD ($)", "EUR (€)", "GBP (£)", "INR (₹)", "JPY (¥)", "BDT (৳)");

        // Load saved settings
        loadSettings();
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
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
=======
        // Load theme
        String theme = prefs.get("theme", "dark");
        cmbTheme.getItems().addAll("dark", "light");
        cmbTheme.setValue(theme);

        // Load notifications setting
        boolean notifications = prefs.getBoolean("notifications", true);
        chkNotifications.setSelected(notifications);
    }

    @FXML
    private void saveSettings() {
        try {
            // Save currency
            prefs.put("currency", cmbCurrency.getValue());

            // Save theme
            prefs.put("theme", cmbTheme.getValue());

            // Apply theme immediately
            applyTheme(cmbTheme.getValue());

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
>>>>>>> Stashed changes
        alert.setHeaderText(null);
        alert.setContentText("Settings saved successfully!");
        alert.showAndWait();
    }
<<<<<<< Updated upstream
=======

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

    private void applyTheme(String theme) {
        try {
            Stage stage = NavigationManager.getStage();
            if (stage == null) return;

            if (stage.getScene() == null) return;
            String base = getClass().getResource("/css/styles.css").toExternalForm();
            String lightCss = getClass().getResource("/css/light-theme.css").toExternalForm();

            // Ensure base dark stylesheet is present first
            if (!stage.getScene().getStylesheets().contains(base)) {
                stage.getScene().getStylesheets().add(0, base);
            }

            if ("light".equalsIgnoreCase(theme)) {
                if (!stage.getScene().getStylesheets().contains(lightCss)) {
                    stage.getScene().getStylesheets().add(lightCss);
                }
            } else {
                // dark: remove light-theme if present
                stage.getScene().getStylesheets().removeIf(s -> s.equals(lightCss));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
>>>>>>> Stashed changes
}