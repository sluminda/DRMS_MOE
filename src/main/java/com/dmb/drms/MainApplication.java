package com.dmb.drms;

import com.dmb.drms.utils.*;
import com.dmb.drms.utils.sql.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainApplication extends Application {
    private BorderPane rootLayout;
    private Stage primaryStage;
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    // Private navigation history instance
    private final NavigationHistory navigationHistory = new NavigationHistory();

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        try {
            // Load the main layout
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/dmb/drms/UI/MainLayout.fxml"));
            rootLayout = fxmlLoader.load();

            // Load the header and set it to the top of the BorderPane
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/com/dmb/drms/UI/Template/Header.fxml"));
            Node headerNode = headerLoader.load();
            Header headerController = headerLoader.getController();
            headerController.setMainApp(this); // Pass mainApp reference to header controller
            rootLayout.setTop(headerNode);
            rootLayout.getTop().setUserData(headerController);

            // Load the initial content (Login screen)
            loadCenterContent("/com/dmb/drms/UI/Panels/LoginPanel.fxml", false);

            // Create and set the scene
            Scene scene = new Scene(rootLayout, 800, 600);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("DRMS Application");
            stage.setScene(scene);

            // Setup developer mode toggle (Ctrl + F12)
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

    public NavigationHistory getNavigationHistory() {
        return navigationHistory;
    }

    public void loadCenterContent(String fxmlFilePath, boolean cacheScene) {
        try {
            Parent centerNode;
            Object controller;

            if (cacheScene) {
                // Try to get the scene and controller from the cache
                centerNode = SceneCache.getScene(fxmlFilePath);
                controller = SceneCache.getController(fxmlFilePath);

                if (centerNode == null || controller == null) {
                    // Load and cache the scene if not already cached
                    centerNode = SceneCache.loadAndCacheScene(fxmlFilePath);
                    controller = SceneCache.getController(fxmlFilePath);  // Get the controller after caching
                }
            } else {
                // Load the scene without caching it
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
                centerNode = loader.load();
                controller = loader.getController();
            }

            // Set the center of the root layout to the new scene
            rootLayout.setCenter(centerNode);

            // Automatically set MainApp reference if the controller implements MainAppController
            if (controller instanceof MainAppController mainAppController) {
                mainAppController.setMainApp(this);
            }

            // Add to navigation history
            navigationHistory.addEntry(fxmlFilePath);

        } catch (IOException e) {
            logger.error("Failed to load FXML file: {}", fxmlFilePath, e);
        }
    }


    private void openDeveloperMode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dmb/drms/UI/Template/LogViewer.fxml"));
            Parent developerModeLayout = loader.load();

            Scene developerScene = new Scene(developerModeLayout);

            Stage developerStage = new Stage();
            developerStage.setTitle("Developer Mode");
            developerStage.setScene(developerScene);
            developerStage.show();
        } catch (IOException e) {
            logger.error("Failed to open Developer Mode", e);
            AlertUtil.showAlertError("Error", "Could not open Developer Mode.");
        }
    }

    public void logOut() {
        try {
            // Clear the user session
            Session.clearSession();

            // Clear the scene cache to free up memory
            SceneCache.clearCache();

            // Load the login screen
            loadCenterContent("/com/dmb/drms/UI/Panels/LoginPanel.fxml", false);

            // Reset the header to initial state
            Header headerController = (Header) rootLayout.getTop().getUserData();
            if (headerController != null) {
                headerController.initialize();
            }

            logger.info("User logged out successfully.");
        } catch (Exception e) {
            logger.error("Logout process failed", e);
            AlertUtil.showAlertError("Logout Failed", "An error occurred during logout. Please try again.");
        }
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
