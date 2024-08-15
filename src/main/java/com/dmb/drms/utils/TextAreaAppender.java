package com.dmb.drms.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TextAreaAppender extends AppenderBase<ILoggingEvent> {
    private static TextArea logArea;

    public static void setLogArea(TextArea logArea) {
        TextAreaAppender.logArea = logArea;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        final String message = eventObject.getFormattedMessage();
        Platform.runLater(() -> {
            if (logArea != null) {
                logArea.appendText(message + "\n");
            }
        });
    }
}
