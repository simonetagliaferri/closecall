package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.dao.demo.InMemoryDAOFactory;
import it.simonetagliaferri.model.dao.fs.FSDAOFactory;
import it.simonetagliaferri.model.dao.jdbc.JDBCDAOFactory;
import it.simonetagliaferri.utils.PropertiesUtils;


import static it.simonetagliaferri.utils.PropertiesUtils.*;

/**
 * Abstract factory to get the right DAO factory based on config files.
 */
public abstract class DAOFactory {

    protected DAOFactory() {
    }

    /**
     * Returns the correct DAOFactory based on current persistence.properties configuration.
     */
    public static DAOFactory getDAOFactory() {
        PersistenceProvider provider = PropertiesUtils.loadProperty(PERSISTENCE_PROPERTIES, PERSISTENCE_KEY, PersistenceProvider.class);
        return getDAOFactoryFromProvider(provider);
    }

    public static DAOFactory getDAOFactory(PersistenceProvider provider) {
        return getDAOFactoryFromProvider(provider);
    }

    private static DAOFactory getDAOFactoryFromProvider(PersistenceProvider provider) {
        switch (provider) {
            case FS:
                return new FSDAOFactory();
            case JDBC:
                return new JDBCDAOFactory();
            case IN_MEMORY:
            default:
                return new InMemoryDAOFactory();
        }
    }

    public abstract LoginDAO getLoginDAO();
    public abstract TournamentDAO getTournamentDAO();
    public abstract HostDAO getHostDAO();
    public abstract PlayerDAO getPlayerDAO();
    public abstract ClubDAO getClubDAO();
}
