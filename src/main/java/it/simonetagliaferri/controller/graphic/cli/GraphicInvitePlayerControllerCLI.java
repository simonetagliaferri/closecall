package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.InvitePlayerLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.view.cli.InvitePlayersHostView;
import it.simonetagliaferri.view.cli.InvitePlayersPlayerView;

import java.time.LocalDate;
import java.util.List;

public class GraphicInvitePlayerControllerCLI extends GraphicController {

    InvitePlayerLogicController controller;
    InvitePlayersPlayerView playerView;
    InvitePlayersHostView hostView;
    TournamentBean tournamentBean;

    public GraphicInvitePlayerControllerCLI(AppContext appContext, TournamentBean tournamentBean) {
        super(appContext);
        this.controller = new InvitePlayerLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getInviteDAO(), appContext.getDAOFactory().getPlayerDAO());
        this.playerView = new InvitePlayersPlayerView();
        this.hostView = new InvitePlayersHostView();
        this.tournamentBean = tournamentBean;
    }

    public void start(Role role) {
        if (role == Role.PLAYER) startPlayer();
        else startHost();
    }

    public void startPlayer() {
        List<InviteBean> invites = this.controller.getInvites();
        int choice = playerView.listNotifications(invites);
        if (choice == 0 ) return;
        InviteBean invite = invites.get(choice-1);
        PlayerBean teammate = this.controller.teammate(invite);
        String teammateName;
        if (teammate != null) {
            teammateName = teammate.getUsername();
            playerView.teamInvite(teammateName);
        }
        playerView.expandedInvite(invite);
        this.controller.updateInvite(invite, playerView.handleInvite());
    }

    public void startHost() {
        addPlayersToTournament(tournamentBean);
    }

    public void addPlayersToTournament(TournamentBean tournamentBean) {
        int choice;
        boolean expireDateSet = false;
        LocalDate inviteExpireDate = null;
        while (true) {
            choice = hostView.askToAddPlayer();
            if (choice == 1) {
                if (!expireDateSet) {
                    inviteExpireDate = tournamentBean.formatDate(hostView.inviteExpireDate());
                    expireDateSet = true;
                }
                if (tournamentBean.isSingles())
                    invitePlayer(inviteExpireDate);
                else
                    inviteTeam(inviteExpireDate);
            }
            else {
                break;
            }
        }
    }

    public void invitePlayer(LocalDate inviteExpireDate) {
        String playerUsername;
        PlayerBean player = null;
        String message = null;
        boolean email = false;
        boolean added = false;
        String emailAddress;
        while (!added) {
            playerUsername = hostView.getPlayer();
            player = this.controller.isPlayerValid(playerUsername);
            if (player!=null) {
                if (hostView.addMessage()==1) {
                    message = hostView.getMessage();
                }
                if (sendEmail()) {
                    email = true;
                }
                added = true;
            }
            else {
                hostView.invalidPlayer();
                if (sendEmail()) {
                    email = true;
                    emailAddress = hostView.getEmail();
                    player = new PlayerBean();
                    player.setEmail(emailAddress);
                    if (hostView.addMessage()==1) {
                        message = hostView.getMessage();
                    }
                }
                added = true;
            }
        }
        this.controller.invitePlayer(player, tournamentBean, inviteExpireDate, message, email);
    }

    public boolean sendEmail() {
        return hostView.askToSendEmail()==1;
    }

    public String getEmail() {
        return hostView.getEmail();
    }


    public void inviteTeam(LocalDate inviteExpireDate) {
        String playerUsername1;
        PlayerBean player1 = null;
        String message1 = null;
        boolean email1 = false;
        boolean added1 = false;
        String emailAddress1;
        String playerUsername2;
        PlayerBean player2 = null;
        String message2 = null;
        boolean email2 = false;
        boolean added2 = false;
        String emailAddress2;
        while (!added1) {
            playerUsername1 = hostView.getPlayer();
            player1 = this.controller.isPlayerValid(playerUsername1);
            if (player1!=null) {
                if (hostView.addMessage()==1) {
                    message1 = hostView.getMessage();
                }
                if (sendEmail()) {
                    email1 = true;
                }
                added1 = true;
            }
            else {
                hostView.invalidPlayer();
                if (sendEmail()) {
                    email1 = true;
                    emailAddress1 = hostView.getEmail();
                    player1 = new PlayerBean();
                    player1.setEmail(emailAddress1);
                    if (hostView.addMessage()==1) {
                        message1 = hostView.getMessage();
                    }
                }
                added1 = true;
            }
        }
        if (hostView.askToAddTeammate()==1) {
            while (!added2) {
                playerUsername2 = hostView.getPlayer();
                player2 = this.controller.isPlayerValid(playerUsername2);
                if (player2!=null) {
                    if (hostView.addMessage()==1) {
                        message1 = hostView.getMessage();
                    }
                    if (sendEmail()) {
                        email2 = true;
                    }
                    added2 = true;
                }
                else {
                    hostView.invalidPlayer();
                    if (sendEmail()) {
                        email2 = true;
                        emailAddress2 = hostView.getEmail();
                        player2 = new PlayerBean();
                        player2.setEmail(emailAddress2);
                        if (hostView.addMessage()==1) {
                            message2 = hostView.getMessage();
                        }
                    }
                    added2 = true;
                }
            }
        }
        this.controller.inviteTeam(player1, player2, tournamentBean, inviteExpireDate,message1, message2, email1, email2);
    }
}
