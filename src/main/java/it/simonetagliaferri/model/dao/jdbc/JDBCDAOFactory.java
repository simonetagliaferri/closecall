package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.*;

public class JDBCDAOFactory extends DAOFactory {

    private JDBCLoginDAO loginDAO;
    private JDBCTournamentDAO tournamentDAO;
    private JDBCHostDAO hostDAO;
    private JDBCPlayerDAO playerDAO;
    private JDBCClubDAO clubDAO;

    @Override
    public LoginDAO getLoginDAO() {
        if (loginDAO == null) {
            loginDAO = new JDBCLoginDAO();
        }
        return loginDAO;
    }

    @Override
    public TournamentDAO getTournamentDAO() {
        if (tournamentDAO == null) {
            tournamentDAO = new JDBCTournamentDAO();
        }
        return tournamentDAO;
    }

    @Override
    public HostDAO getHostDAO() {
        if (hostDAO == null) {
            hostDAO = new JDBCHostDAO(getTournamentDAO());
        }
        return hostDAO;
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        if (playerDAO == null) {
            playerDAO = new JDBCPlayerDAO(getTournamentDAO());
        }
        return playerDAO;
    }

    @Override
    public ClubDAO getClubDAO() {
        if (clubDAO == null) {
            clubDAO = new JDBCClubDAO();
        }
        return clubDAO;
    }
}
