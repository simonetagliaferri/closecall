package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.model.domain.Role;

public class Controller {

    public Role getCurrentUserRole() {
        return NavigationManager.getInstance().getSessionManager().getCurrentUser().getRole();
    }
}
