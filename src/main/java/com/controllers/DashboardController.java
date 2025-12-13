package com.controllers;

import com.models.Transaction;
import com.utils.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML private Label welcomeLabel, totalBalanceLabel, totalIncomeLabel, totalExpensesLabel;

    @FXML private BarChart<String, Number> assetsChart;
    @FXML private TableView<Transaction> latestTransactionsTable;
    @FXML private TableColumn<Transaction, String> titleColumn, dateColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;

    private final DataStore store = DataStore.getInstance();

    @FXML private void initialize() {
        updateDashboardData();
        setupChart();
        setupTable();
    }

    private void updateDashboardData() {
        totalBalanceLabel.setText(String.format("%s%.2f", SettingsController.getCurrencySymbol(), store.getTotalBalance()));
        totalIncomeLabel.setText(String.format("%s%.2f", SettingsController.getCurrencySymbol(), store.getCurrentMonthIncome()));
        totalExpensesLabel.setText(String.format("%s%.2f", SettingsController.getCurrencySymbol(), store.getCurrentMonthExpenses()));
        
        // Show actual username
        String username = store.getCurrentUser() != null ? store.getCurrentUser().getUsername() : "User";
        welcomeLabel.setText("Welcome back, " + username + " 👋");
    }

    private void setupChart() {
        assetsChart.getData().clear();

        // Create monthly income vs expenses chart
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");

        // Get transactions for last 12 months and group by month
        String currentUserId = store.getCurrentUser() != null ? store.getCurrentUser().getId() : null;
        java.time.YearMonth currentMonth = java.time.YearMonth.now();
        for (int i = 11; i >= 0; i--) {
            java.time.YearMonth month = currentMonth.minusMonths(i);
            String monthLabel = month.format(java.time.format.DateTimeFormatter.ofPattern("MMM"));
            
            double monthIncome = store.getTransactions().stream()
                    .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUserId))
                    .filter(t -> java.time.YearMonth.from(t.getDate()).equals(month) && t.getType() == Transaction.Type.INCOME)
                    .mapToDouble(Transaction::getAmount).sum();
            double monthExpense = store.getTransactions().stream()
                    .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUserId))
                    .filter(t -> java.time.YearMonth.from(t.getDate()).equals(month) && t.getType() == Transaction.Type.EXPENSE)
                    .mapToDouble(Transaction::getAmount).sum();
            
            incomeSeries.getData().add(new XYChart.Data<>(monthLabel, monthIncome));
            expenseSeries.getData().add(new XYChart.Data<>(monthLabel, monthExpense));
        }

        assetsChart.getData().addAll(incomeSeries, expenseSeries);
        
        // Apply colors after the chart is displayed
        javafx.application.Platform.runLater(() -> {
            // Income series - orange (#FF9C00)
            incomeSeries.getData().forEach(data -> {
                data.getNode().setStyle("-fx-bar-fill: #FF9C00;");
            });
            // Expense series - red (#9C0001)
            expenseSeries.getData().forEach(data -> {
                data.getNode().setStyle("-fx-bar-fill: #9C0001;");
            });
        });
    }

    private void setupTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setStyle("-fx-alignment: CENTER;");
        // Color-code title based on type
        titleColumn.setCellFactory(col -> new javafx.scene.control.TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String title, boolean empty) {
                super.updateItem(title, empty);
                if (empty || title == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Transaction t = getTableView().getItems().get(getIndex());
                    setText(title);
                    if (t.getType() == Transaction.Type.INCOME) {
                        setStyle("-fx-text-fill: #FF9C00; -fx-alignment: CENTER;");
                    } else if (t.getType() == Transaction.Type.TRANSFER) {
                        setStyle("-fx-text-fill: #0066CC; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: #9C0001; -fx-alignment: CENTER;");
                    }
                }
            }
        });
        
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setStyle("-fx-alignment: CENTER;");
        // Color-code amounts based on type
        amountColumn.setCellFactory(col -> new javafx.scene.control.TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Transaction t = getTableView().getItems().get(getIndex());
                    setText(String.format("%.2f", amount));
                    if (t.getType() == Transaction.Type.INCOME) {
                        setStyle("-fx-text-fill: #FF9C00; -fx-alignment: CENTER;");
                    } else if (t.getType() == Transaction.Type.TRANSFER) {
                        setStyle("-fx-text-fill: #0066CC; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: #9C0001; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        dateColumn.setCellValueFactory(cellData -> {
            Transaction t = cellData.getValue();
            String formatted = t.getDate() != null ? t.getDate().format(DateTimeFormatter.ISO_DATE) : "";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });
        dateColumn.setStyle("-fx-alignment: CENTER;");
        // Color-code date based on type
        dateColumn.setCellFactory(col -> new javafx.scene.control.TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Transaction t = getTableView().getItems().get(getIndex());
                    setText(date);
                    if (t.getType() == Transaction.Type.INCOME) {
                        setStyle("-fx-text-fill: #FF9C00; -fx-alignment: CENTER;");
                    } else if (t.getType() == Transaction.Type.TRANSFER) {
                        setStyle("-fx-text-fill: #0066CC; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: #9C0001; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        // Filter transactions by selected month and current user
        String currentUserId = store.getCurrentUser() != null ? store.getCurrentUser().getId() : null;
        int selectedMonth = SettingsController.getSelectedMonth();
        int currentYear = java.time.LocalDate.now().getYear();
        ObservableList<Transaction> list = FXCollections.observableArrayList(
            store.getTransactions().stream()
                .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUserId))
                .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear)
                .collect(java.util.stream.Collectors.toList())
        );
        latestTransactionsTable.setItems(list);
    }
}