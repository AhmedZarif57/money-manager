package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        dashboardButton.setOnAction(e -> System.out.println("Dashboard"));
        transactionsButton.setOnAction(e -> System.out.println("Transactions"));
        analyticsButton.setOnAction(e -> System.out.println("Analytics"));
        budgetButton.setOnAction(e -> System.out.println("Budget"));
        settingsButton.setOnAction(e -> System.out.println("Settings"));
        logoutButton.setOnAction(e -> System.out.println("Logout"));
    }
}
