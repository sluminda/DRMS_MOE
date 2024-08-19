package com.dmb.drms;

import com.dmb.drms.utils.*;
import com.dmb.drms.utils.sql.Session;
import com.dmb.drms.utils.sql.User;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
    private static final String LOGIN_QUERY = "SELECT User_ID, Name, User_Name, NIC, Email, Phone, User_Role, Password_Hash, Password_Salt FROM Users WHERE User_Name = ?";
    private static final String LOGIN_FAILED_MESSAGE = "Login Failed"; // Constant for "Login Failed"
    private MainApplication mainApp;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() { // Removed unused 'event' parameter
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showAlertWarning(LOGIN_FAILED_MESSAGE, "Please enter both username and password.");
            return;
        }

        Task<Void> loginTask = new Task<>() { // Using diamond operator <>
            @Override
            protected Void call() {
                performLogin(username, password);
                return null;
            }
        };

        new Thread(loginTask).start();
    }

    private void performLogin(String username, String password) {
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(LOGIN_QUERY)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String passwordHash = resultSet.getString("Password_Hash");
                        String passwordSalt = resultSet.getString("Password_Salt");

                        if (PasswordUtil.verifyPassword(password, passwordHash, passwordSalt)) {
                            // Create a User object from the database results
                            User user = new User(
                                    resultSet.getInt("User_ID"),
                                    resultSet.getString("Name"),
                                    resultSet.getString("User_Name"),
                                    resultSet.getString("NIC"),
                                    resultSet.getString("Email"),
                                    resultSet.getString("Phone"),
                                    resultSet.getString("User_Role")
                            );

                            // Store the User object in the session
                            Session.setCurrentUser(user);

                            if (password.equals(DEFAULT_PASSWORD)) {
                                javafx.application.Platform.runLater(() -> openPasswordUpdateDialog(username));
                            } else {
                                javafx.application.Platform.runLater(this::loadMainModulesPanel);
                            }
                        } else {
                            javafx.application.Platform.runLater(() ->
                                    AlertUtil.showAlertError(LOGIN_FAILED_MESSAGE, "Incorrect username or password."));
                        }
                    } else {
                        javafx.application.Platform.runLater(() ->
                                AlertUtil.showAlertError(LOGIN_FAILED_MESSAGE, "User not found."));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQL Exception during login", e);
            javafx.application.Platform.runLater(() ->
                    AlertUtil.showAlertError(LOGIN_FAILED_MESSAGE, "An error occurred while trying to log in. Please try again."));
        }
    }

    private void openPasswordUpdateDialog(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/dmb/drms/UI/Template/PasswordUpdate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Update Password");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

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
            // Clear the cache
            SceneCache.clearCache();

            // Load the MainModulesPanel.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dmb/drms/UI/Panels/MainModulesPanel.fxml"));
            VBox mainModulesPanel = loader.load();

            // Get the controller and set the MainApplication reference
            MainModulesPanelController controller = loader.getController();
            controller.setMainApp(mainApp);

            // Set the loaded panel to the center of the BorderPane
            mainApp.getRootLayout().setCenter(mainModulesPanel);

            // Access the Header controller that was set earlier
            Header headerController = (Header) mainApp.getRootLayout().getTop().getUserData();
            if (headerController != null) {
                headerController.updateHeaderAfterLogin();
            } else {
                logger.error("Header controller is null. Cannot update header after login.");
            }
        } catch (Exception e) {
            logger.error("Failed to load the main modules panel", e);
            AlertUtil.showAlertError("Error", "Could not load the main modules panel.");
        }
    }
}
