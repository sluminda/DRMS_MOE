package com.dmb.drms;

import com.dmb.drms.MainApplication;
import com.dmb.drms.PasswordUpdateController;
import com.dmb.drms.utils.AlertUtil;
import com.dmb.drms.utils.DBConnection;
import com.dmb.drms.utils.PasswordUtil;
import com.dmb.drms.utils.SceneCache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String DEFAULT_PASSWORD = "1234";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showAlertWarning("Login Failed", "Please enter both username and password.");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT Password_Hash, Password_Salt FROM Users WHERE User_Name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String passwordHash = resultSet.getString("Password_Hash");
                String passwordSalt = resultSet.getString("Password_Salt");

                // Check if the entered password matches the stored hash
                if (PasswordUtil.verifyPassword(password, passwordHash, passwordSalt)) {
                    if (password.equals(DEFAULT_PASSWORD)) {
                        // Open the password update dialog
                        openPasswordUpdateDialog(username);
                    } else {
                        // Successful login, proceed to the main modules panel
                        loadMainModulesPanel();
                    }
                } else {
                    AlertUtil.showAlertError("Login Failed", "Incorrect username or password.");
                }
            } else {
                AlertUtil.showAlertError("Login Failed", "User not found.");
            }
        } catch (SQLException e) {
            logger.error("SQL Exception during login", e);
            AlertUtil.showAlertError("Login Failed", "An error occurred while trying to log in.");
        }
    }

    private void openPasswordUpdateDialog(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/dmb/drms/UI/Template/PasswordUpdate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Update Password");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            // Pass the username to the PasswordUpdateController
            PasswordUpdateController controller = loader.getController();
            controller.setUsername(username);

            stage.showAndWait();
        } catch (IOException e) {
            logger.error("Failed to open the password update dialog", e);
            AlertUtil.showAlertError("Error", "Could not open the password update dialog.");
        }
    }

    private void loadMainModulesPanel() {
        try {
            SceneCache.clearCache(); // Clear cache to ensure the latest scene is loaded
            MainApplication mainApp = new MainApplication();
            mainApp.loadCenterContent("/com/dmb/drms/UI/Panels/MainModulesPanel.fxml", true);
        } catch (Exception e) {
            logger.error("Failed to load the main modules panel", e);
            AlertUtil.showAlertError("Error", "Could not load the main modules panel.");
        }
    }
}