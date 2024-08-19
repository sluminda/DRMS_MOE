package com.dmb.drms.utils;

import com.dmb.drms.utils.sql.User;

public class Session {
    private static User currentUser;

    private Session() {
        // Private constructor to prevent instantiation
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }
}
