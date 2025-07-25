package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;

public abstract class NavigationManager {

    protected final AppContext appContext;

    protected NavigationManager(AppContext appContext) {
        this.appContext = appContext;
    }

    public abstract void start();

    public abstract void login();

    public abstract void goToDashboard(Role role);

    public abstract void goToAddTournament();

    public abstract void goToAddClub();

    public abstract void goToHandleNotification(Role role);

}
