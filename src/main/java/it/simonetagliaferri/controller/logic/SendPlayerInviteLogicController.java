package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.model.invite.decorator.EmailDecorator;
import it.simonetagliaferri.model.invite.decorator.InAppInviteNotification;
import it.simonetagliaferri.model.invite.decorator.InviteNotification;
import it.simonetagliaferri.utils.converters.PlayerMapper;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;

public class SendPlayerInviteLogicController extends LogicController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    ClubDAO clubDAO;
    Tournament tournament;

    public SendPlayerInviteLogicController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO, HostDAO hostDAO, ClubDAO clubDAO, TournamentBean tournamentBean) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
        this.tournament = loadTournament(tournamentBean);
    }

    private Tournament loadTournament(TournamentBean tournamentBean) {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        Club club = clubDAO.getClubByHostName(host.getUsername());
        host.addClub(club);
        return tournamentDAO.getTournament(club, tournamentBean.getTournamentName());
    }

    public void invitePlayer(InviteBean inviteBean) {
        PlayerBean playerBean = inviteBean.getPlayer();
        LocalDate inviteExpireDate = inviteBean.getExpiryDate();
        String message = inviteBean.getMessage();
        boolean email = inviteBean.getSendEmail();
        Player player = loadPlayer(playerBean);
        Invite invite = createInvite(player, inviteExpireDate, message);
        sendInvite(invite, email);
        playerDAO.savePlayer(player);
        tournament.reserveSpot(player);
        tournamentDAO.saveTournament(tournament.getClub(), tournament);
    }

    public void inviteTeam(InviteBean inviteBean1, InviteBean inviteBean2) {
        PlayerBean playerBean1 = inviteBean1.getPlayer();
        PlayerBean playerBean2 = inviteBean2.getPlayer();
        Player p1 = loadPlayer(playerBean1);
        Player p2 = loadPlayer(playerBean2);
        String message1 = inviteBean1.getMessage();
        String message2 = inviteBean2.getMessage();
        boolean email1 = inviteBean1.getSendEmail();
        boolean email2 = inviteBean2.getSendEmail();
        LocalDate inviteExpireDate = inviteBean1.getExpiryDate();
        Invite invite1 = createInvite(p1, inviteExpireDate, message1);
        Invite invite2 = createInvite(p2, inviteExpireDate, message2);
        sendInvite(invite1, email1);
        sendInvite(invite2, email2);
        tournament.reserveSpot(p1,p2);
        playerDAO.savePlayer(p1);
        playerDAO.savePlayer(p2);
        tournamentDAO.saveTournament(tournament.getClub(), tournament);
    }

    private Invite createInvite(Player player, LocalDate expireDate, String message) {
        return new Invite(tournament, player, LocalDate.now(), expireDate, InviteStatus.PENDING, message);
    }

    private Player loadPlayer(PlayerBean playerBean) {
        Player player = playerDAO.findByUsername(playerBean.getUsername());
        if (player == null) { player = PlayerMapper.fromBean(playerBean); }
        /* If player is null it means it's not a registered player. It will be saved as a player that has email=username,
         * so that if the player ever signs up, the invite will be in their dashboard.
         */
        return player;
    }

    public boolean playerAlreadyInvited(PlayerBean playerBean) {
        Player player = loadPlayer(playerBean);
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
        if (p != null) {
            return PlayerMapper.toBean(p);
        }
        return null;
    }

    public boolean isEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public void sendInvite(Invite invite, boolean email) {
        InviteNotification notifier = email
                ? new EmailDecorator(new InAppInviteNotification())
                : new InAppInviteNotification();
        notifier.send(invite);
    }

    public LocalDate maxExpireDate() {
        return tournament.maxInviteExpiryDate();
    }

    public LocalDate minExpireDate() {
        return tournament.minInviteExpiryDate();
    }

    public boolean spotAvailable(){
        return tournament.availableSpot();
    }

    public boolean teamAvailable(){
        return tournament.availableTeamSpot();
    }

    public boolean isPlayerRegistered(PlayerBean player) {
        String username = player.getUsername();
        String email = player.getEmail();
        return playerDAO.findByUsername(username) != null && playerDAO.findByEmail(email) != null;
    }

    public boolean isExpireDateValid(LocalDate date) {
        if (date == null) { return false; }
        return tournament.isInviteExpireDateValid(date);
    }
}
