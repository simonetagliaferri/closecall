package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDAOFactory extends DAOFactory {

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, List<Tournament>> tournaments = new HashMap<>();
    private final Map<String, Club> clubs = new HashMap<>();
    private final Map<String, Host> hosts = new HashMap<>();
    private final Map<String, Player> players = new HashMap<>();

    private LoginDAO loginDAO;
    private TournamentDAO tournamentDAO;
    private HostDAO hostDAO;
    private PlayerDAO playerDAO;
    private ClubDAO clubDAO;

    @Override
    public LoginDAO getLoginDAO() {
        if (loginDAO == null) {
            loginDAO = new InMemoryLoginDAO(users);
        }
        return loginDAO;
    }

    @Override
    public TournamentDAO getTournamentDAO() {
        if (tournamentDAO == null) {
            tournamentDAO = new InMemoryTournamentDAO(tournaments);
        }
        return tournamentDAO;
    }

    @Override
    public HostDAO getHostDAO() {
        if (hostDAO == null) {
            hostDAO = new InMemoryHostDAO(hosts);
        }
        return hostDAO;
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        if (playerDAO == null) {
            playerDAO = new InMemoryPlayerDAO(players);
        }
        return playerDAO;
    }

    @Override
    public ClubDAO getClubDAO() {
        if (clubDAO == null) {
            clubDAO = new InMemoryClubDAO(clubs);
        }
        return clubDAO;
    }

}
