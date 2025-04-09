package it.simonetagliaferri.model.dao.config;

import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.dao.fs.FSLoginDAO;

public class FSDAOConfigurator implements DAOConfiguratorStrategy {
    @Override
    public void configureAllDAOs() {
        LoginDAOFactory.getInstance().setImplClass(FSLoginDAO.class);
    }
}
