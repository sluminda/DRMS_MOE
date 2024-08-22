package com.dmb.drms.main_modules.extra;

import com.dmb.drms.utils.MainAppController;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleFXMLLink extends MainAppController {
    private static final Logger logger = LoggerFactory.getLogger(SampleFXMLLink.class);

    @FXML
    public void handleLoadPanel() {
        if (mainApp != null) {
            mainApp.loadCenterContent("/com/dmb/drms/UI/Panels/MainModules/Provinces/Districts.fxml", true);
        } else {
            logger.error("MainApp is null");
        }
    }
}
