package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.dao.demo.InMemoryDAOFactory;
import it.simonetagliaferri.model.dao.fs.FSDAOFactory;
import it.simonetagliaferri.model.dao.jdbc.JDBCDAOFactory;


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
        PersistenceProvider provider = loadProperty(PERSISTENCE_PROPERTIES, PERSISTENCE_KEY, PersistenceProvider.class);
        switch (provider) {
            case IN_MEMORY:
                return new InMemoryDAOFactory();
            case FS:
                return new FSDAOFactory();
            case JDBC:
                return new JDBCDAOFactory();
            default:
                throw new RuntimeException("Unknown persistence provider: " + provider);
        }
    }

    public abstract LoginDAO getLoginDAO();
    public abstract TournamentDAO getTournamentDAO();
    public abstract HostDAO getHostDAO();
    public abstract PlayerDAO getPlayerDAO();
    public abstract ClubDAO getClubDAO();
}
