package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.utils.NavigationManager;

public class TransactionsController {
    
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;
    @FXML private Button btnAddTransaction, btnClearFilters;
    @FXML private TableView<?> transactionsTable;
    @FXML private ComboBox<String> filterType, filterAccount;
    @FXML private DatePicker filterDate;

    @FXML
    public void initialize() {
        setupNavigation();
        setupFilters();
        loadTransactions();
    }

    private void setupNavigation() {
        // Highlight active page
        setActiveButton(transactionsButton);
        
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

    private void setupFilters() {
        if (filterType != null) {
            filterType.getItems().addAll("All", "Income", "Expense");
            filterType.setValue("All");
        }
        
        if (filterAccount != null) {
            filterAccount.getItems().addAll("All Accounts", "Cash", "Bank", "Credit Card");
            filterAccount.setValue("All Accounts");
        }
    }

    @FXML
    private void handleAddTransaction() {
        System.out.println("Add Transaction clicked");
        // TODO: Open add transaction dialog
    }

    @FXML
    private void clearFilters() {
        if (filterType != null) filterType.setValue("All");
        if (filterAccount != null) filterAccount.setValue("All Accounts");
        if (filterDate != null) filterDate.setValue(null);
        loadTransactions();
    }

    private void loadTransactions() {
        System.out.println("Loading transactions...");
        // TODO: Load transactions from database
    }
}