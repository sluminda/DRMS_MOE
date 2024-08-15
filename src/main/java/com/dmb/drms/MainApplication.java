package com.dmb.drms;

import com.dmb.drms.utils.SceneCache;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the main layout from FXML
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/dmb/drms/UI/MainLayout.fxml"));
        rootLayout = fxmlLoader.load();

        // Load the login page initially
        loadCenterContent("/com/dmb/drms/UI/Panels/MainModules/UserManagement/UserRegistration.fxml", false);

        // Set the scene and show the stage
        Scene scene = new Scene(rootLayout, 800, 600);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setTitle("DRMS Application");
        stage.setScene(scene);
        stage.show();
    }

    // Method to load center content dynamically
    public void loadCenterContent(String fxmlFilePath, boolean cacheScene) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlFilePath));
        if (cacheScene) {
            rootLayout.setCenter(SceneCache.getScene(fxmlFilePath)); // Cache reusable scenes
        } else {
            rootLayout.setCenter(loader.load()); // Load without caching
        }
    }

    public void handleLoginSuccess() throws IOException {
        // Clear the login scene from memory by loading the main interface
        loadCenterContent("/com/dmb/drms/UI/Panels/MainPanel.fxml", true);

        // Optionally clear the SceneCache if you want to free up memory
        SceneCache.clearCache();
    }

    public static void main(String[] args) {
        launch();
    }
}
