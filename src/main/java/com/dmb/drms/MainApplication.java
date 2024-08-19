package com.dmb.drms;

import com.dmb.drms.utils.AlertUtil;
import com.dmb.drms.utils.Header;
import com.dmb.drms.utils.SceneCache;
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
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private Stage primaryStage;

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

    public void loadCenterContent(String fxmlFilePath, boolean cacheScene) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Node centerNode;
            if (cacheScene) {
                centerNode = SceneCache.getScene(fxmlFilePath);
            } else {
                centerNode = loader.load();
            }
            rootLayout.setCenter(centerNode);

            // If it's the login screen, don't cache it
            if (fxmlFilePath.contains("LoginPanel.fxml")) {
                SceneCache.clearCache();
            }

            // Set mainApp reference in controller if needed
            Object controller = loader.getController();
            if (controller instanceof LoginController loginController) {
                loginController.setMainApp(this);
            }
        } catch (IOException e) {
            logger.error("Failed to load FXML file: {}", fxmlFilePath, e);
        }
    }

    private void openDeveloperMode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dmb/drms/UI/Template/LogViewer.fxml"));
            Node developerModeLayout = loader.load();

            // Cast the Node to Parent to use it in the Scene constructor
            Scene developerScene = new Scene((Parent) developerModeLayout);

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
        launch();
    }
}
