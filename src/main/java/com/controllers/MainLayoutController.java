package com.controllers;

import com.utils.NavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainLayoutController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button dashboardButton, transactionsButton, analyticsButton,
            budgetButton, settingsButton, logoutButton;

    private String currentPage = "";

    @FXML
    public void initialize() {
        setupButtonActions();
    }

    private void setupButtonActions() {
        dashboardButton.setOnAction(e -> loadPage("dashboard"));
        transactionsButton.setOnAction(e -> loadPage("transactions"));
        analyticsButton.setOnAction(e -> loadPage("analytics"));
        budgetButton.setOnAction(e -> loadPage("budget"));
        settingsButton.setOnAction(e -> loadPage("settings"));
        logoutButton.setOnAction(e -> logout());
    }

    private void loadPage(String pageName) {
        if (currentPage.equals(pageName)) return;

        try {
            String fxmlPath = "/fxml/" + pageName + ".fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);
            currentPage = pageName;

            updateActiveButton(pageName);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load page: " + pageName);
        }
    }

    private void updateActiveButton(String pageName) {
        // Reset all buttons
        Button[] buttons = {dashboardButton, transactionsButton, analyticsButton,
                budgetButton, settingsButton};

        for (Button btn : buttons) {
            btn.getStyleClass().remove("nav-button-active");
        }

        // Set active button
        switch (pageName) {
            case "dashboard":
                dashboardButton.getStyleClass().add("nav-button-active");
                break;
            case "transactions":
                transactionsButton.getStyleClass().add("nav-button-active");
                break;
            case "analytics":
                analyticsButton.getStyleClass().add("nav-button-active");
                break;
            case "budget":
                budgetButton.getStyleClass().add("nav-button-active");
                break;
            case "settings":
                settingsButton.getStyleClass().add("nav-button-active");
                break;
        }
    }

    private void logout() {
        NavigationManager.navigateTo("login.fxml");
    }

    public void loadInitialPage(String pageName) {
        loadPage(pageName);
    }
}