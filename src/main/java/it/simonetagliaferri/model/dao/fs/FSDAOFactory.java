package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.*;

public class FSDAOFactory extends DAOFactory {

    private LoginDAO loginDAO;
    private TournamentDAO tournamentDAO;
    private HostDAO hostDAO;
    private PlayerDAO playerDAO;
    private ClubDAO clubDAO;

    @Override
    public LoginDAO getLoginDAO() {
        if (loginDAO == null) {
            loginDAO = new FSLoginDAO();
        }
        return loginDAO;
    }

    @Override
    public TournamentDAO getTournamentDAO() {
        if (tournamentDAO == null) {
            tournamentDAO = new FSTournamentDAO();
        }
        return tournamentDAO;
    }

    @Override
    public HostDAO getHostDAO() {
        if (hostDAO == null) {
            hostDAO = new FSHostDAO();
        }
        return hostDAO;
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        if (playerDAO == null) {
            playerDAO = new FSPlayerDAO();
        }
        return playerDAO;
    }

    @Override
    public ClubDAO getClubDAO() {
        if (clubDAO == null) {
            clubDAO = new FSClubDAO();
        }
        return clubDAO;
    }

}
