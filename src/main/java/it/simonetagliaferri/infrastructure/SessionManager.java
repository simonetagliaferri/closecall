package it.simonetagliaferri.infrastructure;

import it.simonetagliaferri.model.domain.User;

/**
 * SessionManager is a stateful component that tracks the currently logged-in user.
 * It is created once by the AppContext at application startup and used throughout the lifecycle.
 * It should not be instantiated or used outside AppContext.
 */
public class SessionManager {

    SessionManager() {}

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Not actually needed, but I think  it's better to have a clean Session after a logout and not have the previous user data still lurking around.
    public void clearSession() {
        currentUser = null;
    }
}
