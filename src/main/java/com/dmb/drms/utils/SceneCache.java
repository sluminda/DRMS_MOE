package com.dmb.drms.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneCache {
    private static final Map<String, Parent> cache = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(SceneCache.class);

    // Private constructor to prevent instantiation
    private SceneCache() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Parent getScene(String fxmlPath) {
        if (cache.containsKey(fxmlPath)) {
            logger.info("Loading scene from cache: {}", fxmlPath);
            return cache.get(fxmlPath);
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneCache.class.getResource(fxmlPath));
            Parent sceneRoot = loader.load();
            cache.put(fxmlPath, sceneRoot);
            logger.info("Scene loaded and cached: {}", fxmlPath);
            return sceneRoot;
        } catch (IOException e) {
            logger.error("Failed to load FXML file: {}", fxmlPath, e);
            return null;
        }
    }

    public static void clearCache() {
        logger.info("Clearing scene cache.");
        cache.clear();
    }
}
