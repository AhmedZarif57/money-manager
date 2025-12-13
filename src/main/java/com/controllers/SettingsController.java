package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.prefs.Preferences;
import com.utils.NavigationManager;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private ComboBox<String> cmbCurrency;

    @FXML
    private ComboBox<String> cmbTheme;
    
    @FXML
    private ComboBox<String> cmbMonth;

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
        
        // Initialize month options
        cmbMonth.getItems().addAll("January", "February", "March", "April", "May", "June", 
                                    "July", "August", "September", "October", "November", "December");

        // Load saved settings
        loadSettings();
    }

    private void loadSettings() {
        // Load currency
        String currency = prefs.get("currency", "USD ($)");
        cmbCurrency.setValue(currency);

        // Load theme
        String theme = prefs.get("theme", "Dark");
        cmbTheme.getItems().addAll("Dark", "Light");
        cmbTheme.setValue(theme);
        
        // Load month
        String month = prefs.get("month", java.time.Month.of(java.time.LocalDate.now().getMonthValue()).name());
        String capitalizedMonth = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
        cmbMonth.setValue(capitalizedMonth);

        // Load notifications setting
        boolean notifications = prefs.getBoolean("notifications", true);
        chkNotifications.setSelected(notifications);
    }

    @FXML
    private void saveSettings() {
        try {
            // Get old and new currency
            String oldCurrency = prefs.get("currency", "USD ($)");
            String newCurrency = cmbCurrency.getValue();
            
            // Convert all amounts if currency changed
            if (!oldCurrency.equals(newCurrency)) {
                convertAllAmounts(oldCurrency, newCurrency);
            }
            
            // Save currency
            prefs.put("currency", newCurrency);

            // Save theme
            String selectedTheme = cmbTheme.getValue();
            
            // Check if Light theme is selected
            if ("Light".equalsIgnoreCase(selectedTheme)) {
                showAlert("Light Theme Under Development", 
                    "The Light theme is currently under construction. Please use the Dark theme for now.", 
                    Alert.AlertType.WARNING);
                cmbTheme.setValue("Dark");
                selectedTheme = "Dark";
            }
            
            prefs.put("theme", selectedTheme);

            // Apply theme immediately
            applyTheme(selectedTheme);
            
            // Save month
            prefs.put("month", cmbMonth.getValue());

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
        
        // Apply CSS to dialog
        alert.getDialogPane().getStylesheets().add(
            getClass().getResource("/css/styles.css").toExternalForm()
        );
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        
        // Set dialog icon after showing
        alert.setOnShown(e -> {
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/settings.png"));
                javafx.stage.Stage stage = (javafx.stage.Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(icon);
            } catch (Exception ex) {
                System.err.println("Could not load settings icon: " + ex.getMessage());
            }
        });
        
        alert.showAndWait();
    }
    
    @FXML
    private void changePassword() {
        com.utils.DataStore store = com.utils.DataStore.getInstance();
        com.models.User currentUser = store.getCurrentUser();
        
        if (currentUser == null) {
            showAlert("Error", "No user is logged in.", Alert.AlertType.ERROR);
            return;
        }
        
        // Create custom dialog for password change
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your current and new password");
        
        // Apply CSS to dialog
        dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/css/styles.css").toExternalForm()
        );
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        
        // Set dialog icon after showing
        dialog.setOnShown(e -> {
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/settings.png"));
                javafx.stage.Stage stage = (javafx.stage.Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(icon);
            } catch (Exception ex) {
                System.err.println("Could not load password change icon: " + ex.getMessage());
            }
        });
        
        ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);
        
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Current Password");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");
        
        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPasswordField, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPasswordField, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeButtonType) {
                return new String[]{currentPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText()};
            }
            return null;
        });
        
        java.util.Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(passwords -> {
            String currentPass = passwords[0];
            String newPass = passwords[1];
            String confirmPass = passwords[2];
            
            if (!currentUser.getPassword().equals(currentPass)) {
                showAlert("Error", "Current password is incorrect.", Alert.AlertType.ERROR);
                return;
            }
            
            if (newPass.length() < 6) {
                showAlert("Error", "New password must be at least 6 characters long.", Alert.AlertType.ERROR);
                return;
            }
            
            if (!newPass.equals(confirmPass)) {
                showAlert("Error", "New passwords do not match.", Alert.AlertType.ERROR);
                return;
            }
            
            if (store.changePassword(currentUser.getUsername(), newPass)) {
                showAlert("Success", "Password changed successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to change password.", Alert.AlertType.ERROR);
            }
        });
    }
    
    @FXML
    private void resetData() {
        com.utils.DataStore store = com.utils.DataStore.getInstance();
        com.models.User currentUser = store.getCurrentUser();
        
        if (currentUser == null) {
            showAlert("Error", "No user is logged in.", Alert.AlertType.ERROR);
            return;
        }
        
        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Reset Data");
        confirmAlert.setHeaderText("Are you sure you want to reset all data?");
        confirmAlert.setContentText("This will delete all transactions, budgets, and reset account balances to zero. This action cannot be undone.");
        
        // Apply CSS to dialog
        confirmAlert.getDialogPane().getStylesheets().add(
            getClass().getResource("/css/styles.css").toExternalForm()
        );
        confirmAlert.getDialogPane().getStyleClass().add("dialog-pane");
        
        // Set dialog icon after showing
        confirmAlert.setOnShown(e -> {
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/delete.png"));
                javafx.stage.Stage stage = (javafx.stage.Stage) confirmAlert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(icon);
            } catch (Exception ex) {
                System.err.println("Could not load reset data icon: " + ex.getMessage());
            }
        });
        
        java.util.Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Reset all accounts to 0
            for (com.models.Account account : store.getAccounts()) {
                if (account.getUserId().equals(currentUser.getId())) {
                    account.setBalance(0.0);
                }
            }
            
            // Delete all transactions
            store.getTransactions().removeIf(t -> t.getUserId().equals(currentUser.getId()));
            
            // Delete all budgets
            store.getBudgets().removeIf(b -> b.getUserId().equals(currentUser.getId()));
            
            // Save changes
            store.saveAccounts();
            store.saveTransactions();
            store.saveBudgets();
            
            showAlert("Data Reset", "All your data has been reset successfully.", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    private void deleteAccount() {
        com.utils.DataStore store = com.utils.DataStore.getInstance();
        com.models.User currentUser = store.getCurrentUser();
        
        if (currentUser == null) {
            showAlert("Error", "No user is logged in.", Alert.AlertType.ERROR);
            return;
        }
        
        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Account");
        confirmAlert.setHeaderText("Are you sure you want to delete your account?");
        confirmAlert.setContentText("This action cannot be undone. All your data will be permanently deleted.");
        
        // Apply CSS to dialog
        confirmAlert.getDialogPane().getStylesheets().add(
            getClass().getResource("/css/styles.css").toExternalForm()
        );
        confirmAlert.getDialogPane().getStyleClass().add("dialog-pane");
        
        // Set dialog icon after showing
        confirmAlert.setOnShown(e -> {
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/delete.png"));
                javafx.stage.Stage stage = (javafx.stage.Stage) confirmAlert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(icon);
            } catch (Exception ex) {
                System.err.println("Could not load delete account icon: " + ex.getMessage());
            }
        });
        
        java.util.Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (store.deleteUser(currentUser.getUsername())) {
                showAlert("Account Deleted", "Your account has been deleted successfully.", Alert.AlertType.INFORMATION);
                // Navigate back to login
                NavigationManager.navigateTo("login.fxml");
            } else {
                showAlert("Error", "Failed to delete account.", Alert.AlertType.ERROR);
            }
        }
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
    
    // Utility method to get selected month (1-12)
    public static int getSelectedMonth() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String month = prefs.get("month", java.time.Month.of(java.time.LocalDate.now().getMonthValue()).name());
        String capitalizedMonth = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
        
        // Convert month name to number
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(capitalizedMonth)) {
                return i + 1; // Return 1-12
            }
        }
        return java.time.LocalDate.now().getMonthValue(); // Default to current month
    }
    
    private void convertAllAmounts(String fromCurrency, String toCurrency) {
        double conversionRate = getConversionRate(fromCurrency, toCurrency);
        com.utils.DataStore store = com.utils.DataStore.getInstance();
        
        // Convert all account balances and round to 2 decimal places
        for (com.models.Account account : store.getAccounts()) {
            double newBalance = account.getBalance() * conversionRate;
            account.setBalance(Math.round(newBalance * 100.0) / 100.0);
        }
        
        // Convert all transactions and round to 2 decimal places
        for (com.models.Transaction transaction : store.getTransactions()) {
            double newAmount = transaction.getAmount() * conversionRate;
            transaction.setAmount(Math.round(newAmount * 100.0) / 100.0);
        }
        
        // Convert all budgets and round to 2 decimal places
        for (com.models.Budget budget : store.getBudgets()) {
            double newLimit = budget.getLimit() * conversionRate;
            budget.setLimit(Math.round(newLimit * 100.0) / 100.0);
        }
        
        // Save all changes
        store.saveAccounts();
        store.saveTransactions();
        store.saveBudgets();
    }
    
    private double getConversionRate(String fromCurrency, String toCurrency) {
        // Extract currency codes
        String fromCode = extractCurrencyCode(fromCurrency);
        String toCode = extractCurrencyCode(toCurrency);
        
        if (fromCode.equals(toCode)) return 1.0;
        
        // Conversion rates relative to USD (as of Dec 2025)
        java.util.Map<String, Double> toUSD = new java.util.HashMap<>();
        toUSD.put("USD", 1.0);
        toUSD.put("EUR", 1.05);     // 1 EUR = 1.05 USD
        toUSD.put("GBP", 1.27);     // 1 GBP = 1.27 USD
        toUSD.put("INR", 0.012);    // 1 INR = 0.012 USD
        toUSD.put("JPY", 0.0067);   // 1 JPY = 0.0067 USD
        toUSD.put("BDT", 0.0091);   // 1 BDT = 0.0091 USD (Taka)
        
        // Convert: from -> USD -> to
        double fromToUSD = toUSD.getOrDefault(fromCode, 1.0);
        double toToUSD = toUSD.getOrDefault(toCode, 1.0);
        
        return fromToUSD / toToUSD;
    }
    
    private String extractCurrencyCode(String currency) {
        // Extract code from format "USD ($)"
        if (currency.contains("USD")) return "USD";
        if (currency.contains("EUR")) return "EUR";
        if (currency.contains("GBP")) return "GBP";
        if (currency.contains("INR")) return "INR";
        if (currency.contains("JPY")) return "JPY";
        if (currency.contains("BDT")) return "BDT";
        return "USD"; // default
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

            if ("Light".equalsIgnoreCase(theme)) {
                if (!stage.getScene().getStylesheets().contains(lightCss)) {
                    stage.getScene().getStylesheets().add(lightCss);
                }
            } else {
                // Dark: remove light-theme if present
                stage.getScene().getStylesheets().removeIf(s -> s.equals(lightCss));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}