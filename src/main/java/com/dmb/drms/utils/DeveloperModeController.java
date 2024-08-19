package com.dmb.drms.utils;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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
            showAlert("Access Denied", "Invalid Password", "Please enter the correct password.");
        }
    }

    private void loadLogs() {
        logArea.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("app.log"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logArea.appendText(formatLogLine(line));
            }
        } catch (IOException e) {
            logArea.appendText("Error loading logs: " + e.getMessage() + "\n");
        }
    }

    private String formatLogLine(String logLine) {
        return logLine + "\n"; // You can customize this to add additional formatting if needed
    }

    @FXML
    private void clearLogs() {
        logArea.clear();
        try (FileWriter writer = new FileWriter("app.log")) {
            // Truncate the file to clear its contents
            writer.write("");
        } catch (IOException e) {
            showAlert("Error", "Log Clearing Error", "Failed to clear logs: " + e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
