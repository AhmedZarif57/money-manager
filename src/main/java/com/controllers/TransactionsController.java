package com.controllers;

import com.models.Account;
import com.models.Transaction;
import com.utils.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionsController {

    @FXML private Button btnAddTransaction, btnClearFilters;
    @FXML private ComboBox<String> filterType, filterAccount;
    @FXML private DatePicker filterDate;

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> titleColumn, typeColumn, accountColumn, dateColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, Void> actionColumn;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        // populate filters
        filterType.getItems().addAll("All", "INCOME", "EXPENSE", "TRANSFER");
        filterType.setValue("All");

        filterAccount.getItems().add("All");
        // Add only distinct account types (Wallet, Bkash, Card, Bank)
        java.util.Set<String> distinctAccounts = new java.util.HashSet<>();
        for (Account a : store.getAccounts()) {
            distinctAccounts.add(a.getName());
        }
        filterAccount.getItems().addAll(distinctAccounts);
        filterAccount.setValue("All");

        // table columns
        titleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));
        titleColumn.setStyle("-fx-alignment: CENTER;");
        
        amountColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("amount"));
        amountColumn.setStyle("-fx-alignment: CENTER;");
        // Color-code amounts
        amountColumn.setCellFactory(col -> new javafx.scene.control.TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                    setStyle("-fx-alignment: CENTER;");
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
        
        typeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType().name()));
        typeColumn.setStyle("-fx-alignment: CENTER;");
        
        dateColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDate().toString()));
        dateColumn.setStyle("-fx-alignment: CENTER;");
        accountColumn.setCellValueFactory(cell -> {
            String id = cell.getValue().getAccountId();
            for (Account a : store.getAccounts()) if (a.getId().equals(id)) return new javafx.beans.property.SimpleStringProperty(a.getName());
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        accountColumn.setStyle("-fx-alignment: CENTER;");
        
        // Setup action column with edit and delete buttons
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);
            
            {
                editBtn.setStyle("-fx-background-color: #FF6B35; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px; -fx-padding: 4 10;");
                deleteBtn.setStyle("-fx-background-color: #9C0001; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px; -fx-padding: 4 10;");
                buttons.setAlignment(javafx.geometry.Pos.CENTER);
                
                editBtn.setOnAction(e -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    openEditDialog(transaction);
                });
                
                deleteBtn.setOnAction(e -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmDialog.setTitle("Delete Transaction");
                    confirmDialog.setHeaderText("Are you sure you want to delete this transaction?");
                    confirmDialog.setContentText(transaction.getTitle() + " - " + String.format("%.2f", transaction.getAmount()));
                    confirmDialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                    confirmDialog.getDialogPane().getStyleClass().add("dialog-pane");
                    
                    // Set dialog icon
                    confirmDialog.setOnShown(event -> {
                        try {
                            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/delete.png"));
                            javafx.stage.Stage stage = (javafx.stage.Stage) confirmDialog.getDialogPane().getScene().getWindow();
                            stage.getIcons().add(icon);
                        } catch (Exception ex) {
                            System.err.println("Could not load delete icon: " + ex.getMessage());
                        }
                    });
                    
                    Optional<ButtonType> result = confirmDialog.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        store.deleteTransaction(transaction);
                        refreshTable();
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });

        refreshTable();

        btnAddTransaction.setOnAction(e -> openAddDialog());
        btnClearFilters.setOnAction(e -> clearFilters());
        
        // Add listeners for filter changes
        filterType.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterAccount.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterDate.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void refreshTable() {
        // Apply month filter by default
        applyFilters();
    }
    
    private void applyFilters() {
        ObservableList<Transaction> allTransactions = FXCollections.observableArrayList(store.getTransactions());
        ObservableList<Transaction> filtered = FXCollections.observableArrayList();
        
        // Get selected month from settings
        int selectedMonth = SettingsController.getSelectedMonth();
        int currentYear = java.time.LocalDate.now().getYear();
        
        for (Transaction t : allTransactions) {
            boolean matches = true;
            
            // Filter by selected month and current year
            if (t.getDate().getMonthValue() != selectedMonth || t.getDate().getYear() != currentYear) {
                matches = false;
            }
            
            // Filter by type
            if (filterType.getValue() != null && !filterType.getValue().equals("All")) {
                if (!t.getType().name().equals(filterType.getValue())) {
                    matches = false;
                }
            }
            
            // Filter by account
            if (filterAccount.getValue() != null && !filterAccount.getValue().equals("All")) {
                String accountName = "";
                for (Account a : store.getAccounts()) {
                    if (a.getId().equals(t.getAccountId())) {
                        accountName = a.getName();
                        break;
                    }
                }
                if (!accountName.equals(filterAccount.getValue())) {
                    matches = false;
                }
            }
            
            // Filter by date (specific date within the month)
            if (filterDate.getValue() != null) {
                if (!t.getDate().equals(filterDate.getValue())) {
                    matches = false;
                }
            }
            
            if (matches) {
                filtered.add(t);
            }
        }
        
        transactionsTable.setItems(filtered);
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
        
        // Apply CSS to dialog
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        
        // Set dialog icon after showing
        dialog.setOnShown(e -> {
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/add.png"));
                javafx.stage.Stage stage = (javafx.stage.Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(icon);
            } catch (Exception ex) {
                System.err.println("Could not load add transaction icon: " + ex.getMessage());
            }
        });

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Date picker (default to today)
        DatePicker dp = new DatePicker(LocalDate.now());
        
        // Type ComboBox (Income/Expense/Transfer)
        ComboBox<String> cmbType = new ComboBox<>();
        cmbType.getItems().addAll("INCOME", "EXPENSE", "TRANSFER");
        cmbType.setValue("EXPENSE");
        
        // Category ComboBox (depends on type)
        ComboBox<String> cmbCategory = new ComboBox<>();
        cmbCategory.setPromptText("Select Category");
        cmbCategory.setPrefWidth(150);
        
        // Add Category button
        Button btnAddCategory = new Button("+");
        btnAddCategory.setStyle("-fx-background-color: #FF6B35; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 5 12; -fx-cursor: hand;");
        btnAddCategory.setOnAction(e -> {
            TextInputDialog catDialog = new TextInputDialog();
            catDialog.setTitle("Add Category");
            catDialog.setHeaderText("Add New Category");
            catDialog.setContentText("Category Name:");
            catDialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            catDialog.getDialogPane().getStyleClass().add("dialog-pane");
            
            Optional<String> result = catDialog.showAndWait();
            result.ifPresent(catName -> {
                if (!catName.trim().isEmpty()) {
                    String type = cmbType.getValue();
                    store.addCategory(catName.trim(), type);
                    cmbCategory.getItems().add(catName.trim());
                    cmbCategory.setValue(catName.trim());
                }
            });
        });
        
        HBox categoryBox = new HBox(5, cmbCategory, btnAddCategory);
        
        // Account ComboBox (for INCOME/EXPENSE)
        ComboBox<String> cmbAccount = new ComboBox<>();
        for (Account a : store.getAccounts()) {
            cmbAccount.getItems().add(a.getName());
        }
        if (!cmbAccount.getItems().isEmpty()) {
            cmbAccount.setValue(cmbAccount.getItems().get(0));
        }
        
        // From Account ComboBox (for TRANSFER)
        ComboBox<String> cmbFromAccount = new ComboBox<>();
        for (Account a : store.getAccounts()) {
            cmbFromAccount.getItems().add(a.getName());
        }
        if (!cmbFromAccount.getItems().isEmpty()) {
            cmbFromAccount.setValue(cmbFromAccount.getItems().get(0));
        }
        
        // To Account ComboBox (for TRANSFER)
        ComboBox<String> cmbToAccount = new ComboBox<>();
        for (Account a : store.getAccounts()) {
            cmbToAccount.getItems().add(a.getName());
        }
        if (cmbToAccount.getItems().size() > 1) {
            cmbToAccount.setValue(cmbToAccount.getItems().get(1));
        } else if (!cmbToAccount.getItems().isEmpty()) {
            cmbToAccount.setValue(cmbToAccount.getItems().get(0));
        }
        
        // Labels for dynamic fields
        Label lblCategoryOrFrom = new Label("Category");
        Label lblAccountOrTo = new Label("Account");
        
        // Update form based on type selection
        Runnable updateFormFields = () -> {
            String type = cmbType.getValue();
            
            if ("TRANSFER".equals(type)) {
                // Show From/To fields, hide Category
                lblCategoryOrFrom.setText("From Account");
                lblAccountOrTo.setText("To Account");
                grid.getChildren().removeAll(categoryBox, cmbAccount);
                if (!grid.getChildren().contains(cmbFromAccount)) {
                    grid.add(cmbFromAccount, 1, 2);
                }
                if (!grid.getChildren().contains(cmbToAccount)) {
                    grid.add(cmbToAccount, 1, 3);
                }
            } else {
                // Show Category/Account fields, hide From/To
                lblCategoryOrFrom.setText("Category");
                lblAccountOrTo.setText("Account");
                grid.getChildren().removeAll(cmbFromAccount, cmbToAccount);
                if (!grid.getChildren().contains(categoryBox)) {
                    grid.add(categoryBox, 1, 2);
                }
                if (!grid.getChildren().contains(cmbAccount)) {
                    grid.add(cmbAccount, 1, 3);
                }
                
                // Update categories
                cmbCategory.getItems().clear();
                List<String> categories = store.getCategoriesByType(type);
                cmbCategory.getItems().addAll(categories);
                if (!cmbCategory.getItems().isEmpty()) {
                    cmbCategory.setValue(cmbCategory.getItems().get(0));
                }
            }
        };
        
        // Listen for type changes
        cmbType.valueProperty().addListener((obs, oldVal, newVal) -> updateFormFields.run());
        
        // Amount field
        TextField txtAmount = new TextField();
        txtAmount.setPromptText("Amount");

        // Add static fields to grid first
        grid.add(new Label("Date"), 0, 0);
        grid.add(dp, 1, 0);
        grid.add(new Label("Type"), 0, 1);
        grid.add(cmbType, 1, 1);
        grid.add(lblCategoryOrFrom, 0, 2);
        grid.add(lblAccountOrTo, 0, 3);
        grid.add(new Label("Amount"), 0, 4);
        grid.add(txtAmount, 1, 4);
        
        // Initialize form - this will add the appropriate fields
        updateFormFields.run();

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(new Callback<ButtonType, Transaction>() {
            @Override
            public Transaction call(ButtonType param) {
                if (param == addButtonType) {
                    try {
                        String type = cmbType.getValue();
                        double amount = Double.parseDouble(txtAmount.getText());
                        
                        if ("TRANSFER".equals(type)) {
                            // Handle transfer
                            String fromAccountName = cmbFromAccount.getValue();
                            String toAccountName = cmbToAccount.getValue();
                            
                            if (fromAccountName.equals(toAccountName)) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Invalid Transfer");
                                alert.setContentText("Source and destination accounts must be different.");
                                alert.showAndWait();
                                return null;
                            }
                            
                            if (amount <= 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Invalid Amount");
                                alert.setContentText("Amount must be greater than zero.");
                                alert.showAndWait();
                                return null;
                            }
                            
                            // Get account objects
                            Account fromAccount = store.getAccounts().stream()
                                .filter(a -> a.getName().equals(fromAccountName))
                                .findFirst()
                                .orElse(null);
                            
                            Account toAccount = store.getAccounts().stream()
                                .filter(a -> a.getName().equals(toAccountName))
                                .findFirst()
                                .orElse(null);
                            
                            if (fromAccount == null || toAccount == null) {
                                return null;
                            }
                            
                            if (fromAccount.getBalance() < amount) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Insufficient Balance");
                                alert.setContentText("Source account does not have sufficient balance.");
                                alert.showAndWait();
                                return null;
                            }
                            
                            // Create single transfer transaction
                            Transaction transfer = new Transaction(
                                UUID.randomUUID().toString(),
                                fromAccountName + " to " + toAccountName,
                                dp.getValue(),
                                amount,
                                Transaction.Type.TRANSFER,
                                fromAccount.getId(),
                                store.getCurrentUser() != null ? store.getCurrentUser().getId() : null
                            );
                            transfer.setToAccountId(toAccount.getId());
                            
                            // Update balances manually
                            fromAccount.setBalance(fromAccount.getBalance() - amount);
                            toAccount.setBalance(toAccount.getBalance() + amount);
                            
                            // Add transaction
                            store.addTransaction(transfer);
                            
                            return transfer;
                        } else {
                            // Handle regular income/expense
                            String category = cmbCategory.getValue();
                            if (category == null || category.isEmpty()) {
                                return null;
                            }
                            
                            if (amount <= 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Invalid Amount");
                                alert.setContentText("Amount must be greater than zero.");
                                alert.showAndWait();
                                return null;
                            }
                            
                            Transaction.Type transType = "INCOME".equals(type) ? Transaction.Type.INCOME : Transaction.Type.EXPENSE;
                            String accountName = cmbAccount.getValue();
                            String accountId = store.getAccounts().stream()
                                .filter(a -> a.getName().equals(accountName))
                                .findFirst()
                                .map(Account::getId)
                                .orElse(store.getAccounts().get(0).getId());
                            
                            Transaction newTransaction = new Transaction(
                                UUID.randomUUID().toString(), 
                                category, 
                                dp.getValue(), 
                                amount, 
                                transType, 
                                accountId,
                                store.getCurrentUser() != null ? store.getCurrentUser().getId() : null
                            );
                            
                            // Add transaction to store
                            store.addTransaction(newTransaction);
                            
                            return newTransaction;
                        }
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Input");
                        alert.setContentText("Please enter a valid amount.");
                        alert.showAndWait();
                        return null;
                    } catch (Exception ex) {
                        return null;
                    }
                }
                return null;
            }
        });

        Optional<Transaction> result = dialog.showAndWait();
        result.ifPresent(t -> {
            if (t != null) {
                refreshTable();
            }
        });
    }
    
    private void openEditDialog(Transaction transaction) {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");
        
        // Apply CSS to dialog
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        
        // Set dialog icon after showing
        dialog.setOnShown(e -> {
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/add.png"));
                javafx.stage.Stage stage = (javafx.stage.Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(icon);
            } catch (Exception ex) {
                System.err.println("Could not load edit transaction icon: " + ex.getMessage());
            }
        });

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Date picker (pre-filled with existing date)
        DatePicker dp = new DatePicker(transaction.getDate());
        
        // Type ComboBox (pre-filled)
        ComboBox<String> cmbType = new ComboBox<>();
        cmbType.getItems().addAll("INCOME", "EXPENSE");
        cmbType.setValue(transaction.getType().name());
        
        // Category ComboBox (depends on type)
        ComboBox<String> cmbCategory = new ComboBox<>();
        cmbCategory.setPromptText("Select Category");
        cmbCategory.setPrefWidth(150);
        
        // Add Category button
        Button btnAddCategory = new Button("+");
        btnAddCategory.setStyle("-fx-background-color: #FF6B35; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 5 12; -fx-cursor: hand;");
        btnAddCategory.setOnAction(e -> {
            TextInputDialog catDialog = new TextInputDialog();
            catDialog.setTitle("Add Category");
            catDialog.setHeaderText("Add New Category");
            catDialog.setContentText("Category Name:");
            catDialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            catDialog.getDialogPane().getStyleClass().add("dialog-pane");
            
            Optional<String> result = catDialog.showAndWait();
            result.ifPresent(catName -> {
                if (!catName.trim().isEmpty()) {
                    String type = cmbType.getValue();
                    store.addCategory(catName.trim(), type);
                    cmbCategory.getItems().add(catName.trim());
                    cmbCategory.setValue(catName.trim());
                }
            });
        });
        
        HBox categoryBox = new HBox(5, cmbCategory, btnAddCategory);
        
        // Update categories based on type selection
        Runnable updateCategories = () -> {
            String type = cmbType.getValue();
            cmbCategory.getItems().clear();
            
            // Load categories from DataStore
            List<String> categories = store.getCategoriesByType(type);
            cmbCategory.getItems().addAll(categories);
            
            // Set current category if it exists in the list
            if (categories.contains(transaction.getTitle())) {
                cmbCategory.setValue(transaction.getTitle());
            } else if (!cmbCategory.getItems().isEmpty()) {
                cmbCategory.setValue(cmbCategory.getItems().get(0));
            }
        };
        
        // Initialize categories
        updateCategories.run();
        
        // Listen for type changes
        cmbType.valueProperty().addListener((obs, oldVal, newVal) -> updateCategories.run());
        
        // Account ComboBox (pre-filled)
        ComboBox<String> cmbAccount = new ComboBox<>();
        for (Account a : store.getAccounts()) {
            cmbAccount.getItems().add(a.getName());
        }
        String currentAccountName = store.getAccounts().stream()
            .filter(a -> a.getId().equals(transaction.getAccountId()))
            .findFirst()
            .map(Account::getName)
            .orElse("");
        cmbAccount.setValue(currentAccountName);
        
        // Amount field (pre-filled)
        TextField txtAmount = new TextField(String.format("%.2f", transaction.getAmount()));
        txtAmount.setPromptText("Amount");

        grid.add(new Label("Date"), 0, 0);
        grid.add(dp, 1, 0);
        grid.add(new Label("Type"), 0, 1);
        grid.add(cmbType, 1, 1);
        grid.add(new Label("Category"), 0, 2);
        grid.add(categoryBox, 1, 2);
        grid.add(new Label("Account"), 0, 3);
        grid.add(cmbAccount, 1, 3);
        grid.add(new Label("Amount"), 0, 4);
        grid.add(txtAmount, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(new Callback<ButtonType, Transaction>() {
            @Override
            public Transaction call(ButtonType param) {
                if (param == saveButtonType) {
                    try {
                        String category = cmbCategory.getValue();
                        if (category == null || category.isEmpty()) {
                            return null;
                        }
                        
                        double amount = Double.parseDouble(txtAmount.getText());
                        Transaction.Type type = "INCOME".equals(cmbType.getValue()) ? Transaction.Type.INCOME : Transaction.Type.EXPENSE;
                        String accountName = cmbAccount.getValue();
                        String accountId = store.getAccounts().stream()
                            .filter(a -> a.getName().equals(accountName))
                            .findFirst()
                            .map(Account::getId)
                            .orElse(store.getAccounts().get(0).getId());
                        
                        // Update existing transaction
                        transaction.setTitle(category);
                        transaction.setDate(dp.getValue());
                        transaction.setAmount(amount);
                        transaction.setType(type);
                        transaction.setAccountId(accountId);
                        
                        return transaction;
                    } catch (Exception ex) {
                        return null;
                    }
                }
                return null;
            }
        });

        Optional<Transaction> result = dialog.showAndWait();
        result.ifPresent(t -> {
            store.updateTransaction(t);
            refreshTable();
        });
    }
    
    private void openTransferDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Transfer Between Accounts");
        
        // Apply CSS to dialog
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        ButtonType transferButtonType = new ButtonType("Transfer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(transferButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Date picker (default to today)
        DatePicker dp = new DatePicker(LocalDate.now());
        
        // From Account ComboBox
        ComboBox<String> cmbFromAccount = new ComboBox<>();
        for (Account a : store.getAccounts()) {
            cmbFromAccount.getItems().add(a.getName());
        }
        if (!cmbFromAccount.getItems().isEmpty()) {
            cmbFromAccount.setValue(cmbFromAccount.getItems().get(0));
        }
        
        // To Account ComboBox
        ComboBox<String> cmbToAccount = new ComboBox<>();
        for (Account a : store.getAccounts()) {
            cmbToAccount.getItems().add(a.getName());
        }
        if (cmbToAccount.getItems().size() > 1) {
            cmbToAccount.setValue(cmbToAccount.getItems().get(1));
        } else if (!cmbToAccount.getItems().isEmpty()) {
            cmbToAccount.setValue(cmbToAccount.getItems().get(0));
        }
        
        // Amount field
        TextField txtAmount = new TextField();
        txtAmount.setPromptText("Amount");

        grid.add(new Label("Date"), 0, 0);
        grid.add(dp, 1, 0);
        grid.add(new Label("From Account"), 0, 1);
        grid.add(cmbFromAccount, 1, 1);
        grid.add(new Label("To Account"), 0, 2);
        grid.add(cmbToAccount, 1, 2);
        grid.add(new Label("Amount"), 0, 3);
        grid.add(txtAmount, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(new Callback<ButtonType, Void>() {
            @Override
            public Void call(ButtonType param) {
                if (param == transferButtonType) {
                    try {
                        String fromAccountName = cmbFromAccount.getValue();
                        String toAccountName = cmbToAccount.getValue();
                        
                        // Validate: different accounts
                        if (fromAccountName.equals(toAccountName)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid Transfer");
                            alert.setContentText("Source and destination accounts must be different.");
                            alert.showAndWait();
                            return null;
                        }
                        
                        double amount = Double.parseDouble(txtAmount.getText());
                        
                        // Validate: positive amount
                        if (amount <= 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid Amount");
                            alert.setContentText("Amount must be greater than zero.");
                            alert.showAndWait();
                            return null;
                        }
                        
                        // Get account IDs
                        String fromAccountId = store.getAccounts().stream()
                            .filter(a -> a.getName().equals(fromAccountName))
                            .findFirst()
                            .map(Account::getId)
                            .orElse(null);
                            
                        String toAccountId = store.getAccounts().stream()
                            .filter(a -> a.getName().equals(toAccountName))
                            .findFirst()
                            .map(Account::getId)
                            .orElse(null);
                        
                        if (fromAccountId == null || toAccountId == null) {
                            return null;
                        }
                        
                        // Get from account to check balance
                        Account fromAccount = store.getAccounts().stream()
                            .filter(a -> a.getId().equals(fromAccountId))
                            .findFirst()
                            .orElse(null);
                        
                        if (fromAccount == null || fromAccount.getBalance() < amount) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Insufficient Balance");
                            alert.setContentText("Source account does not have sufficient balance.");
                            alert.showAndWait();
                            return null;
                        }
                        
                        // Get to account
                        Account toAccount = store.getAccounts().stream()
                            .filter(a -> a.getId().equals(toAccountId))
                            .findFirst()
                            .orElse(null);
                        
                        if (toAccount == null) {
                            return null;
                        }
                        
                        // Create transfer transactions for visibility in transaction list
                        Transaction transferOut = new Transaction(
                            java.util.UUID.randomUUID().toString(),
                            "Transfer to " + toAccountName,
                            dp.getValue(),
                            amount,
                            Transaction.Type.TRANSFER,
                            fromAccountId,
                            store.getCurrentUser() != null ? store.getCurrentUser().getId() : null
                        );
                        
                        Transaction transferIn = new Transaction(
                            java.util.UUID.randomUUID().toString(),
                            "Transfer from " + fromAccountName,
                            dp.getValue(),
                            amount,
                            Transaction.Type.TRANSFER,
                            toAccountId,
                            store.getCurrentUser() != null ? store.getCurrentUser().getId() : null
                        );
                        
                        // Update balances manually for transfers
                        fromAccount.setBalance(fromAccount.getBalance() - amount);
                        toAccount.setBalance(toAccount.getBalance() + amount);
                        
                        // Save transactions (no automatic balance update for TRANSFER type)
                        store.addTransaction(transferOut);
                        store.addTransaction(transferIn);
                        
                        refreshTable();
                        
                        // Show success message
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Transfer Complete");
                        alert.setContentText("Successfully transferred $" + String.format("%.2f", amount) + " from " + fromAccountName + " to " + toAccountName);
                        alert.showAndWait();
                        
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Input");
                        alert.setContentText("Please enter a valid amount.");
                        alert.showAndWait();
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Transfer Failed");
                        alert.setContentText("An error occurred: " + ex.getMessage());
                        alert.showAndWait();
                    }
                }
                return null;
            }
        });

        Optional<Void> result = dialog.showAndWait();
        result.ifPresent(v -> refreshTable());
    }
}
