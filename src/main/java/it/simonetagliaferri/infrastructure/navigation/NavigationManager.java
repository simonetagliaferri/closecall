package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;

/**
 * The navigation manager is used to move from one screen to another, it is used so that graphic controllers do not know about
 * other graphic controllers, they only know about the navigation manager and its methods to go somewhere else.
 */
public abstract class NavigationManager {

    protected final AppContext appContext;

    protected NavigationManager(AppContext appContext) {
        this.appContext = appContext;
    }

    public abstract void start();

    public abstract void login();

    /**
     * Navigates to the correct dashboard based on the role.
     * @param role can be either HOST or PLAYER.
     */
    public abstract void goToDashboard(Role role);

    public abstract void goToAddTournament();

    public abstract void goToAddClub();

    public abstract void goToInvitePlayer(TournamentBean tournamentBean);

    public abstract void goToJoinTournament();

    public abstract void goToNotifications(Role role);

    public abstract void goToProcessInvites();

    public abstract void goHome(Role role);
}
