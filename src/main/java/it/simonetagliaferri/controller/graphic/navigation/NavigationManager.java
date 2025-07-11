package it.simonetagliaferri.controller.graphic.navigation;


import it.simonetagliaferri.AppContext;
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
}
