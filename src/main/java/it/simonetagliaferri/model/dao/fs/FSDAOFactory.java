package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;

public class FSDAOFactory extends DAOFactory {

    @Override
    public LoginDAO getLoginDAO() {
        return new FSLoginDAO();
    }
}
