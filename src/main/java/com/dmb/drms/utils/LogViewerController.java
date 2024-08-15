package com.dmb.drms.utils;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class LogViewerController {
    @FXML
    private TextArea logArea;

    @FXML
    public void initialize() {
        TextAreaAppender.setLogArea(logArea);
    }

    public void appendLog(String message) {
        // this used for logging
    }
}
