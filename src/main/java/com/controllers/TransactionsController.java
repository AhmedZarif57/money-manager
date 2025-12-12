package com.controllers;

<<<<<<< Updated upstream
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
=======
import com.models.Account;
import com.models.Transaction;
import com.utils.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class TransactionsController {

    @FXML private Button btnAddTransaction, btnClearFilters;
    @FXML private ComboBox<String> filterType, filterAccount;
    @FXML private DatePicker filterDate;

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> titleColumn, typeColumn, accountColumn, dateColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        // populate filters
        filterType.getItems().addAll("All", "INCOME", "EXPENSE");
        filterType.setValue("All");

        filterAccount.getItems().add("All");
        for (Account a : store.getAccounts()) filterAccount.getItems().add(a.getName());
        filterAccount.setValue("All");

        // table columns
        titleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));
        amountColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType().name()));
        dateColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDate().toString()));
        accountColumn.setCellValueFactory(cell -> {
            String id = cell.getValue().getAccountId();
            for (Account a : store.getAccounts()) if (a.getId().equals(id)) return new javafx.beans.property.SimpleStringProperty(a.getName());
            return new javafx.beans.property.SimpleStringProperty("-");
        });

        refreshTable();

        btnAddTransaction.setOnAction(e -> openAddDialog());
        btnClearFilters.setOnAction(e -> clearFilters());
    }

    private void refreshTable() {
        ObservableList<Transaction> list = FXCollections.observableArrayList(store.getTransactions());
        transactionsTable.setItems(list);
    }

    private void clearFilters() {
        filterType.setValue("All");
        filterAccount.setValue("All");
        filterDate.setValue(null);
        refreshTable();
    }

    private void openAddDialog() {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Title");
        ComboBox<String> cmbType = new ComboBox<>(); cmbType.getItems().addAll("INCOME","EXPENSE"); cmbType.setValue("EXPENSE");
        ComboBox<String> cmbAccount = new ComboBox<>(); for (Account a : store.getAccounts()) cmbAccount.getItems().add(a.getName()); if (!cmbAccount.getItems().isEmpty()) cmbAccount.setValue(cmbAccount.getItems().get(0));
        DatePicker dp = new DatePicker(LocalDate.now());
        TextField txtAmount = new TextField(); txtAmount.setPromptText("Amount");

        grid.add(new Label("Title"), 0, 0); grid.add(txtTitle, 1, 0);
        grid.add(new Label("Type"), 0, 1); grid.add(cmbType, 1, 1);
        grid.add(new Label("Account"), 0, 2); grid.add(cmbAccount, 1, 2);
        grid.add(new Label("Date"), 0, 3); grid.add(dp, 1, 3);
        grid.add(new Label("Amount"), 0, 4); grid.add(txtAmount, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(new Callback<ButtonType, Transaction>() {
            @Override
            public Transaction call(ButtonType param) {
                if (param == addButtonType) {
                    try {
                        String title = txtTitle.getText().isEmpty() ? "(untitled)" : txtTitle.getText();
                        double amount = Double.parseDouble(txtAmount.getText());
                        Transaction.Type type = "INCOME".equals(cmbType.getValue()) ? Transaction.Type.INCOME : Transaction.Type.EXPENSE;
                        String accountName = cmbAccount.getValue();
                        String accountId = store.getAccounts().stream().filter(a -> a.getName().equals(accountName)).findFirst().map(Account::getId).orElse(store.getAccounts().get(0).getId());
                        return new Transaction(UUID.randomUUID().toString(), title, dp.getValue(), amount, type, accountId);
                    } catch (Exception ex) {
                        return null;
                    }
                }
                return null;
            }
        });

        Optional<Transaction> result = dialog.showAndWait();
        result.ifPresent(t -> {
            store.addTransaction(t);
            refreshTable();
        });
    }
}
>>>>>>> Stashed changes
