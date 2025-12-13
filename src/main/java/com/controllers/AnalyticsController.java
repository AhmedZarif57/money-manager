package com.controllers;

import com.models.Transaction;
import com.utils.DataStore;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.collections.*;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsController {

    @FXML private ToggleGroup typeToggle;
    @FXML private ToggleButton incomeRadio, expenseRadio;
    
    @FXML private PieChart categoryPieChart;
    @FXML private LineChart<String, Number> trendLineChart;
    @FXML private BarChart<String, Number> categoryBarChart;
    @FXML private Label percentageLabel;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        // Default to expense view
        expenseRadio.setSelected(true);
        
        // Load initial data
        loadExpenseData();
        
        // Setup listeners for toggle
        typeToggle.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == incomeRadio) {
                loadIncomeData();
            } else {
                loadExpenseData();
            }
        });
    }

    private void loadIncomeData() {
        // Get selected month
        int selectedMonth = SettingsController.getSelectedMonth();
        int currentYear = java.time.LocalDate.now().getYear();
        
        // Pie chart: income breakdown by category
        Map<String, Double> incomeByCategory = new HashMap<>();
        store.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.INCOME)
                .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear)
                .forEach(t -> incomeByCategory.merge(t.getTitle(), t.getAmount(), Double::sum));
        loadPieChartData(incomeByCategory);
        
        // Line chart: monthly income trend
        loadMonthlyTrend(Transaction.Type.INCOME);
        
        // Bar chart: income by category
        loadCategoryBarChart(incomeByCategory);
    }

    private void loadExpenseData() {
        // Get selected month
        int selectedMonth = SettingsController.getSelectedMonth();
        int currentYear = java.time.LocalDate.now().getYear();
        
        // Pie chart: expense breakdown by category
        Map<String, Double> expenseByCategory = new HashMap<>();
        store.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.EXPENSE)
                .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear)
                .forEach(t -> expenseByCategory.merge(t.getTitle(), t.getAmount(), Double::sum));
        loadPieChartData(expenseByCategory);
        
        // Line chart: monthly expense trend
        loadMonthlyTrend(Transaction.Type.EXPENSE);
        
        // Bar chart: expense by category
        loadCategoryBarChart(expenseByCategory);
    }

    private void loadPieChartData(Map<String, Double> data) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        double total = data.values().stream().mapToDouble(Double::doubleValue).sum();
        
        data.forEach((category, amount) -> pieData.add(new PieChart.Data(category, amount)));
        categoryPieChart.setData(pieData);
        
        // Calculate and display percentages
        if (total > 0) {
            StringBuilder percentages = new StringBuilder();
            data.forEach((category, amount) -> {
                double percentage = (amount / total) * 100;
                percentages.append(String.format("%s: %.1f%%\n", category, percentage));
            });
            percentageLabel.setText(percentages.toString().trim());
        } else {
            percentageLabel.setText("No data available");
        }
    }

    private void loadMonthlyTrend(Transaction.Type type) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type == Transaction.Type.INCOME ? "Income" : "Expenses");

        // Get selected month from settings
        int selectedMonth = SettingsController.getSelectedMonth();
        int currentYear = java.time.LocalDate.now().getYear();
        java.time.YearMonth yearMonth = java.time.YearMonth.of(currentYear, selectedMonth);
        int daysInMonth = yearMonth.lengthOfMonth();
        
        // Create a map to store daily totals
        java.util.Map<Integer, Double> dailyTotals = new java.util.HashMap<>();
        
        // Calculate daily totals for selected month
        store.getTransactions().stream()
            .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear && t.getType() == type)
            .forEach(t -> {
                int day = t.getDate().getDayOfMonth();
                dailyTotals.merge(day, t.getAmount(), Double::sum);
            });
        
        // Add data points for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            double dayTotal = dailyTotals.getOrDefault(day, 0.0);
            series.getData().add(new XYChart.Data<>(String.valueOf(day), dayTotal));
        }

        trendLineChart.getData().clear();
        trendLineChart.getData().add(series);
        
        // Apply color
        javafx.application.Platform.runLater(() -> {
            String color = type == Transaction.Type.INCOME ? "#FF9C00" : "#9C0001";
            series.getNode().setStyle("-fx-stroke: " + color + "; -fx-stroke-width: 3px;");
            series.getData().forEach(data -> {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-background-color: " + color + ", white;");
                }
            });
        });
    }

    private void loadCategoryBarChart(Map<String, Double> data) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Amount");

        data.forEach((category, amount) -> {
            series.getData().add(new XYChart.Data<>(category, amount));
        });

        categoryBarChart.getData().clear();
        categoryBarChart.getData().add(series);
        
        // Apply color
        javafx.application.Platform.runLater(() -> {
            String color = incomeRadio.isSelected() ? "#FF9C00" : "#9C0001";
            series.getData().forEach(chartData -> {
                chartData.getNode().setStyle("-fx-bar-fill: " + color + ";");
            });
        });
    }
}
