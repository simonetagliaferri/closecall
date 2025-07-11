package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.AppContext;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;

public class LogicController {
    protected final AppContext appContext;

    public LogicController(AppContext appContext) {
        this.appContext = appContext;
    }

    public Role getCurrentUserRole() {
        return appContext.getSessionManager().getCurrentUser().getRole();
    }

    protected User getCurrentUser() {
        return appContext.getSessionManager().getCurrentUser();
    }

    protected void setCurrentUser(User user) {
        appContext.getSessionManager().setCurrentUser(user);
    }

    // Public because it needs to be called from graphic controllers to trigger the logout.
    public void logout() {
        appContext.getSessionManager().clearSession();
    }
}
