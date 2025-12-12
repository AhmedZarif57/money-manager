package com.controllers;

import com.models.Transaction;
import com.utils.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
<<<<<<< Updated upstream
import javafx.scene.control.*;
import com.utils.NavigationManager;
=======
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
>>>>>>> Stashed changes

public class DashboardController {
    @FXML private Label welcomeLabel, totalBalanceLabel, totalIncomeLabel, totalExpensesLabel;
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;

    @FXML private BarChart<String, Number> assetsChart;
    @FXML private TableView<Transaction> latestTransactionsTable;
    @FXML private TableColumn<Transaction, String> titleColumn, dateColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;

    private final DataStore store = DataStore.getInstance();

    @FXML private void initialize() {
        updateDashboardData();
<<<<<<< Updated upstream
        setupNavigation();
=======
        setupChart();
        setupTable();
>>>>>>> Stashed changes
    }

    private void updateDashboardData() {
        totalBalanceLabel.setText(String.format("%s%.2f", SettingsController.getCurrencySymbol(), store.getTotalBalance()));
        totalIncomeLabel.setText(String.format("%s%.2f", SettingsController.getCurrencySymbol(), store.getTotalIncome()));
        totalExpensesLabel.setText(String.format("%s%.2f", SettingsController.getCurrencySymbol(), store.getTotalExpenses()));
        welcomeLabel.setText("Welcome back, User 👋");
    }

<<<<<<< Updated upstream
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
=======
    private void setupChart() {
        assetsChart.getData().clear();

        // Create monthly income vs expenses chart
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");

        // Get transactions for last 12 months and group by month
        java.time.YearMonth currentMonth = java.time.YearMonth.now();
        for (int i = 11; i >= 0; i--) {
            java.time.YearMonth month = currentMonth.minusMonths(i);
            String monthLabel = month.format(java.time.format.DateTimeFormatter.ofPattern("MMM"));
            
            double monthIncome = store.getTransactions().stream()
                    .filter(t -> java.time.YearMonth.from(t.getDate()).equals(month) && t.getType() == Transaction.Type.INCOME)
                    .mapToDouble(Transaction::getAmount).sum();
            double monthExpense = store.getTransactions().stream()
                    .filter(t -> java.time.YearMonth.from(t.getDate()).equals(month) && t.getType() == Transaction.Type.EXPENSE)
                    .mapToDouble(Transaction::getAmount).sum();
            
            incomeSeries.getData().add(new XYChart.Data<>(monthLabel, monthIncome));
            expenseSeries.getData().add(new XYChart.Data<>(monthLabel, monthExpense));
        }

        assetsChart.getData().addAll(incomeSeries, expenseSeries);
    }

    private void setupTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        dateColumn.setCellValueFactory(cellData -> {
            Transaction t = cellData.getValue();
            String formatted = t.getDate() != null ? t.getDate().format(DateTimeFormatter.ISO_DATE) : "";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });

        ObservableList<Transaction> list = FXCollections.observableArrayList(store.getTransactions());
        latestTransactionsTable.setItems(list);
    }
}
>>>>>>> Stashed changes
