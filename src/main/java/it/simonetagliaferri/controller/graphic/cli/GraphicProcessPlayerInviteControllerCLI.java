package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.ProcessPlayerInviteApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.view.cli.InvitePlayersPlayerView;

import java.util.List;

public class GraphicProcessPlayerInviteControllerCLI extends GraphicController {

    private final ProcessPlayerInviteApplicationController controller;
    private final InvitePlayersPlayerView view;

    public GraphicProcessPlayerInviteControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new ProcessPlayerInviteApplicationController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        this.view = new InvitePlayersPlayerView();
    }

    public void start() {
        List<InviteBean> invites = this.controller.getInvites();
        if (invites.isEmpty()) {
            view.noInvites();
            return;
        }
        int choice = view.listNotifications(invites);
        if (choice == -1) return;
        InviteBean invite = invites.get(choice);
        if (this.controller.expiredInvite(invite)) {
            view.expiredInvite(invite.getExpiryDate());
            return;
        }
        view.expandedInvite(invite);
        this.controller.updateInvite(invite, view.handleInvite());
        favouriteClub(invite);
    }

    private void favouriteClub(InviteBean invite) {
        if (this.controller.isNotSubscribed(invite) && view.addClubToFavourites() == InvitePlayersPlayerView.AddClubToFavourites.YES) {
            this.controller.addClubToFavourites(invite);
        }
    }

}
