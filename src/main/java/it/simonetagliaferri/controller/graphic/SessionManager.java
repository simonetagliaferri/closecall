package it.simonetagliaferri.controller.graphic;

import it.simonetagliaferri.model.domain.User;

/*
    Class needed to propagate the active user through the run.
 */
public class SessionManager {

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
