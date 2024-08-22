package com.dmb.drms.main_modules.table_views;

import com.dmb.drms.utils.DBConnection;
import com.dmb.drms.utils.MainAppController;
import com.dmb.drms.utils.UIUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class ProvincesTableViewCategory extends MainAppController {

    private static final Logger logger = LoggerFactory.getLogger(ProvincesTableViewCategory.class);

    @FXML
    private TilePane provincesListContainer;

    @FXML
    public void initialize() {
        CompletableFuture.runAsync(this::loadProvinces)
                .exceptionally(ex -> {
                    logger.error("Failed to load provinces asynchronously", ex);
                    return null;
                });
    }

    private void loadProvinces() {
        String query = "SELECT PTVC_Name, PTVC_FX_ID, PTVC_Image_Path FROM ProvincesTableViewCategory";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String provinceName = resultSet.getString("PTVC_Name");
                String provinceFXID = resultSet.getString("PTVC_FX_ID");
                String imagePath = resultSet.getString("PTVC_Image_Path");

                Platform.runLater(() -> addProvinceToUI(provinceName, provinceFXID, imagePath));
            }
        } catch (SQLException e) {
            logger.error("Failed to load provinces", e);
        }
    }

    private void addProvinceToUI(String provinceName, String provinceFXID, String imagePath) {
        VBox provinceBox = UIUtils.createModuleBox(provinceName, provinceFXID, imagePath, "/com/dmb/drms/Images/Body/MainModules/dashboard.png");

        switch (provinceFXID) {
            case "central":
                provinceBox.setOnMouseClicked(event -> loadUI("/com/dmb/drms/UI/Panels/CentralProvince.fxml"));
                break;
            case "eastern":
                provinceBox.setOnMouseClicked(event -> loadUI("/com/dmb/drms/UI/Panels/EasternProvince.fxml"));
                break;
            // Add more cases for other provinces
            default:
                logger.warn("No handler found for province FX ID: {}", provinceFXID);
        }

        provincesListContainer.getChildren().add(provinceBox);
    }

    private void loadUI(String fxmlFilePath) {
        if (mainApp != null) {
            mainApp.loadCenterContent(fxmlFilePath, true);
        } else {
            logger.error("MainApplication instance is null. Cannot load UI for path: {}", fxmlFilePath);
        }
    }
}
