package com.dmb.drms.main_modules.extra;

import com.dmb.drms.utils.MainAppController;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nav4 extends MainAppController {
    private static final Logger logger = LoggerFactory.getLogger(Nav4.class);

    @FXML
    public void handleLoadPanel4() {
        if (mainApp != null) {
            mainApp.loadCenterContent("/com/dmb/drms/UI/Extra/5.fxml", true);
        } else {
            logger.error("MainApp is null");
        }
    }
}
