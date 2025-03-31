package it.simonetagliaferri.model.dao.config;

import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.dao.demo.InMemoryLoginDAO;

public class InMemoryDAOConfigurator implements DAOConfiguratorStrategy{
    @Override
    public void configureAllDAOs() {
        LoginDAOFactory.getInstance().setImplClass(InMemoryLoginDAO.class);
    }
}
