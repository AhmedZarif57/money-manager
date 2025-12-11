package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BudgetController {

    @FXML private TableView<Budget> budgetTable;
    @FXML private Button btnAddBudget;
    @FXML private ComboBox<String> filterCategory;
    @FXML private Button btnClearFilters;

    @FXML
    public void initialize() {
        // Initialize filter dropdown
        if (filterCategory != null) {
            filterCategory.getItems().addAll("All Categories", "Food", "Rent", "Transport", "Shopping", "Others");
            filterCategory.setValue("All Categories");
        }

        // Load sample data
        loadBudgets();
    }

    @FXML
    private void handleAddBudget() {
        System.out.println("Add Budget clicked - Feature coming soon!");
        showAlert("Add Budget", "This feature will allow you to create new budget categories.");
    }

    @FXML
    private void clearFilters() {
        if (filterCategory != null) {
            filterCategory.setValue("All Categories");
        }
        System.out.println("Budget filters cleared");
    }

    private void loadBudgets() {
        System.out.println("Loading budgets...");
        // Sample data would be loaded here
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Simple Budget class for table
    public static class Budget {
        private String category;
        private double allocated;
        private double spent;
        private double remaining;

        public Budget(String category, double allocated, double spent, double remaining) {
            this.category = category;
            this.allocated = allocated;
            this.spent = spent;
            this.remaining = remaining;
        }

        public String getCategory() { return category; }
        public double getAllocated() { return allocated; }
        public double getSpent() { return spent; }
        public double getRemaining() { return remaining; }
    }
}
