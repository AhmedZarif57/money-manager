package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.utils.NavigationManager;

public class SignupSuccessfulController {

    @FXML
    private Button btnGoToLogin;

    @FXML
    public void initialize() {
        if (btnGoToLogin != null) {
            btnGoToLogin.setOnAction(event -> NavigationManager.navigateTo("login.fxml"));
        }
    }
}
