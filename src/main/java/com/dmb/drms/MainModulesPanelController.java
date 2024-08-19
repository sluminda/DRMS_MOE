package com.dmb.drms;

import com.dmb.drms.utils.DBConnection;
import com.dmb.drms.utils.sql.Session;
import com.dmb.drms.utils.sql.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class MainModulesPanelController {
    private static final Logger logger = LoggerFactory.getLogger(MainModulesPanelController.class);

    @FXML
    private TilePane moduleContainer;

    @FXML
    public void initialize() {
        // Load modules asynchronously to prevent UI freezing
        CompletableFuture.runAsync(this::loadModules)
                .exceptionally(ex -> {
                    logger.error("Failed to load modules asynchronously", ex);
                    return null;
                });
    }

    private void loadModules() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            logger.error("No user is currently logged in.");
            return;
        }

        String query = "SELECT m.M_Name, m.M_FX_ID, m.M_Image_Path " +
                "FROM Modules m " +
                "JOIN Privileges p ON m.M_ID = p.M_ID " +
                "WHERE p.User_ID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, currentUser.getUserID());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String moduleName = resultSet.getString("M_Name");
                    String moduleFXID = resultSet.getString("M_FX_ID");
                    String imagePath = resultSet.getString("M_Image_Path");

                    // Update the UI on the JavaFX Application Thread
                    Platform.runLater(() -> addModuleToUI(moduleName, moduleFXID, imagePath));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to load modules for the current user", e);
        }
    }

    private void addModuleToUI(String moduleName, String moduleFXID, String imagePath) {
        VBox moduleBox = new VBox();
        moduleBox.getStyleClass().add("module");
        moduleBox.setId(moduleFXID);

        ImageView imageView = new ImageView();
        imageView.setCache(true);
        imageView.setFitHeight(55.0);
        imageView.setFitWidth(55.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        // Adjust the image path by removing the '@' symbol
        String adjustedImagePath = imagePath.replace("@", "");

        // Ensure the path is relative to the classpath
        URL imageUrl = getClass().getResource(adjustedImagePath);

        if (imageUrl != null) {
            imageView.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            logger.error("Failed to load image at path: {}", adjustedImagePath);
            // Optionally, set a default image if the original image is not found
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/dmb/drms/Images/Body/MainModules/dashboard.png")).toExternalForm()));
        }

        Label label = new Label(moduleName);

        moduleBox.getChildren().addAll(imageView, label);

        // Event handler to load UserManagement.fxml when 'userManagement' module is clicked
        if ("userManagement".equals(moduleFXID)) {
            moduleBox.setOnMouseClicked(event -> loadUserManagementUI());
        }

        moduleContainer.getChildren().add(moduleBox);
    }

    private void loadUserManagementUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dmb/drms/UI/Panels/MainModules/UserManagement/UserManagement.fxml"));
            VBox userManagementUI = loader.load();

            // Replace the current content with the UserManagement UI
            // Assuming there's a container in your scene where you want to load the new UI
            moduleContainer.getChildren().setAll(userManagementUI);
        } catch (IOException e) {
            logger.error("Failed to load UserManagement.fxml", e);
        }
    }

}
