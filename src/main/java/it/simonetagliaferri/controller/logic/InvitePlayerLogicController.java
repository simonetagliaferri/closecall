package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.*;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.model.invite.decorator.EmailDecorator;
import it.simonetagliaferri.model.invite.decorator.InAppInviteNotification;
import it.simonetagliaferri.model.invite.decorator.InviteNotification;
import it.simonetagliaferri.utils.converters.InviteMapper;
import it.simonetagliaferri.utils.converters.PlayerMapper;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvitePlayerLogicController extends LogicController{

    InviteDAO inviteDAO;
    PlayerDAO playerDAO;
    HostDAO hostDAO;
    TournamentDAO tournamentDAO;
    ClubDAO clubDAO;
    public InvitePlayerLogicController(SessionManager sessionManager, InviteDAO inviteDAO, PlayerDAO playerDAO, HostDAO hostDao, TournamentDAO tournamentDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.inviteDAO = inviteDAO;
        this.playerDAO = playerDAO;
        this.hostDAO = hostDao;
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
    }

    public List<InviteBean> getInvites() {
        List<InviteBean> inviteBeanList = new ArrayList<>();
        List<Invite> invites = inviteDAO.getInvites(sessionManager.getCurrentUser().getUsername());
        if (invites != null && !invites.isEmpty()) {
            for (Invite invite : invites) {
                invite.hasExpired();
                InviteBean inviteBean = InviteMapper.toBean(invite);
                inviteBeanList.add(inviteBean);
            }
        }
        return inviteBeanList;
    }

    public LocalDate maxExpireDate(TournamentBean tournamentBean) {
        return tournamentBean.getSignupDeadline();
    }

    public LocalDate minExpireDate() {
        return LocalDate.now();
    }


    public void updateInvite(InviteBean inviteBean, InviteStatus status){
        if (status != InviteStatus.PENDING) {
            Invite invite = getInviteFromBean(inviteBean);
            Tournament tournament = invite.getTournament();
            Player player = invite.getPlayer();
            invite.updateStatus(status);
            inviteDAO.delete(invite);
            Team team = tournament.getReservedTeam(player);
            if (tournament.isSingles())
                tournament.processInviteForSingles(invite, team);
            else {
                Invite otherInvite = null;
                Player player2 = team.getOtherPlayer(player.getUsername());
                if (player2 != null) {
                    otherInvite = inviteDAO.getInvite(player2, tournament);
                }
                tournament.processInviteForDoubles(invite, team, otherInvite);
            }
            tournamentDAO.updateTournament(tournament.getClub(), tournament);
        }
    }

    public boolean isExpireDateValid(TournamentBean tournamentBean, LocalDate date) {
        return !date.isAfter(tournamentBean.getSignupDeadline());
    }

    public boolean expiredInvite(InviteBean inviteBean){
        Invite invite = getInviteFromBean(inviteBean);
        if (invite.hasExpired()) {
            inviteDAO.delete(invite);
            return true;
        }
        return false;
    }

    public boolean spotAvailable(TournamentBean tournamentBean){
        Tournament tournament = getTournamentFromBean(tournamentBean);
        return tournament.availableSpot();
    }

    public boolean teamAvailable(TournamentBean tournamentBean){
        Tournament tournament = getTournamentFromBean(tournamentBean);
        return tournament.availableTeamSpot();
    }

    public boolean isPlayerRegistered(PlayerBean player) {
        return !player.getUsername().equals(player.getEmail());
    }

    public Invite getInviteFromBean(InviteBean inviteBean) {
        TournamentBean tournamentBean = inviteBean.getTournament();
        Tournament tournament = getTournamentFromBean(tournamentBean);
        Player player = playerDAO.findByUsername(inviteBean.getPlayer().getUsername());
        return inviteDAO.getInvite(player, tournament);
    }

    public Tournament getTournamentFromBean(TournamentBean tournamentBean) {
        ClubBean clubBean = tournamentBean.getClub();
        HostBean hostBean = clubBean.getOwner();
        Host host = hostDAO.getHostByUsername(hostBean.getUsername());
        Club club = clubDAO.getClubByName(host.getUsername(), clubBean.getName());
        String tournamentName = tournamentBean.getTournamentName();
        String tournamentFormat = tournamentBean.getTournamentFormat();
        String tournamentType = tournamentBean.getTournamentType();
        LocalDate startDate = tournamentBean.getStartDate();
        return tournamentDAO.getTournament(club, tournamentName, tournamentFormat, tournamentType, startDate);
    }

    public PlayerBean teammate(InviteBean inviteBean) {
        Invite invite = getInviteFromBean(inviteBean);
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

    public void invitePlayer(PlayerBean playerBean, TournamentBean tournamentBean, LocalDate inviteExpireDate, String message, boolean email) {
        Player player = playerDAO.findByUsername(playerBean.getUsername());
        Tournament tournament = getTournamentFromBean(tournamentBean);
        Invite invite = new Invite(tournament, player, LocalDate.now(), inviteExpireDate, InviteStatus.PENDING, message);
        sendInvite(invite, email);
        tournament.reserveSpot(player);
        tournamentDAO.updateTournament(tournament.getClub(), tournament);
    }

    public void inviteTeam(PlayerBean player1, PlayerBean player2, TournamentBean tournamentBean, LocalDate inviteExpireDate, String message1, String message2, boolean email1, boolean email2) {
        Player p1 = playerDAO.findByUsername(player1.getUsername());
        Player p2 = playerDAO.findByUsername(player2.getUsername());
        Tournament tournament = getTournamentFromBean(tournamentBean);
        Invite invite1 = new Invite(tournament, p1, LocalDate.now(), inviteExpireDate, InviteStatus.PENDING, message1);
        Invite invite2 = new Invite(tournament, p2, LocalDate.now(), inviteExpireDate, InviteStatus.PENDING, message2);
        sendInvite(invite1, email1);
        sendInvite(invite2, email2);
        tournament.reserveSpot(p1,p2);
    }

    public boolean playerAlreadyInvited(PlayerBean playerBean, TournamentBean tournamentBean) {
        Tournament tournament = getTournamentFromBean(tournamentBean);
        Player player = playerDAO.findByUsername(playerBean.getUsername());
        return tournament.playerAlreadyInATeam(player);
    }

    public PlayerBean isPlayerValid(String player) {
        PlayerBean playerBean = new PlayerBean();
        Player p;
        if (playerBean.setEmail(player)) {
            p = playerDAO.findByEmail(playerBean.getEmail());
        }
        else {
            playerBean.setEmail(null);
            playerBean.setUsername(player);
            p = playerDAO.findByUsername(player);
        }
        return PlayerMapper.toBean(p);
    }

    public boolean isEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public void sendInvite(Invite invite, boolean email) {
        InviteNotification notifier = buildNotifier(email);
        notifier.send(invite);
    }

    private InviteNotification buildNotifier(boolean email) {
        InviteNotification base = new InAppInviteNotification(inviteDAO);
        if (email) {
            base = new EmailDecorator(base);
        }
        return base;
    }

}
