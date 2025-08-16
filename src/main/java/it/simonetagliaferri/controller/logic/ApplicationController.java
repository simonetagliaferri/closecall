package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;

public class ApplicationController {

    protected final SessionManager sessionManager;

    public ApplicationController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Role getCurrentUserRole() {
        return sessionManager.getCurrentUser().getRole();
    }

    protected User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    public void logout() {
        sessionManager.clearSession();
    }
}
