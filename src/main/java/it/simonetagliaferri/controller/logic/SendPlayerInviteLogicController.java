package it.simonetagliaferri.controller.logic;

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

    public void invitePlayer(PlayerBean playerBean, LocalDate inviteExpireDate, String message, boolean email) {
        Player player = playerDAO.findByUsername(playerBean.getUsername());
        Invite invite = createInvite(player, inviteExpireDate, message);
        sendInvite(invite, email);
        tournament.reserveSpot(player);
        tournamentDAO.saveTournament(tournament.getClub(), tournament);
        playerDAO.savePlayer(player);
    }

    public void inviteTeam(PlayerBean player1, PlayerBean player2, LocalDate inviteExpireDate, String message1, String message2, boolean email1, boolean email2) {
        Player p1 = playerDAO.findByUsername(player1.getUsername());
        Player p2 = playerDAO.findByUsername(player2.getUsername());
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

    public boolean playerAlreadyInvited(PlayerBean playerBean) {
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
        return tournament.isInviteExpireDateValid(date);
    }
}
