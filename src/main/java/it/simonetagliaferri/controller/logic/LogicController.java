package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;

public class LogicController {

    protected final SessionManager sessionManager;

    public LogicController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Role getCurrentUserRole() {
        return sessionManager.getCurrentUser().getRole();
    }

    protected User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    protected void setCurrentUser(User user) {
        sessionManager.setCurrentUser(user);
    }

    public void logout() {
        sessionManager.clearSession();
    }
}
