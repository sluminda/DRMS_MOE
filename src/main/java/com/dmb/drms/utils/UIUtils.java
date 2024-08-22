package com.dmb.drms.utils;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;

public class UIUtils {

    private static final Logger logger = LoggerFactory.getLogger(UIUtils.class);

    // Private constructor to prevent instantiation
    private UIUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static VBox createModuleBox(String name, String fxID, String imagePath, String defaultImagePath) {
        VBox box = new VBox();
        box.getStyleClass().add("module");
        box.setId(fxID);

        ImageView imageView = new ImageView();
        imageView.setCache(true);
        imageView.setFitHeight(55.0);
        imageView.setFitWidth(55.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        // Adjust the image path by removing the '@' symbol
        String adjustedImagePath = imagePath.replace("@", "");

        // Ensure the path is relative to the classpath
        URL imageUrl = UIUtils.class.getResource(adjustedImagePath);

        if (imageUrl != null) {
            imageView.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            logger.error("Failed to load image at path: {}", adjustedImagePath);
            // Optionally, set a default image if the original image is not found
            imageView.setImage(new Image(Objects.requireNonNull(UIUtils.class.getResource(defaultImagePath)).toExternalForm()));
        }

        Label label = new Label(name);

        box.getChildren().addAll(imageView, label);

        return box;
    }
}
