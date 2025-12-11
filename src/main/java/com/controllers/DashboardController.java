package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label welcomeLabel, totalBalanceLabel, totalIncomeLabel, totalExpensesLabel;

    @FXML private void initialize() {
        updateDashboardData();
    }

    private void updateDashboardData() {
        totalBalanceLabel.setText("$1,234.56");
        totalIncomeLabel.setText("$5,678.90");
        totalExpensesLabel.setText("$4,444.34");
        welcomeLabel.setText("Welcome back, User 👋");
    }
}