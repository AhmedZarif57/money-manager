package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.utils.NavigationManager;

public class DashboardController {
    @FXML private Label welcomeLabel, totalBalanceLabel, totalIncomeLabel, totalExpensesLabel;
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;

    @FXML private void initialize() {
        updateDashboardData();
        setupNavigation();
    }

    private void updateDashboardData() {
        totalBalanceLabel.setText("$1,234.56");
        totalIncomeLabel.setText("$5,678.90");
        totalExpensesLabel.setText("$4,444.34");
        welcomeLabel.setText("Welcome back, User 👋");
    }

    private void setupNavigation() {
        // Highlight active page
        setActiveButton(dashboardButton);
        
        dashboardButton.setOnAction(e -> NavigationManager.navigateTo("dashboard.fxml"));
        transactionsButton.setOnAction(e -> NavigationManager.navigateTo("transactions.fxml"));
        analyticsButton.setOnAction(e -> NavigationManager.navigateTo("analytics.fxml"));
        budgetButton.setOnAction(e -> NavigationManager.navigateTo("budget.fxml"));
        settingsButton.setOnAction(e -> NavigationManager.navigateTo("settings.fxml"));
        logoutButton.setOnAction(e -> NavigationManager.navigateTo("login.fxml"));
    }
    
    private void setActiveButton(Button activeButton) {
        // Remove active style from all buttons
        dashboardButton.getStyleClass().remove("nav-button-active");
        transactionsButton.getStyleClass().remove("nav-button-active");
        analyticsButton.getStyleClass().remove("nav-button-active");
        budgetButton.getStyleClass().remove("nav-button-active");
        settingsButton.getStyleClass().remove("nav-button-active");
        
        // Add active style to current button
        if (activeButton != null && !activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }
}
