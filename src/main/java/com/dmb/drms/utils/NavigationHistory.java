package com.dmb.drms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NavigationHistory {
    private static final Logger logger = LoggerFactory.getLogger(NavigationHistory.class);
    private final List<String> history;
    private int currentIndex;

    public NavigationHistory() {
        this.history = new ArrayList<>();
        this.currentIndex = -1;
    }

    // Add a new entry to the navigation history
    public void addEntry(String fxmlFilePath) {
        // Exclude LoginPanel.fxml from being added to the history
        if (fxmlFilePath.contains("LoginPanel.fxml")) {
            return;
        }

        // If we are adding a new entry and there are forward entries, remove them
        if (currentIndex < history.size() - 1) {
            logger.debug("Clearing forward history from index: " + (currentIndex + 1));
            history.subList(currentIndex + 1, history.size()).clear();
        }

        // Add the new entry and update the current index
        history.add(fxmlFilePath);
        currentIndex = history.size() - 1;
    }


    // Go backward in the navigation history
    public String goBackward() {
        if (currentIndex > 0) {
            currentIndex--;
            return history.get(currentIndex);
        }
        return null; // No backward history
    }

    // Go forward in the navigation history
    public String goForward() {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        }
        return null; // No forward history
    }

    // Go to the first entry in the navigation history
    public String goFastBackward() {
        if (currentIndex > 0) {
            currentIndex = 0;
            return history.get(currentIndex);
        }
        return null; // No backward history
    }

    // Go to the last entry in the navigation history
    public String goFastForward() {
        if (currentIndex < history.size() - 1) {
            currentIndex = history.size() - 1;
            return history.get(currentIndex);
        }
        return null; // No forward history
    }

    // Check if there is backward history available
    public boolean canGoBackward() {
        return currentIndex > 0;
    }

    // Check if there is forward history available
    public boolean canGoForward() {
        return currentIndex < history.size() - 1;
    }

    // Check if there is history to fast backward
    public boolean canGoFastBackward() {
        return currentIndex > 0;
    }

    // Check if there is history to fast forward
    public boolean canGoFastForward() {
        return currentIndex < history.size() - 1;
    }

    // Clear the entire navigation history
    public void clearHistory() {
        history.clear();
        currentIndex = -1;
    }

    // Clear the forward history (useful if adding a new entry after going backward)
    public void clearForwardHistory() {
        if (currentIndex < history.size() - 1) {
            history.subList(currentIndex + 1, history.size()).clear();
        }
    }

    // Get the current index
    public int getCurrentIndex() {
        return currentIndex;
    }

    // Get the history list (useful for debugging)
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
}
