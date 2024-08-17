package com.dmb.drms;

import com.dmb.drms.utils.SceneCache;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApplication extends Application {
    private BorderPane rootLayout;
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    @Override
    public void start(Stage stage) {
        try {
            // Load the main layout from FXML
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/dmb/drms/UI/MainLayout.fxml"));
            rootLayout = fxmlLoader.load();

            // Load the login page initially
            loadCenterContent("/com/dmb/drms/UI/Panels/LoginPanel.fxml", false);

            // Set the scene and add a developer mode shortcut
            Scene scene = new Scene(rootLayout, 800, 600);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("DRMS Application");
            stage.setScene(scene);

            // Set up a shortcut for developer mode (Ctrl + F12)
            scene.setOnKeyPressed(event -> {
                if (event.isControlDown() && event.getCode() == KeyCode.F12) {
                    openDeveloperMode();
                }
            });

            stage.show();
        } catch (IOException e) {
            logger.error("Failed to start the application", e);
        }
    }

    // Method to load center content dynamically
    public void loadCenterContent(String fxmlFilePath, boolean cacheScene) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlFilePath));
            if (cacheScene) {
                rootLayout.setCenter(SceneCache.getScene(fxmlFilePath)); // Cache reusable scenes
            } else {
                rootLayout.setCenter(loader.load()); // Load without caching
            }
        } catch (IOException e) {
            logger.error("Failed to load FXML file: {}", fxmlFilePath, e);
        }
    }


    // Method to open the Developer Mode UI
    private void openDeveloperMode() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/dmb/drms/UI/Template/LogViewer.fxml"));
            VBox developerModeLayout = loader.load();
            Stage developerStage = new Stage();
            developerStage.setTitle("Developer Mode");
            Scene developerScene = new Scene(developerModeLayout);
            developerStage.setScene(developerScene);
            developerStage.show();
        } catch (IOException e) {
            logger.error("Failed to open Developer Mode", e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
