package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.SendPlayerInviteLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.utils.converters.DateConverter;
import it.simonetagliaferri.view.cli.InvitePlayersHostView;

import java.time.DateTimeException;
import java.time.LocalDate;

public class GraphicInvitePlayerControllerCLI extends GraphicController {

    InvitePlayersHostView view;
    TournamentBean tournamentBean;
    SendPlayerInviteLogicController controller;

    public GraphicInvitePlayerControllerCLI(AppContext appContext, TournamentBean tournamentBean) {
        super(appContext);
        this.controller = new SendPlayerInviteLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO(),
                tournamentBean);
        this.view = new InvitePlayersHostView();
        this.tournamentBean = tournamentBean;
    }

    public void start() {
        InvitePlayersHostView.InviteChoices choice = InvitePlayersHostView.InviteChoices.YES;
        LocalDate inviteExpireDate = setInvitesExpireDate();
        while (choice == InvitePlayersHostView.InviteChoices.YES) {
            if (tournamentBean.isSingles())
                invite(inviteExpireDate);
            else {
                inviteTeam(inviteExpireDate);
            }
            choice = view.askToAddPlayer();
        }
    }

    private LocalDate setInvitesExpireDate() {
        LocalDate inviteExpireDate;
        while (true) {
            try {
                inviteExpireDate = DateConverter.formatDate(view.inviteExpireDate());
            } catch (DateTimeException e) {
                view.invalidDate();
                continue;
            }
            if (!this.controller.isExpireDateValid(inviteExpireDate)) {
                view.invalidExpireDate();
            } else {
                break;
            }
        }
        return inviteExpireDate;
    }

    private PlayerBean getPlayer() {
        String playerName = view.getPlayer();
        PlayerBean playerBean = this.controller.isPlayerValid(playerName);
        if (playerBean != null) return playerBean; // The player is valid
        view.invalidPlayerUsername();
        if (sendEmail()) {
            if (!this.controller.isEmail(playerName)) { // If the host did not enter an email address, we ask for one
                String playerEmail = getEmail();
                return new PlayerBean(playerName, playerEmail);
            }
        }
        return null;
    }

    public void invite(LocalDate inviteExpireDate) {
        if (!this.controller.spotAvailable()) {
            view.fullTournament();
            return;
        }
        InviteBean inviteBean = invitePlayer(inviteExpireDate);
        if (inviteBean != null) {
            this.controller.invitePlayer(inviteBean);
        }
    }

    public boolean sendEmail() {
        return view.askToSendEmail() == InvitePlayersHostView.InviteChoices.YES;
    }

    public String getEmail() {
        return view.getEmail();
    }

    private InviteBean invitePlayer(LocalDate inviteExpireDate) {
        PlayerBean player = getPlayer();
        boolean email = true;
        String message = null;
        if (player != null) {
            if (this.controller.playerAlreadyInvited(player)) {
                view.playerAlreadyInvited();
                return null;
            } else {
                if (view.addMessage() == InvitePlayersHostView.InviteChoices.YES) {
                    message = view.getMessage();
                }
                if (this.controller.isPlayerRegistered(player)) {
                    /*
                     * If the player is registered, there is no need to ask for the email address. We already have it.
                     */
                    if (!sendEmail()) {
                        email = false;
                    }
                }
                return new InviteBean(player, inviteExpireDate, message, email);
            }
        }
        return null;
    }


    public void inviteTeam(LocalDate inviteExpireDate) {
        if (!this.controller.teamAvailable()) {
            view.noSpaceForTeam();
            return;
        }
        InviteBean invite1 = invitePlayer(inviteExpireDate);
        if (invite1 == null) { return; }
        PlayerBean player1 = invite1.getPlayer();
        if (player1 == null) {return;}
        if (this.controller.playerAlreadyInvited(player1)) {
            view.playerAlreadyInvited();
            view.teamDeleted();
            return;
        }
        if (view.askToAddTeammate() == InvitePlayersHostView.InviteChoices.YES) {
            InviteBean invite2 = invitePlayer(inviteExpireDate);
            if (invite2 == null) { return; }
            PlayerBean player2 = invite2.getPlayer();
            if (this.controller.playerAlreadyInvited(player2)) {
                view.playerAlreadyInvited();
                view.teamDeleted();
                return;
            }
            this.controller.inviteTeam(invite1, invite2);
        }
        else {
            this.controller.invitePlayer(invite1);
        }
    }

}
