package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;

public class JDBCDAOFactory extends DAOFactory {

    @Override
    public LoginDAO getLoginDAO() {
        return new JDBCLoginDAO();
    }

    @Override
    public TournamentDAO getTournamentDAO() {return new JDBCTournamentDAO(); }
}
