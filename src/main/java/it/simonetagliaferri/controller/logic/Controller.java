package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;

public class Controller {

    public Role getCurrentUserRole() {
        return NavigationManager.getInstance().getSessionManager().getCurrentUser().getRole();
    }

    protected User getCurrentUser() {
        return NavigationManager.getInstance().getSessionManager().getCurrentUser();
    }

    protected void setCurrentUser(User user) {
        NavigationManager.getInstance().getSessionManager().setCurrentUser(user);
    }

    // Public because it needs to be called from graphic controllers to trigger the logout.
    public void logout() {
        NavigationManager.getInstance().getSessionManager().clearSession();
    }
}
