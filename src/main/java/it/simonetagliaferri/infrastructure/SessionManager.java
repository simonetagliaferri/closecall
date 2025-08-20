package it.simonetagliaferri.infrastructure;

import it.simonetagliaferri.model.domain.User;

/**
 * SessionManager is a stateful component that tracks the currently logged-in user.
 * It is created once by the AppContext at application startup and used throughout the lifecycle.
 * It should not be instantiated or used outside AppContext.
 */
public class SessionManager {

    protected SessionManager() {}

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void clearSession() {
        currentUser = null;
    }

}
