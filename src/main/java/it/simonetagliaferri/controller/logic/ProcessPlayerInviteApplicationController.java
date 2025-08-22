package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Team;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.utils.converters.InviteMapper;

import java.util.ArrayList;
import java.util.List;

public class ProcessPlayerInviteApplicationController extends ApplicationController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    ClubDAO clubDAO;

    public ProcessPlayerInviteApplicationController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    private Player loadPlayer() {
        String username = getCurrentUserUsername();
        return playerDAO.findByUsername(username);
    }

    private Player loadPlayer(String username) {
        return playerDAO.findByUsername(username);
    }

    private Invite loadInvite(InviteBean inviteBean) {
        TournamentBean tournamentBean = inviteBean.getTournament();
        String tournamentName = tournamentBean.getTournamentName();
        Club club = loadClub(inviteBean);
        Tournament tournament = tournamentDAO.getTournament(club, tournamentName);
        Player player = loadPlayer();
        return player.getInviteForTournament(tournament);
    }

    private Club loadClub(InviteBean inviteBean) {
        TournamentBean tournamentBean = inviteBean.getTournament();
        ClubBean clubBean = tournamentBean.getClub();
        HostBean hostBean = clubBean.getOwner();
        return clubDAO.getClubByHostName(hostBean.getUsername());
    }

    public List<InviteBean> getInvites() {
        String username = getCurrentUserUsername();
        Player player = playerDAO.findByUsername(username);
        List<InviteBean> inviteBeanList = new ArrayList<>();
        List<Invite> invites = player.getInvites();
        if (invites != null && !invites.isEmpty()) {
            for (Invite invite : invites) {
                invite.hasExpired();
                if (invite.getStatus() == InviteStatus.PENDING) {
                    InviteBean inviteBean = InviteMapper.toBean(invite);
                    inviteBeanList.add(inviteBean);
                }
            }
        }
        return inviteBeanList;
    }

    public void updateInvite(InviteBean inviteBean, InviteStatus status) {
        if (status != InviteStatus.PENDING) {
            Invite invite = loadInvite(inviteBean);
            Player player = invite.getPlayer();
            Tournament tournament = invite.getTournament();
            if (tournament.playerAlreadyConfirmed(player)) return;
            invite.updateStatus(status);
            Team team = tournament.getReservedTeam(player);
            if (tournament.isSingles())
                tournament.processInviteForSingles(invite, team);
            else {
                Invite otherInvite = null;
                Player player2 = team.getOtherPlayer(player.getUsername());
                if (player2 != null) {
                    player2 = loadPlayer(player2.getUsername());
                    otherInvite = player2.getInviteForTournament(tournament);
                }
                tournament.processInviteForDoubles(invite, team, otherInvite);
            }
            tournamentDAO.saveTournament(tournament.getClub(), tournament);
            playerDAO.savePlayer(player);
            hostDAO.saveHost(tournament.getClub().getOwner());
            player.clearInvite(invite);
        }
    }

    public boolean expiredInvite(InviteBean inviteBean) {
        Invite invite = loadInvite(inviteBean);
        Player player = invite.getPlayer();
        if (invite.hasExpired()) {
            player.clearInvite(invite);
            return true;
        }
        return false;
    }

    public boolean isNotSubscribed(InviteBean inviteBean) {
        Club club = loadClub(inviteBean);
        Player player = loadPlayer();
        return club.isNotSubscribed(player);
    }

    public void addClubToFavourites(InviteBean inviteBean) {
        Club club = loadClub(inviteBean);
        Player player = loadPlayer();
        club.subscribe(player);
        clubDAO.saveClub(club);
        playerDAO.savePlayer(player);
    }

}
