package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;

public class FSDAOFactory extends DAOFactory {

    @Override
    public LoginDAO getLoginDAO() {
        return new FSLoginDAO();
    }

    @Override
    public TournamentDAO getTournamentDAO() { return new FSTournamentDAO(); }
}
