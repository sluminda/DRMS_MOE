package com.dmb.drms.utils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.SQLException;

public class FooterController {


    @FXML
    private Label isConnected;

    @FXML
    public void initialize() {
        checkDatabaseConnection();
    }

    private void checkDatabaseConnection() {
        try (Connection connection = DBConnection.getConnection()) {
            // If the connection is successful
            isConnected.setText("Connected");
            isConnected.getStyleClass().removeAll("red");
            isConnected.getStyleClass().add("green");
        } catch (SQLException e) {
            // If the connection fails
            isConnected.setText("Disconnected");
            isConnected.getStyleClass().removeAll("green");
            isConnected.getStyleClass().add("red");
        }
    }
}
