package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategyFactory;
import it.simonetagliaferri.utils.converters.HostMapper;
import it.simonetagliaferri.utils.converters.TeamMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.time.LocalDate;

public class AddTournamentController extends Controller {
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    PlayerDAO playerDAO;
    public AddTournamentController() {
        tournamentDAO = DAOFactory.getDAOFactory().getTournamentDAO();
        hostDAO = DAOFactory.getDAOFactory().getHostDAO();
        playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
    }

    public void addTournament(TournamentBean tournamentBean) {
        User user = getCurrentUser();
        Host host = Host.fromUser(user);
        host.setTournaments(tournamentDAO.getTournaments(host));
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentFormat());
        tournament.setTournamentFormatStrategy(strategy);
        host.addTournament(tournament);
        tournamentDAO.addTournament(host, tournament);
    }

    public LocalDate estimatedEndDate(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentFormat());
        tournament.setTournamentFormatStrategy(strategy);
        return tournament.estimateEndDate();
    }

    public LocalDate getStartDate(TournamentBean tournamentBean, String strDate) {
        LocalDate startDate = tournamentBean.isDateValid(strDate);
        if (startDate == null) {
            throw new InvalidDateException();
        }
        return startDate;
    }

    public LocalDate getSignupDeadline(TournamentBean tournamentBean, String strDate) {
        LocalDate deadline = tournamentBean.isDateValid(strDate);
        if (deadline == null || !tournamentBean.isDeadlineValid(deadline)) {
            throw new InvalidDateException();
        }
        return deadline;
    }

    public LocalDate getEndDate(TournamentBean tournamentBean, String strDate) {
        LocalDate endDate = tournamentBean.isDateValid(strDate);
        if (endDate == null || !tournamentBean.isEndDateValid(endDate)) {
            throw new InvalidDateException();
        }
        return endDate;
    }

    public HostBean getHostBean() {
        User user = getCurrentUser();
        Host host = Host.fromUser(user);
        return HostMapper.toBean(host);
    }

    public boolean firstTime() {
        User user = getCurrentUser();
        Host host = Host.fromUser(user);
        return hostDAO.additionalInfo(host);
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

    public void addTeam(String player1, String player2, TournamentBean tournamentBean) {
        Player p1 = isPlayerValid(player1);
        Player p2 = isPlayerValid(player2);
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        Team team = tournament.addTeam(p1, p2);
        tournamentBean.addTeam(TeamMapper.toBean(team));
    }


    public Player isPlayerValid(String player) {
        PlayerBean playerBean = new PlayerBean();
        playerBean.setEmail(player);
        Player p;
        if (playerBean.validEmail()) {
            p = playerDAO.findByEmail(playerBean.getEmail());
        }
        else {
            playerBean.setEmail(null);
            playerBean.setUsername(player);
            p = playerDAO.findByUsername(player);
        }
        return p;
    }
}
