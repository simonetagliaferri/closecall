package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.*;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.utils.converters.InviteMapper;
import it.simonetagliaferri.utils.converters.PlayerMapper;

import java.util.ArrayList;
import java.util.List;

public class ProcessPlayerInviteLogicController extends LogicController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    ClubDAO clubDAO;

    public ProcessPlayerInviteLogicController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    public List<InviteBean> getInvites() {
        User user = getCurrentUser();
        Player player = playerDAO.findByUsername(user.getUsername());
        List<InviteBean> inviteBeanList = new ArrayList<>();
        List<Invite> invites = player.getInvites();
        if (invites != null && !invites.isEmpty()) {
            for (Invite invite : invites) {
                invite.hasExpired();
                InviteBean inviteBean = InviteMapper.toBean(invite);
                inviteBeanList.add(inviteBean);
            }
        }
        return inviteBeanList;
    }

    public void updateInvite(InviteBean inviteBean, InviteStatus status){
        if (status != InviteStatus.PENDING) {
            User user = getCurrentUser();
            Player player = playerDAO.findByUsername(user.getUsername());
            Invite invite = loadInvite(inviteBean);
            Tournament tournament = invite.getTournament();
            invite.updateStatus(status);
            player.clearInvite(invite);
            Team team = tournament.getReservedTeam(player);
            if (tournament.isSingles())
                tournament.processInviteForSingles(invite, team);
            else {
                Invite otherInvite = null;
                Player player2 = team.getOtherPlayer(player.getUsername());
                if (player2 != null) {
                    otherInvite = player2.getInviteForTournament(tournament);
                }
                tournament.processInviteForDoubles(invite, team, otherInvite);
            }
            tournamentDAO.saveTournament(tournament.getClub(), tournament);
            playerDAO.savePlayer(player);
            hostDAO.saveHost(tournament.getClub().getOwner());
        }
    }

    public boolean expiredInvite(InviteBean inviteBean){
        Invite invite = loadInvite(inviteBean);
        Player player = invite.getPlayer();
        if (invite.hasExpired()) {
            player.clearInvite(invite);
            return true;
        }
        return false;
    }

    private Player loadPlayer() {
        User user = getCurrentUser();
        return playerDAO.findByUsername(user.getUsername());
    }

    private Invite loadInvite(InviteBean inviteBean) {
        TournamentBean tournamentBean = inviteBean.getTournament();
        String tournamentName = tournamentBean.getTournamentName();
        ClubBean clubBean = tournamentBean.getClub();
        HostBean hostBean = clubBean.getOwner();
        Club club = clubDAO.getClubByHostName(hostBean.getUsername());
        Tournament tournament = tournamentDAO.getTournament(club, tournamentName);
        Player player = loadPlayer();
        return player.getInviteForTournament(tournament);
    }

    public PlayerBean teammate(InviteBean inviteBean) {
        Invite invite = loadInvite(inviteBean);
        Tournament tournament = invite.getTournament();
        Player player2 = null;
        Player player = invite.getPlayer();
        if (!tournament.isSingles()) {
            Team team = tournament.getReservedTeam(player);
            player2 = team.getOtherPlayer(getCurrentUser().getUsername());
        }
        if (player2 == null) {
            return null;
        }
        return PlayerMapper.toBean(player2);
    }

}
