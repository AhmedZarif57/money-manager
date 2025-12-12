package com.controllers;

import com.models.Transaction;
import com.utils.DataStore;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.collections.*;
import com.utils.NavigationManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsController {

    @FXML private PieChart expensePieChart;
    @FXML private LineChart<String, Number> expensesLineChart;
    @FXML private LineChart<String, Number> incomeLineChart;

    @FXML private DatePicker fromDatePicker, toDatePicker;
    @FXML private ComboBox<String> categoryCombo, accountCombo;
    @FXML private Button btnResetFilters;
    
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        setupNavigation();
        
        // Load dropdown values
        categoryCombo.getItems().addAll("Salary", "Rent", "Groceries", "Utilities", "Transport", "Entertainment");
        accountCombo.getItems().addAll("Main Account", "Card", "Bank", "Mobile Banking");

        // Load initial charts with real data
        loadRealData();

        setupFilterListeners();
    }

<<<<<<< Updated upstream
    private void setupNavigation() {
        // Highlight active page
        setActiveButton(analyticsButton);
        
        if (dashboardButton != null) dashboardButton.setOnAction(e -> NavigationManager.navigateTo("dashboard.fxml"));
        if (transactionsButton != null) transactionsButton.setOnAction(e -> NavigationManager.navigateTo("transactions.fxml"));
        if (analyticsButton != null) analyticsButton.setOnAction(e -> NavigationManager.navigateTo("analytics.fxml"));
        if (budgetButton != null) budgetButton.setOnAction(e -> NavigationManager.navigateTo("budget.fxml"));
        if (settingsButton != null) settingsButton.setOnAction(e -> NavigationManager.navigateTo("settings.fxml"));
        if (logoutButton != null) logoutButton.setOnAction(e -> NavigationManager.navigateTo("login.fxml"));
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
=======
    private void loadRealData() {
        // Pie chart: expense breakdown by category
        Map<String, Double> expensesByCategory = new HashMap<>();
        store.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.EXPENSE)
                .forEach(t -> expensesByCategory.merge(t.getTitle(), t.getAmount(), Double::sum));
        loadPieChartData(expensesByCategory);

        // Monthly expenses
        loadMonthlyExpenseSeries();

        // Monthly income
        loadMonthlyIncomeSeries();
>>>>>>> Stashed changes
    }

    /* ---------------- PIE CHART (1) ---------------- */
    private void loadPieChartData(Map<String, Double> data) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        data.forEach((category, amount) -> pieData.add(new PieChart.Data(category, amount)));
        expensePieChart.setData(pieData);
    }

    /* ---------------- MONTHLY EXPENSES (2) ---------------- */
    private void loadMonthlyExpenseSeries() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");

        java.time.YearMonth currentMonth = java.time.YearMonth.now();
        for (int i = 11; i >= 0; i--) {
            java.time.YearMonth month = currentMonth.minusMonths(i);
            double monthExpense = store.getTransactions().stream()
                    .filter(t -> java.time.YearMonth.from(t.getDate()).equals(month) && t.getType() == Transaction.Type.EXPENSE)
                    .mapToDouble(Transaction::getAmount).sum();
            String monthLabel = month.format(java.time.format.DateTimeFormatter.ofPattern("MMM"));
            series.getData().add(new XYChart.Data<>(monthLabel, monthExpense));
        }

        expensesLineChart.getData().clear();
        expensesLineChart.getData().add(series);
    }

    /* ---------------- MONTHLY INCOME (3) ---------------- */
    private void loadMonthlyIncomeSeries() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Income");

        java.time.YearMonth currentMonth = java.time.YearMonth.now();
        for (int i = 11; i >= 0; i--) {
            java.time.YearMonth month = currentMonth.minusMonths(i);
            double monthIncome = store.getTransactions().stream()
                    .filter(t -> java.time.YearMonth.from(t.getDate()).equals(month) && t.getType() == Transaction.Type.INCOME)
                    .mapToDouble(Transaction::getAmount).sum();
            String monthLabel = month.format(java.time.format.DateTimeFormatter.ofPattern("MMM"));
            series.getData().add(new XYChart.Data<>(monthLabel, monthIncome));
        }

        incomeLineChart.getData().clear();
        incomeLineChart.getData().add(series);
    }

    /* ---------------- FILTER SYSTEM (7) ---------------- */
    private void setupFilterListeners() {
        fromDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        toDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        accountCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        btnResetFilters.setOnAction(e -> {
            fromDatePicker.setValue(null);
            toDatePicker.setValue(null);
            categoryCombo.getSelectionModel().clearSelection();
            accountCombo.getSelectionModel().clearSelection();

            loadRealData();
        });
    }

    private void applyFilters() {
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();
        String category = categoryCombo.getValue();
        String account = accountCombo.getValue();

        System.out.println("Applying filters → From: " + from + " | To: " + to +
                " | Category: " + category + " | Account: " + account);

        // Reload with real filtered data
        loadRealData();
    }
}
