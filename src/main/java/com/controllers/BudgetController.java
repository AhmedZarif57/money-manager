package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.utils.NavigationManager;

public class BudgetController {
    
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;
    @FXML private Button btnAddBudget;
    @FXML private TableView<?> budgetTable;

    @FXML
    public void initialize() {
        setupNavigation();
        loadBudgets();
    }

    private void setupNavigation() {
        // Highlight active page
        setActiveButton(budgetButton);
        
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

    @FXML
    private void handleAddBudget() {
        System.out.println("Add Budget clicked");
        // TODO: Open add budget dialog
    }

    private void loadBudgets() {
        System.out.println("Loading budgets...");
        // TODO: Load budgets from database
    }
}