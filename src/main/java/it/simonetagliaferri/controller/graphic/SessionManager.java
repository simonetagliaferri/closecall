package it.simonetagliaferri.controller.graphic;

import it.simonetagliaferri.beans.UserBean;

public class SessionManager {
    private SessionManager() {}

    private static UserBean currentUser;

    public static UserBean getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserBean currentUser) {
        SessionManager.currentUser = currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }
}
