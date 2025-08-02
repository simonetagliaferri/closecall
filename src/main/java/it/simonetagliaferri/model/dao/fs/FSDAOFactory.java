package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.*;

public class FSDAOFactory extends DAOFactory {

    @Override
    public LoginDAO getLoginDAO() {
        return new FSLoginDAO();
    }

    @Override
    public TournamentDAO getTournamentDAO() { return new FSTournamentDAO();}

    @Override
    public HostDAO getHostDAO() { return new FSHostDAO();}

    @Override
    public PlayerDAO getPlayerDAO() { return new FSPlayerDAO();}

    @Override
    public ClubDAO getClubDAO() { return new FSClubDAO();}

}
