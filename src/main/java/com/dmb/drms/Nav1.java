package com.dmb.drms;

import com.dmb.drms.utils.MainAppController;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nav1 extends MainAppController {
    private static final Logger logger = LoggerFactory.getLogger(Nav1.class);
    @FXML
    public void handleLoadPanel1() {
        if (mainApp != null) {
            mainApp.loadCenterContent("/com/dmb/drms/UI/Panels/MainModules/UserManagement/2.fxml", true);
        } else {
            logger.error("MainApp is null");
        }
    }
}
