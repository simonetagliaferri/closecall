package it.simonetagliaferri.model.dao.config;

import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.dao.demo.InMemoryLoginDAO;
import it.simonetagliaferri.model.dao.fs.FsLoginDAO;
import it.simonetagliaferri.model.dao.jdbc.JdbcLoginDAO;

public class DAOConfigurator {
    public static void configure(PersistenceProvider provider) {
        switch (provider) {
            case IN_MEMORY:
                LoginDAOFactory.getInstance().setLoginDaoImpl(InMemoryLoginDAO.class);
                break;
            case FS:
                LoginDAOFactory.getInstance().setLoginDaoImpl(FsLoginDAO.class);
                break;
            case JDBC:
                LoginDAOFactory.getInstance().setLoginDaoImpl(JdbcLoginDAO.class);
                break;
            default:
                throw new RuntimeException("Unsupported persistence provider: " + provider);
        }
    }
}
