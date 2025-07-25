package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.*;

public class JDBCDAOFactory extends DAOFactory {

    @Override
    public LoginDAO getLoginDAO() {
        return new JDBCLoginDAO();
    }

    @Override
    public TournamentDAO getTournamentDAO() {return new JDBCTournamentDAO();}

    @Override
    public HostDAO getHostDAO() { return new JDBCHostDAO();}

    @Override
    public PlayerDAO getPlayerDAO() {return new JDBCPlayerDAO();}

    @Override
    public ClubDAO getClubDAO() {return new JDBCClubDAO();}

    @Override
    public InviteDAO getInviteDAO() {
        return new JDBCInviteDAO();
    }
}
