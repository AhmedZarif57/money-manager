package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.collections.*;
import com.utils.NavigationManager;

import java.time.LocalDate;
import java.util.Map;

public class AnalyticsController {

    @FXML private PieChart expensePieChart;
    @FXML private LineChart<String, Number> expensesLineChart;
    @FXML private LineChart<String, Number> incomeLineChart;

    @FXML private DatePicker fromDatePicker, toDatePicker;
    @FXML private ComboBox<String> categoryCombo, accountCombo;
    @FXML private Button btnResetFilters;
    
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;

    @FXML
    public void initialize() {
        setupNavigation();
        
        // Load dropdown values
        categoryCombo.getItems().addAll("Food", "Rent", "Transport", "Shopping", "Others");
        accountCombo.getItems().addAll("Cash", "Card", "Bank", "Mobile Banking");

        // Load initial charts
        loadPieChartData(sampleExpenseCategoryData());
        loadMonthlyExpenseSeries();
        loadMonthlyIncomeSeries();

        setupFilterListeners();
    }

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
    }

    /* ---------------- PIE CHART (1) ---------------- */
    private void loadPieChartData(Map<String, Double> data) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        data.forEach((category, amount) -> pieData.add(new PieChart.Data(category, amount)));
        expensePieChart.setData(pieData);
    }

    private Map<String, Double> sampleExpenseCategoryData() {
        return Map.of(
                "Food", 2500.0,
                "Rent", 3000.0,
                "Transport", 900.0,
                "Shopping", 1800.0,
                "Others", 700.0
        );
    }

    /* ---------------- MONTHLY EXPENSES (2) ---------------- */
    private void loadMonthlyExpenseSeries() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        double[] data = {800,700,900,850,1000,1100,950,1200,900,880,1050,970};

        for (int i = 0; i < months.length; i++) {
            series.getData().add(new XYChart.Data<>(months[i], data[i]));
        }

        expensesLineChart.getData().setAll(series);
    }

    /* ---------------- MONTHLY INCOME (3) ---------------- */
    private void loadMonthlyIncomeSeries() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Income");

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        double[] data = {1200,1000,1300,1250,1400,1500,1450,1600,1380,1420,1500,1480};

        for (int i = 0; i < months.length; i++) {
            series.getData().add(new XYChart.Data<>(months[i], data[i]));
        }

        incomeLineChart.getData().setAll(series);
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

            // reload original charts
            loadPieChartData(sampleExpenseCategoryData());
            loadMonthlyExpenseSeries();
            loadMonthlyIncomeSeries();
        });
    }

    private void applyFilters() {
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();
        String category = categoryCombo.getValue();
        String account = accountCombo.getValue();

        System.out.println("Applying filters → From: " + from + " | To: " + to +
                " | Category: " + category + " | Account: " + account);

        // TODO: Replace with YOUR actual filtered database logic
        // currently it just reloads charts unchanged
        loadPieChartData(sampleExpenseCategoryData());
        loadMonthlyExpenseSeries();
        loadMonthlyIncomeSeries();
    }
}
