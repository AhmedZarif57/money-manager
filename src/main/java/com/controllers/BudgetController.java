package com.controllers;

<<<<<<< Updated upstream
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.utils.NavigationManager;

public class BudgetController {
    
    @FXML private Button dashboardButton, transactionsButton, analyticsButton, budgetButton, settingsButton, logoutButton;
    @FXML private Button btnAddBudget;
    @FXML private TableView<?> budgetTable;

    @FXML
    public void initialize() {
        setupNavigation();
        loadBudgets();
    }

    private void setupNavigation() {
        // Highlight active page
        setActiveButton(budgetButton);
        
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

    @FXML
    private void handleAddBudget() {
        System.out.println("Add Budget clicked");
        // TODO: Open add budget dialog
    }

    private void loadBudgets() {
        System.out.println("Loading budgets...");
        // TODO: Load budgets from database
    }
}
=======
import com.models.Budget;
import com.models.Transaction;
import com.utils.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;
import java.util.stream.DoubleStream;

public class BudgetController {

    @FXML private Button btnAddBudget;
    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, String> categoryColumn;
    @FXML private TableColumn<Budget, Double> limitColumn, spentColumn, remainingColumn;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        limitColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("limit"));

        refreshTable();

        btnAddBudget.setOnAction(e -> openAddBudgetDialog());
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
                double spent = store.getTransactions().stream()
                        .filter(t -> t.getTitle().equalsIgnoreCase(b.getCategory()))
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
                double spent = store.getTransactions().stream()
                        .filter(t -> t.getTitle().equalsIgnoreCase(b.getCategory()))
                        .mapToDouble(Transaction::getAmount).sum();
                setText(String.format("%.2f", b.getLimit() - spent));
            }
        });
    }

    private void openAddBudgetDialog() {
        Dialog<Budget> dialog = new Dialog<>();
        dialog.setTitle("Add Budget");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        TextField txtCategory = new TextField(); txtCategory.setPromptText("Category");
        TextField txtLimit = new TextField(); txtLimit.setPromptText("Limit");

        grid.add(new Label("Category"), 0, 0); grid.add(txtCategory, 1, 0);
        grid.add(new Label("Limit"), 0, 1); grid.add(txtLimit, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    String cat = txtCategory.getText();
                    double lim = Double.parseDouble(txtLimit.getText());
                    return new Budget(cat, lim);
                } catch (Exception e) { return null; }
            }
            return null;
        });

        Optional<Budget> res = dialog.showAndWait();
        res.ifPresent(b -> { store.addBudget(b); refreshTable(); });
    }
}
>>>>>>> Stashed changes
