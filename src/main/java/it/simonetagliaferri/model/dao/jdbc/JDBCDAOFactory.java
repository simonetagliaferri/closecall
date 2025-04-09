package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;

public class JDBCDAOFactory extends DAOFactory {
    public JDBCDAOFactory() {}

    @Override
    public LoginDAO getLoginDAO() {
        return new JDBCLoginDAO();
    }
}
