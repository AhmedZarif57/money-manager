package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TransactionsController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private Button btnAddTransaction;
    @FXML private Button btnClearFilters;
    @FXML private ComboBox<String> filterType;
    @FXML private ComboBox<String> filterAccount;
    @FXML private DatePicker filterDate;

    @FXML
    public void initialize() {
        // Initialize filter dropdowns
        if (filterType != null) {
            filterType.getItems().addAll("All", "Income", "Expense");
            filterType.setValue("All");
        }
        
        if (filterAccount != null) {
            filterAccount.getItems().addAll("All Accounts", "Cash", "Card", "Bank", "Mobile Banking");
            filterAccount.setValue("All Accounts");
        }

        // Load sample data
        loadTransactions();
    }

    @FXML
    private void handleAddTransaction() {
        System.out.println("Add Transaction clicked - Feature coming soon!");
        showAlert("Add Transaction", "This feature will allow you to add new transactions.");
    }

    @FXML
    private void clearFilters() {
        if (filterType != null) filterType.setValue("All");
        if (filterAccount != null) filterAccount.setValue("All Accounts");
        if (filterDate != null) filterDate.setValue(null);
        System.out.println("Filters cleared");
    }

    private void loadTransactions() {
        System.out.println("Loading transactions...");
        // Sample data would be loaded here
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Simple Transaction class for table
    public static class Transaction {
        private String date;
        private String category;
        private String type;
        private String account;
        private double amount;

        public Transaction(String date, String category, String type, String account, double amount) {
            this.date = date;
            this.category = category;
            this.type = type;
            this.account = account;
            this.amount = amount;
        }

        public String getDate() { return date; }
        public String getCategory() { return category; }
        public String getType() { return type; }
        public String getAccount() { return account; }
        public double getAmount() { return amount; }
    }
}
