package com.dmb.drms;

import com.dmb.drms.utils.MainAppController;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nav3 extends MainAppController {
    private static final Logger logger = LoggerFactory.getLogger(Nav3.class);

    @FXML
    public void handleLoadPanel3() {
        if (mainApp != null) {
            mainApp.loadCenterContent("/com/dmb/drms/UI/Panels/MainModules/UserManagement/4.fxml", true);
        } else {
            logger.error("MainApp is null");
        }
    }
}
