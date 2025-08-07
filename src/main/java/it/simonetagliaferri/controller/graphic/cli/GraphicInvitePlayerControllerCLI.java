package it.simonetagliaferri.controller.graphic.cli;

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
        addPlayersToTournament(tournamentBean);
    }

    public void addPlayersToTournament(TournamentBean tournamentBean) {
        InvitePlayersHostView.InviteChoices choice = InvitePlayersHostView.InviteChoices.YES;
        boolean expireDateSet = false;
        LocalDate inviteExpireDate = null;
        while (choice == InvitePlayersHostView.InviteChoices.YES) {
            if (!expireDateSet) {
                while (inviteExpireDate == null) {
                    try {
                        inviteExpireDate = DateConverter.formatDate(view.inviteExpireDate());
                    } catch (DateTimeException e) {
                        view.invalidDate();
                        continue;
                    }
                    if (!this.controller.isExpireDateValid(inviteExpireDate)) {
                        inviteExpireDate = null;
                        view.invalidExpireDate();
                    } else {
                        expireDateSet = true;
                    }
                }
            }
            if (tournamentBean.isSingles())
                invite(inviteExpireDate);
            else {
                inviteTeam(inviteExpireDate);
            }
            choice = view.askToAddPlayer();
        }
    }

    private PlayerBean getPlayer() {
        String playerName = view.getPlayer();
        PlayerBean playerBean = this.controller.isPlayerValid(playerName);
        if (playerBean != null) return playerBean;
        view.invalidPlayerUsername();
        if (sendEmail()) {
            if (!this.controller.isEmail(playerName)) {
                String playerEmail = getEmail();
                return new PlayerBean(playerName, playerEmail);
            }
            return null;
        }
        return null;
    }

    public void invite(LocalDate inviteExpireDate) {
        if (!this.controller.spotAvailable()) {
            view.fullTournament();
            return;
        }
        PlayerBean player = getPlayer();
        boolean email = true;
        if (player == null) return;
        String message = null;
        if (this.controller.playerAlreadyInvited(player)) {
            view.playerAlreadyInvited();
        }
        else {
            if (view.addMessage() == InvitePlayersHostView.InviteChoices.YES) {
            message = view.getMessage();
            }
            if (this.controller.isPlayerRegistered(player)) {
                if (!sendEmail()) {
                    email = false;
                }
            }
            this.controller.invitePlayer(player, inviteExpireDate, message, email);
        }
    }


    public boolean sendEmail() {
        return view.askToSendEmail() == InvitePlayersHostView.InviteChoices.YES;
    }

    public String getEmail() {
        return view.getEmail();
    }


    public void inviteTeam(LocalDate inviteExpireDate) {
        if (!this.controller.teamAvailable()) {
            view.noSpaceForTeam();
            return;
        }
        PlayerBean player1 = getPlayer();
        boolean email1 = true;
        if (player1 == null) return;
        String message1 = null;
        if (this.controller.playerAlreadyInvited(player1)) {
            view.playerAlreadyInvited();
            view.teamDeleted();
        }
        else {
            if (view.addMessage() == InvitePlayersHostView.InviteChoices.YES) {
                message1 = view.getMessage();
            }
            if (this.controller.isPlayerRegistered(player1)) {
                if (!sendEmail()) {
                    email1 = false;
                }
            }
            if (view.askToAddTeammate() == InvitePlayersHostView.InviteChoices.YES) {
                PlayerBean player2 = getPlayer();
                boolean email2 = true;
                if (player2 == null) return;
                String message2 = null;
                if (this.controller.playerAlreadyInvited(player2)) {
                    view.playerAlreadyInvited();
                    view.teamDeleted();
                }
                else {
                    if (view.addMessage() == InvitePlayersHostView.InviteChoices.YES) {
                        message2 = view.getMessage();
                    }
                    if (this.controller.isPlayerRegistered(player2)) {
                        if (!sendEmail()) {
                            email2 = false;
                        }
                    }
                    this.controller.inviteTeam(player1, player2, inviteExpireDate, message1, message2, email1, email2);
                }
            }
            else {
                this.controller.invitePlayer(player1, inviteExpireDate, message1, email1);
            }
        }
    }
}
