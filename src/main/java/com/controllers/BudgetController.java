package com.controllers;

import com.models.Budget;
import com.models.Transaction;
import com.utils.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

public class BudgetController {

    @FXML private Button btnAddBudget;
    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, String> categoryColumn;
    @FXML private TableColumn<Budget, Double> limitColumn, spentColumn, remainingColumn;
    @FXML private TableColumn<Budget, Void> actionColumn;
    @FXML private VBox budgetPageRoot;

    private Label walletBalance, bkashBalance, cardBalance, bankBalance;
    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        limitColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("limit"));
        
        // Setup action column with edit button
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            
            {
                editBtn.setStyle("-fx-background-color: #FF6B35; -fx-text-fill: white; -fx-cursor: hand;");
                editBtn.setOnAction(e -> {
                    Budget budget = getTableView().getItems().get(getIndex());
                    openEditBudgetDialog(budget);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editBtn);
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });

        refreshTable();

        btnAddBudget.setOnAction(e -> openAddBudgetDialog());
        
        // Add account balance tiles programmatically
        addAccountBalanceTiles();
        // Update tiles with actual data after creating them
        updateAccountTiles();
    }
    
    private void addAccountBalanceTiles() {
        // Create HBox for tiles
        javafx.scene.layout.HBox tilesBox = new javafx.scene.layout.HBox(15);
        
        // Create tiles for each account type
        String[] accountTypes = {"Wallet", "Bkash", "Card", "Bank"};
        Label[] balanceLabels = new Label[4];
        
        for (int i = 0; i < accountTypes.length; i++) {
            final String accountType = accountTypes[i];
            javafx.scene.layout.VBox tile = new javafx.scene.layout.VBox(8);
            tile.getStyleClass().add("glass-card");
            tile.setPrefWidth(170);
            tile.setPrefHeight(100);
            tile.setStyle("-fx-cursor: hand;");
            javafx.geometry.Insets padding = new javafx.geometry.Insets(16);
            tile.setPadding(padding);
            
            Label titleLabel = new Label(accountType);
            titleLabel.getStyleClass().add("card-title");
            
            balanceLabels[i] = new Label(String.format("%s0.00", SettingsController.getCurrencySymbol()));
            balanceLabels[i].getStyleClass().add("card-value");
            balanceLabels[i].setStyle("-fx-font-size: 14px; -fx-wrap-text: true; -fx-max-width: 140px;");
            
            // Add click handler for editing
            tile.setOnMouseClicked(event -> openEditAccountDialog(accountType));
            
            tile.getChildren().addAll(titleLabel, balanceLabels[i]);
            tilesBox.getChildren().add(tile);
        }
        
        // Assign to instance variables
        walletBalance = balanceLabels[0];
        bkashBalance = balanceLabels[1];
        cardBalance = balanceLabels[2];
        bankBalance = balanceLabels[3];
        
        // Insert tiles into the page (after title/button, before table)
        if (budgetPageRoot != null && budgetPageRoot.getChildren().size() > 1) {
            budgetPageRoot.getChildren().add(1, tilesBox);
        }
    }

    private void updateAccountTiles() {
        for (com.models.Account account : store.getAccounts()) {
            String balance = String.format("%s%.2f", SettingsController.getCurrencySymbol(), account.getBalance());
            switch (account.getName()) {
                case "Wallet":
                    if (walletBalance != null) walletBalance.setText(balance);
                    break;
                case "Bkash":
                    if (bkashBalance != null) bkashBalance.setText(balance);
                    break;
                case "Card":
                    if (cardBalance != null) cardBalance.setText(balance);
                    break;
                case "Bank":
                    if (bankBalance != null) bankBalance.setText(balance);
                    break;
            }
        }
    }
    
    private void refreshTable() {
        ObservableList<Budget> list = FXCollections.observableArrayList(store.getBudgets());
        budgetTable.setItems(list);

        // compute spent and remaining columns via cell factories
        spentColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setText(null); return; }
                Budget b = getTableView().getItems().get(getIndex());
                int selectedMonth = SettingsController.getSelectedMonth();
                int currentYear = java.time.LocalDate.now().getYear();
                double spent = store.getTransactions().stream()
                        .filter(t -> t.getTitle().equalsIgnoreCase(b.getCategory()))
                        .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear)
                        .mapToDouble(Transaction::getAmount).sum();
                setText(String.format("%.2f", spent));
            }
        });

        remainingColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setText(null); return; }
                Budget b = getTableView().getItems().get(getIndex());
                int selectedMonth = SettingsController.getSelectedMonth();
                int currentYear = java.time.LocalDate.now().getYear();
                double spent = store.getTransactions().stream()
                        .filter(t -> t.getTitle().equalsIgnoreCase(b.getCategory()))
                        .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear)
                        .mapToDouble(Transaction::getAmount).sum();
                setText(String.format("%.2f", b.getLimit() - spent));
            }
        });
    }

    private void openAddBudgetDialog() {
        Dialog<Budget> dialog = new Dialog<>();
        dialog.setTitle("Add Budget");
        
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
                System.err.println("Could not load add budget icon: " + ex.getMessage());
            }
        });
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        // Only allow expense categories for budgets - load from DataStore
        ComboBox<String> cmbCategory = new ComboBox<>();
        List<String> expenseCategories = store.getCategoriesByType("EXPENSE");
        cmbCategory.getItems().addAll(expenseCategories);
        cmbCategory.setPromptText("Select Category");
        
        TextField txtLimit = new TextField(); 
        txtLimit.setPromptText("Limit");

        grid.add(new Label("Category"), 0, 0); 
        grid.add(cmbCategory, 1, 0);
        grid.add(new Label("Limit"), 0, 1); 
        grid.add(txtLimit, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    String cat = cmbCategory.getValue();
                    if (cat == null || cat.isEmpty()) return null;
                    double lim = Double.parseDouble(txtLimit.getText());
                    return new Budget(cat, lim);
                } catch (Exception e) { return null; }
            }
            return null;
        });

        Optional<Budget> res = dialog.showAndWait();
        res.ifPresent(b -> {
            // Check if budget for this category already exists
            Budget existingBudget = store.getBudgets().stream()
                .filter(budget -> budget.getCategory().equalsIgnoreCase(b.getCategory()))
                .findFirst()
                .orElse(null);
            
            if (existingBudget != null) {
                // Update existing budget limit
                existingBudget.setLimit(existingBudget.getLimit() + b.getLimit());
                store.saveBudgets();
            } else {
                // Add new budget
                store.addBudget(b);
            }
            refreshTable();
        });
    }

    private void openEditAccountDialog(String accountType) {
        // Find the account
        com.models.Account account = store.getAccounts().stream()
            .filter(a -> a.getName().equals(accountType) && a.getUserId().equals(store.getCurrentUser().getId()))
            .findFirst()
            .orElse(null);
        
        if (account == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Account not found");
            alert.showAndWait();
            return;
        }
        
        javafx.scene.control.Dialog<Double> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Edit " + accountType + " Balance");
        dialog.setHeaderText("Current Balance: " + String.format("%s%.2f", SettingsController.getCurrencySymbol(), account.getBalance()));
        
        javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);
        
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        javafx.scene.control.TextField txtBalance = new javafx.scene.control.TextField(String.format("%.2f", account.getBalance()));
        txtBalance.setPromptText("New Balance");
        
        grid.add(new javafx.scene.control.Label("New Balance:"), 0, 0);
        grid.add(txtBalance, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return Double.parseDouble(txtBalance.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
        
        java.util.Optional<Double> result = dialog.showAndWait();
        result.ifPresent(newBalance -> {
            if (newBalance != null && newBalance >= 0) {
                account.setBalance(Math.round(newBalance * 100.0) / 100.0);
                store.saveAccounts();
                updateAccountTiles();
            }
        });
    }
    
    private void openEditBudgetDialog(Budget budget) {
        Dialog<Budget> dialog = new Dialog<>();
        dialog.setTitle("Edit Budget");
        
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
                System.err.println("Could not load edit budget icon: " + ex.getMessage());
            }
        });
        
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        // Category is read-only in edit mode
        Label lblCategory = new Label(budget.getCategory());
        lblCategory.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        TextField txtLimit = new TextField(String.valueOf(budget.getLimit())); 
        txtLimit.setPromptText("Limit");

        grid.add(new Label("Category"), 0, 0); 
        grid.add(lblCategory, 1, 0);
        grid.add(new Label("Limit"), 0, 1); 
        grid.add(txtLimit, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(bt -> {
            if (bt == deleteButtonType) {
                // Show confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Budget");
                alert.setHeaderText("Confirm Deletion");
                alert.setContentText("Are you sure you want to delete this budget?");
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                alert.getDialogPane().getStyleClass().add("dialog-pane");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    store.deleteBudget(budget);
                    refreshTable();
                }
                return null;
            } else if (bt == ButtonType.OK) {
                try {
                    double lim = Double.parseDouble(txtLimit.getText());
                    budget.setLimit(lim);
                    return budget;
                } catch (Exception e) { return null; }
            }
            return null;
        });

        Optional<Budget> res = dialog.showAndWait();
        res.ifPresent(b -> { store.saveBudgets(); refreshTable(); });
    }
}
