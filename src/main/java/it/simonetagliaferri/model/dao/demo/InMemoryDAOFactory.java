package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;

public class InMemoryDAOFactory extends DAOFactory {
    public InMemoryDAOFactory() {}

    @Override
    public LoginDAO getLoginDAO() {
        return new InMemoryLoginDAO();
    }
}
