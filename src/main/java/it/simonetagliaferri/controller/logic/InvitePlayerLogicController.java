package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Team;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.model.invite.decorator.EmailDecorator;
import it.simonetagliaferri.model.invite.decorator.InAppInviteNotification;
import it.simonetagliaferri.model.invite.decorator.InviteNotification;
import it.simonetagliaferri.utils.converters.InviteMapper;
import it.simonetagliaferri.utils.converters.PlayerMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvitePlayerLogicController extends LogicController{

    InviteDAO inviteDAO;
    PlayerDAO playerDAO;
    public InvitePlayerLogicController(SessionManager sessionManager, InviteDAO inviteDAO, PlayerDAO playerDAO) {
        super(sessionManager);
        this.inviteDAO = inviteDAO;
        this.playerDAO = playerDAO;
    }

    public List<InviteBean> getInvites() {
        List<InviteBean> inviteBeanList = new ArrayList<>();
        List<Invite> invites = inviteDAO.getInvites(sessionManager.getCurrentUser().getUsername());
        if (invites != null && !invites.isEmpty()) {
            for (Invite invite : invites) {
                InviteBean inviteBean = InviteMapper.toBean(invite);
                inviteBeanList.add(inviteBean);
            }
        }
        return inviteBeanList;
    }

    public void updateInvite(InviteBean inviteBean, InviteStatus status){
        Invite invite = InviteMapper.fromBean(inviteBean);
        invite.updateStatus(status);
        Player player = playerDAO.findByUsername(getCurrentUser().getUsername());
        inviteDAO.delete(invite);
        Tournament tournament = invite.getTournament();
        Team team = tournament.getReservedTeam(player);
        Player player2 = team.getOtherPlayer(player.getUsername());
        Invite otherInvite = inviteDAO.getInvite(player2, tournament);
        if (tournament.isSingles())
            tournament.processInviteForSingles(invite, team);
        else
            tournament.processInviteForDoubles(invite, team, otherInvite);
    }

    public boolean isPlayerRegistered(PlayerBean player) {
        return !player.getUsername().equals(player.getEmail());
    }

    public PlayerBean teammate(InviteBean inviteBean) {
        Invite invite = InviteMapper.fromBean(inviteBean);
        Tournament tournament = invite.getTournament();
        Player player2 = null;
        Player player = playerDAO.findByUsername(getCurrentUser().getUsername());
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
        Player player = PlayerMapper.fromBean(playerBean);
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        Invite invite = new Invite(tournament, player, LocalDate.now(), inviteExpireDate, InviteStatus.PENDING, message);
        sendInvite(invite, email);
        tournament.reserveSpot(player);
    }

    public void inviteTeam(PlayerBean player1, PlayerBean player2, TournamentBean tournamentBean, LocalDate inviteExpireDate, String message1, String message2, boolean email1, boolean email2) {
        Player p1 = PlayerMapper.fromBean(player1);
        Player p2 = PlayerMapper.fromBean(player2);
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        Invite invite1 = new Invite(tournament, p1, LocalDate.now(), inviteExpireDate, InviteStatus.PENDING, message1);
        Invite invite2 = new Invite(tournament, p2, LocalDate.now(), inviteExpireDate, InviteStatus.PENDING, message2);
        sendInvite(invite1, email1);
        sendInvite(invite2, email2);
        tournament.reserveSpot(p1,p2);
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
