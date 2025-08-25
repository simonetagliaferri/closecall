package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.domain.Role;

public abstract class ApplicationController {

    protected final SessionManager sessionManager;

    protected ApplicationController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Role getCurrentUserRole() {
        return sessionManager.getCurrentUser().getRole();
    }

    protected String getCurrentUserUsername() {
        return sessionManager.getCurrentUserUsername();
    }

    public void logout() {
        sessionManager.clearSession();
    }
}
