package it.simonetagliaferri.model.dao.config;

import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.dao.jdbc.JDBCLoginDAO;

public class JDBCDAOConfigurator implements DAOConfiguratorStrategy{
    @Override
    public void configureAllDAOs() {
        LoginDAOFactory.getInstance().setImplClass(JDBCLoginDAO.class);
    }
}
