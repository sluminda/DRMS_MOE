package com.dmb.drms;

import com.dmb.drms.utils.LogViewerController;
import com.dmb.drms.utils.SceneCache;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private BorderPane rootLayout;
    private Stage logViewerStage;
    private LogViewerController logViewerController;
    private static final String PASSWORD = "abcd"; // Password for log viewer

    @Override
    public void start(Stage stage) throws IOException {
        initializeMainLayout(stage);
        setupLogViewer();
        setupKeyboardShortcut(stage.getScene());
    }

    private void initializeMainLayout(Stage stage) throws IOException {
        rootLayout = loadFXML("/com/dmb/drms/UI/MainLayout.fxml");
        loadInitialContent();
        setupStage(stage, rootLayout, "DRMS Application", 800, 600);
    }

    private void loadInitialContent() throws IOException {
        loadCenterContent("/com/dmb/drms/UI/Panels/MainModules/UserManagement/UserRegistration.fxml", false);
    }

    private void setupLogViewer() throws IOException {
        logViewerStage = new Stage();
        logViewerController = loadLogViewerFXML(logViewerStage);
    }

    private void setupKeyboardShortcut(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.F12) {
                promptForPassword();
                event.consume();
            }
        });
    }

    private LogViewerController loadLogViewerFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dmb/drms/UI/Template/LogViewer.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Log Viewer");
        return loader.getController();
    }

    private void promptForPassword() {
        Dialog<String> dialog = createPasswordDialog();
        dialog.showAndWait().ifPresent(this::handlePasswordInput);
    }

    private Dialog<String> createPasswordDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Password Required");
        dialog.setHeaderText("Enter password to access log viewer:");
        PasswordField passwordField = new PasswordField();
        VBox vbox = new VBox(new Label("Password:"), passwordField);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? passwordField.getText() : null);
        return dialog;
    }

    private void handlePasswordInput(String password) {
        if (PASSWORD.equals(password.trim())) {
            toggleLogViewer();
        } else {
            showAlert("Invalid password", "The password you entered is incorrect.");
        }
    }

    private void toggleLogViewer() {
        if (logViewerStage.isShowing()) {
            logViewerStage.hide();
        } else {
            logViewerStage.show();
        }
    }

    public void log(String message) {
        if (logViewerController != null) {
            logViewerController.appendLog(message);
        }
    }

    private void loadCenterContent(String fxmlFilePath, boolean cacheScene) throws IOException {
        if (cacheScene) {
            rootLayout.setCenter(SceneCache.getScene(fxmlFilePath));
        } else {
            rootLayout.setCenter(loadFXML(fxmlFilePath));
        }
    }

    private BorderPane loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        return loader.load();
    }

    private void setupStage(Stage stage, BorderPane rootLayout, String title, int minWidth, int minHeight) {
        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
