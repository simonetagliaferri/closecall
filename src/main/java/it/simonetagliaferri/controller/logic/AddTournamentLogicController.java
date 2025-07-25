package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategyFactory;
import it.simonetagliaferri.service.InviteService;
import it.simonetagliaferri.utils.converters.ClubMapper;
import it.simonetagliaferri.utils.converters.TeamMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.time.LocalDate;
import java.util.List;

public class AddTournamentLogicController extends LogicController {
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    PlayerDAO playerDAO;
    ClubDAO clubDAO;
    InviteDAO inviteDAO;
    public AddTournamentLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO, ClubDAO clubDAO, HostDAO hostDAO, PlayerDAO playerDAO, InviteDAO inviteDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
        this.hostDAO = hostDAO;
        this.playerDAO = playerDAO;
        this.inviteDAO = inviteDAO;
    }

    public void addTournament(TournamentBean tournamentBean) {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        host.setTournaments(tournamentDAO.getTournaments(host));
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentFormat());
        tournament.setTournamentFormatStrategy(strategy);
        tournamentDAO.addTournament(host, tournament);
    }

    public LocalDate estimatedEndDate(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentFormat());
        tournament.setTournamentFormatStrategy(strategy);
        return tournament.estimateEndDate();
    }

    public LocalDate getStartDate(TournamentBean tournamentBean, LocalDate startDate) {
        startDate = tournamentBean.isDateValid(startDate);
        if (startDate == null) {
            throw new InvalidDateException();
        }
        if (!tournamentBean.isStartDateValid(startDate)) {
            throw new InvalidDateException();
        }
        return startDate;
    }

    public LocalDate getSignupDeadline(TournamentBean tournamentBean, LocalDate deadline) {
        deadline = tournamentBean.isDateValid(deadline);
        if (deadline == null || !tournamentBean.isDeadlineValid(deadline)) {
            throw new InvalidDateException();
        }
        return deadline;
    }

    public LocalDate getEndDate(TournamentBean tournamentBean, LocalDate endDate) {

        endDate = tournamentBean.isDateValid(endDate);
        if (endDate == null || !tournamentBean.isEndDateValid(endDate)) {
            throw new InvalidDateException();
        }
        return endDate;
    }

    public List<ClubBean> getClubBeans() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        List<Club> clubs = clubDAO.getClubs(host);
        return ClubMapper.toBean(clubs);
    }

    public boolean addPlayer(String player, TournamentBean tournamentBean) {
        Player p = isPlayerValid(player);
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        if (p == null) return false;
        if (tournament.isSingles()) {
            Team team = tournament.addTeam(p);
            tournamentBean.addTeam(TeamMapper.toBean(team));
        }
        return true;
    }

    public boolean invitePlayer(String player, TournamentBean tournamentBean, LocalDate inviteExpireDate, String message, boolean email) {
        Player p = isPlayerValid(player);
        if (p == null && !email) return false;
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        Invite invite = new Invite(tournament, p, LocalDate.now(), inviteExpireDate, InviteStatus.PENDING, message);
        InviteService inviteService = new InviteService(inviteDAO);
        inviteService.sendInvite(invite, email);
        Team team = new Team(p);
        tournament.reserveSpot(team);
        return true;
    }

    public boolean inviteTeam(String player1, String player2, TournamentBean tournamentBean, boolean email1, boolean email2) {
        Player p1 = isPlayerValid(player1);
        Player p2 = isPlayerValid(player2);
        if (p1 == null && !email1) return false;
        if (p2 == null && !email2) return false;
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        Invite invite1 = new Invite(tournament, p1, LocalDate.now(), LocalDate.now(), InviteStatus.PENDING, "");
        Invite invite2 = new Invite(tournament, p2, LocalDate.now(), LocalDate.now(), InviteStatus.PENDING, "");
        InviteService inviteService = new InviteService(inviteDAO);
        inviteService.sendInvite(invite1, email1);
        inviteService.sendInvite(invite2, email2);
        Team team = new Team(p1, p2);
        tournament.reserveSpot(team);
        return true;

    }

    public void addTeam(String player1, String player2, TournamentBean tournamentBean) {
        Player p1 = isPlayerValid(player1);
        Player p2 = isPlayerValid(player2);
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        Team team = tournament.addTeam(p1, p2);
        tournamentBean.addTeam(TeamMapper.toBean(team));
    }


    public Player isPlayerValid(String player) {
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
        return p;
    }

    public LocalDate MinimumStartDate() {
        return LocalDate.now().plusDays(2);
    }

    public LocalDate MinimumStartDate(TournamentBean tournamentBean) {
        return tournamentBean.getSignupDeadline().plusDays(1);
    }

    public LocalDate MinimumDeadline() {
        return LocalDate.now().plusDays(1);
    }

    public LocalDate MinimumDeadline(TournamentBean tournamentBean) {
        return tournamentBean.getStartDate().minusDays(1);
    }

    public LocalDate MinimumEndDate(TournamentBean tournamentBean) {
        return tournamentBean.getStartDate();
    }

}
