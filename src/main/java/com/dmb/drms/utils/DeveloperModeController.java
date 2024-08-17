package com.dmb.drms.utils;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeveloperModeController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextArea logArea;

    private static final String DEVELOPER_PASSWORD = "1234";

    @FXML
    private void unlockDeveloperMode() {
        String enteredPassword = passwordField.getText();
        if (DEVELOPER_PASSWORD.equals(enteredPassword)) {
            loadLogs();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Access Denied");
            alert.setHeaderText("Invalid Password");
            alert.setContentText("Please enter the correct password.");
            alert.showAndWait();
        }
    }

    private void loadLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader("app.log"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logArea.appendText(line + "\n");
            }
        } catch (IOException e) {
            logArea.appendText("Error loading logs: " + e.getMessage());
        }
    }
}
