package com.dmb.drms.utils;

import com.dmb.drms.MainApplication;
import com.dmb.drms.utils.sql.Session;
import com.dmb.drms.utils.sql.User;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeaderController {

    private static final Logger logger = LoggerFactory.getLogger(HeaderController.class);
    private MainApplication mainApp;

    @FXML
    private ImageView headerRightImage;

    @FXML
    private HBox headerRightPanel;

    @FXML
    private MenuButton headerUsername;

    @FXML
    public void initialize() {
        headerRightPanel.setVisible(false);
        headerRightPanel.setManaged(false);
        headerRightImage.setVisible(true);
        headerRightImage.setManaged(true);
    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogOut() {
        boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Logout", "Are you sure you want to log out?");
        if (confirmed) {
            try {
                // Use the MainApplication's logOut method for consistency
                mainApp.logOut();
            } catch (Exception e) {
                logger.error("Failed to log out the user", e);
                AlertUtil.showAlertError("Logout Failed", "An error occurred during logout. Please try again.");
            }
        }
    }


    public void updateHeaderAfterLogin() {
        User user = Session.getCurrentUser();

        if (user != null) {
            headerRightImage.setVisible(false);
            headerRightImage.setManaged(false);
            headerRightPanel.setVisible(true);
            headerRightPanel.setManaged(true);

            headerUsername.setText(user.getUsername());
        }
    }
}
