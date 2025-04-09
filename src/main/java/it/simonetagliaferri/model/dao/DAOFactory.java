package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.dao.demo.InMemoryDAOFactory;
import it.simonetagliaferri.model.dao.fs.FSDAOFactory;
import it.simonetagliaferri.model.dao.jdbc.JDBCDAOFactory;

import java.io.IOException;

import static it.simonetagliaferri.utils.PropertiesUtils.*;

public abstract class DAOFactory {

    private static DAOFactory instance;

    protected DAOFactory() {
    }

    public static synchronized DAOFactory getDAOFactory() throws IOException {
        if (instance == null) {
            PersistenceProvider provider = loadProperty(PERSISTENCE_PROPERTIES, PERSISTENCE_KEY, PersistenceProvider.class);
            switch (provider) {
                case IN_MEMORY:
                    instance = new InMemoryDAOFactory();
                    break;
                case FS:
                    instance = new FSDAOFactory();
                    break;
                case JDBC:
                    instance = new JDBCDAOFactory();
                    break;
                default:

            }
        }
        return instance;
    }

    public abstract LoginDAO getLoginDAO();

}
