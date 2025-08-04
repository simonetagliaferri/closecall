package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.InvitePlayerLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.utils.converters.DateConverter;
import it.simonetagliaferri.view.cli.InvitePlayersHostView;
import it.simonetagliaferri.view.cli.InvitePlayersPlayerView;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

public class GraphicInvitePlayerControllerCLI extends GraphicController {

    InvitePlayerLogicController controller;
    InvitePlayersPlayerView playerView;
    InvitePlayersHostView hostView;
    TournamentBean tournamentBean;

    public GraphicInvitePlayerControllerCLI(AppContext appContext, TournamentBean tournamentBean) {
        super(appContext);
        this.controller = new InvitePlayerLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO());
        this.playerView = new InvitePlayersPlayerView();
        this.hostView = new InvitePlayersHostView();
        this.tournamentBean = tournamentBean;
    }

    public void start(Role role) {
        if (role == Role.PLAYER) startPlayer();
        else startHost();
    }

    public void startPlayer() {
        if (playerView.chooseNotification() == InvitePlayersPlayerView.NotificationType.NOTIFICATIONS)
            navigationManager.goToNotifications(Role.PLAYER);
        else {
            List<InviteBean> invites = this.controller.getInvites();
            if (invites.isEmpty()) {
                playerView.noNotifications();
                return;
            }
            int choice = playerView.listNotifications(invites);
            if (choice == -1) return;
            InviteBean invite = invites.get(choice);
            PlayerBean teammate = this.controller.teammate(invite);
            String teammateName;
            if (teammate != null) {
                teammateName = teammate.getUsername();
                playerView.teamInvite(teammateName);
            }
            if (this.controller.expiredInvite(invite)) {
                playerView.expiredInvite(invite.getExpiryDate());
                return;
            }
            playerView.expandedInvite(invite);
            this.controller.updateInvite(invite, playerView.handleInvite());
        }
    }

    public void startHost() {
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
                        inviteExpireDate = DateConverter.formatDate(hostView.inviteExpireDate());
                    } catch (DateTimeException e) {
                        hostView.invalidDate();
                        continue;
                    }
                    if (!this.controller.isExpireDateValid(tournamentBean, inviteExpireDate)) {
                        inviteExpireDate = null;
                        hostView.invalidExpireDate();
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
            choice = hostView.askToAddPlayer();
        }
    }

    private PlayerBean getPlayer() {
        String playerName = hostView.getPlayer();
        PlayerBean playerBean = this.controller.isPlayerValid(playerName);
        if (playerBean != null) return playerBean;
        hostView.invalidPlayerUsername();
        if (sendEmail()) {
            if (!this.controller.isEmail(playerName))
                playerName = getEmail();
            return new PlayerBean(playerName, playerName);
        }
        else return null;
    }

    public void invite(LocalDate inviteExpireDate) {
        if (!this.controller.spotAvailable(tournamentBean)) {
            hostView.fullTournament();
            return;
        }
        PlayerBean player = getPlayer();
        boolean email = true;
        if (player == null) return;
        String message = null;
        if (this.controller.playerAlreadyInvited(player, tournamentBean)) {
            hostView.playerAlreadyInvited();
        }
        else {
            if (hostView.addMessage() == InvitePlayersHostView.InviteChoices.YES) {
            message = hostView.getMessage();
            }
            if (this.controller.isPlayerRegistered(player)) {
                if (!sendEmail()) {
                    email = false;
                }
            }
            this.controller.invitePlayer(player, tournamentBean, inviteExpireDate, message, email);
        }
    }


    public boolean sendEmail() {
        return hostView.askToSendEmail() == InvitePlayersHostView.InviteChoices.YES;
    }

    public String getEmail() {
        return hostView.getEmail();
    }


    public void inviteTeam(LocalDate inviteExpireDate) {
        if (!this.controller.teamAvailable(tournamentBean)) {
            hostView.noSpaceForTeam();
            return;
        }
        PlayerBean player1 = getPlayer();
        boolean email1 = true;
        if (player1 == null) return;
        String message1 = null;
        if (this.controller.playerAlreadyInvited(player1, tournamentBean)) {
            hostView.playerAlreadyInvited();
            hostView.teamDeleted();
        }
        else {
            if (hostView.addMessage() == InvitePlayersHostView.InviteChoices.YES) {
                message1 = hostView.getMessage();
            }
            if (this.controller.isPlayerRegistered(player1)) {
                if (!sendEmail()) {
                    email1 = false;
                }
            }
            if (hostView.askToAddTeammate() == InvitePlayersHostView.InviteChoices.YES) {
                PlayerBean player2 = getPlayer();
                boolean email2 = true;
                if (player2 == null) return;
                String message2 = null;
                if (this.controller.playerAlreadyInvited(player2, tournamentBean)) {
                    hostView.playerAlreadyInvited();
                    hostView.teamDeleted();
                }
                else {
                    if (hostView.addMessage() == InvitePlayersHostView.InviteChoices.YES) {
                        message2 = hostView.getMessage();
                    }
                    if (this.controller.isPlayerRegistered(player2)) {
                        if (!sendEmail()) {
                            email2 = false;
                        }
                    }
                    this.controller.inviteTeam(player1, player2, tournamentBean, inviteExpireDate, message1, message2, email1, email2);
                }
            }
            else {
                this.controller.invitePlayer(player1, tournamentBean, inviteExpireDate, message1, email1);
            }
        }
    }
}
