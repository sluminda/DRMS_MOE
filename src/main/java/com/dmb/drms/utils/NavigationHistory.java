package com.dmb.drms.utils;

import java.util.ArrayList;
import java.util.List;

public class NavigationHistory {
    private final List<String> history = new ArrayList<>();
    private int currentIndex = -1;  // Start with no history

    public void addEntry(String fxmlFilePath) {
        if (currentIndex < history.size() - 1) {
            history.subList(currentIndex + 1, history.size()).clear();  // Clear forward history if new entry
        }
        history.add(fxmlFilePath);
        currentIndex = history.size() - 1;
    }

    public String goBackward() {
        if (currentIndex > 0) {
            currentIndex--;
            return history.get(currentIndex);
        }
        return null;  // No more history
    }

    public String goForward() {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        }
        return null;  // No more forward history
    }

    public String goFastBackward() {
        if (!history.isEmpty()) {
            currentIndex = 0;
            return history.get(currentIndex);
        }
        return null;
    }

    public String goFastForward() {
        if (!history.isEmpty()) {
            currentIndex = history.size() - 1;
            return history.get(currentIndex);
        }
        return null;
    }
}
