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

public class TableViewMainCategoryController extends MainAppController {

    private static final Logger logger = LoggerFactory.getLogger(TableViewMainCategoryController.class);

    @FXML
    private TilePane tableViewMainCategoryContainer;

    @FXML
    public void initialize() {
        CompletableFuture.runAsync(this::loadCategories)
                .exceptionally(ex -> {
                    logger.error("Failed to load categories asynchronously", ex);
                    return null;
                });
    }

    private void loadCategories() {
        String query = "SELECT TVMC_Name, TVMC_FX_ID, TVMC_Image_Path FROM TableViewMainCategory";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String categoryName = resultSet.getString("TVMC_Name");
                String categoryFXID = resultSet.getString("TVMC_FX_ID");
                String imagePath = resultSet.getString("TVMC_Image_Path");

                Platform.runLater(() -> addCategoryToUI(categoryName, categoryFXID, imagePath));
            }
        } catch (SQLException e) {
            logger.error("Failed to load categories", e);
        }
    }

    private void addCategoryToUI(String categoryName, String categoryFXID, String imagePath) {
        VBox categoryBox = UIUtils.createModuleBox(categoryName, categoryFXID, imagePath, "/com/dmb/drms/Images/Body/MainModules/dashboard.png");

        switch (categoryFXID) {
            case "nschool":
                categoryBox.setOnMouseClicked(event -> loadUI("/com/dmb/drms/UI/Panels/MainModules/TableViews/ProvincesTableViewCategory.fxml"));
                break;
            case "ncoe":
                categoryBox.setOnMouseClicked(event -> loadUI("/com/dmb/drms/UI/Panels/NationalCollegeOfEducation.fxml"));
                break;
            case "ttc":
                categoryBox.setOnMouseClicked(event -> loadUI("/com/dmb/drms/UI/Panels/TeacherTrainingCollege.fxml"));
                break;
            case "moe":
                categoryBox.setOnMouseClicked(event -> loadUI("/com/dmb/drms/UI/Panels/MinistryOfEducation.fxml"));
                break;
            case "spc":
                categoryBox.setOnMouseClicked(event -> loadUI("/com/dmb/drms/UI/Panels/StatePrintingCorporation.fxml"));
                break;
            default:
                logger.warn("No handler found for category FX ID: {}", categoryFXID);
        }

        tableViewMainCategoryContainer.getChildren().add(categoryBox);
    }

    private void loadUI(String fxmlFilePath) {
        if (mainApp != null) {
            mainApp.loadCenterContent(fxmlFilePath, true);
        } else {
            logger.error("MainApplication instance is null. Cannot load UI for path: {}", fxmlFilePath);
        }
    }
}
