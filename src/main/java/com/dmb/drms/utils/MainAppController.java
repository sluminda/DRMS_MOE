package com.dmb.drms.utils;

import com.dmb.drms.MainApplication;
import javafx.fxml.FXML;

public abstract class MainAppController {
    protected MainApplication mainApp;

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void goBackward() {
        String previousPage = mainApp.getNavigationHistory().goBackward();
        if (previousPage != null) {
            mainApp.loadCenterContent(previousPage, true);
        }
    }

    @FXML
    private void goForward() {
        String nextPage = mainApp.getNavigationHistory().goForward();
        if (nextPage != null) {
            mainApp.loadCenterContent(nextPage, true);
        }
    }

    @FXML
    private void goFastBackward() {
        String firstPage = mainApp.getNavigationHistory().goFastBackward();
        if (firstPage != null) {
            mainApp.loadCenterContent(firstPage, false);
        }
    }

    @FXML
    private void goFastForward() {
        String lastPage = mainApp.getNavigationHistory().goFastForward();
        if (lastPage != null) {
            mainApp.loadCenterContent(lastPage, false);
        }
    }
}
