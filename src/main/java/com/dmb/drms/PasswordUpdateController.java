package com.dmb.drms;

import com.dmb.drms.utils.AlertUtil;
import com.dmb.drms.utils.DBConnection;
import com.dmb.drms.utils.PasswordUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordUpdateController.class);

    private static final String UPDATE_FAILED = "Update Failed"; // Defined constant

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private void handlePasswordUpdate() { // Removed unused 'event' parameter
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            AlertUtil.showAlertWarning(UPDATE_FAILED, "Please enter and confirm your new password.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AlertUtil.showAlertError(UPDATE_FAILED, "Passwords do not match.");
            return;
        }

        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(newPassword, salt);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Users SET Password_Hash = ?, Password_Salt = ? WHERE User_Name = ?")) {

            statement.setString(1, hashedPassword);
            statement.setString(2, salt);
            statement.setString(3, username);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                AlertUtil.showAlertSuccess("Success", "Password updated successfully.");
                closeWindow();
            } else {
                AlertUtil.showAlertError(UPDATE_FAILED, "An error occurred while updating the password.");
            }
        } catch (SQLException e) {
            logger.error("SQL Exception during password update", e);
            AlertUtil.showAlertError(UPDATE_FAILED, "An error occurred while updating the password.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) newPasswordField.getScene().getWindow();
        stage.close();
    }
}
