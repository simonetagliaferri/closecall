package it.simonetagliaferri.infrastructure;

import it.simonetagliaferri.model.domain.User;

/**
 * SessionManager is a stateful component that tracks the currently logged-in user.
 * It is created once by the AppContext at application startup and used throughout the lifecycle.
 * It should not be instantiated or used outside AppContext.
 */
public class SessionManager {

    private User currentUser;

    protected SessionManager() {
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void clearSession() {
        currentUser = null;
    }

    public String getCurrentUserUsername() {
        return currentUser.getUsername();
    }

}
