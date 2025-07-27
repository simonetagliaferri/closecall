package it.simonetagliaferri.infrastructure;

import it.simonetagliaferri.infrastructure.navigation.NavigationManager;
import it.simonetagliaferri.infrastructure.navigation.NavigationManagerFactory;
import it.simonetagliaferri.model.dao.DAOFactory;

/**
 * The context needed for the application consists of the navigation manager, the session manager and the dao factory.
 * They will be instantiated by the AppContext constructor, then the context will be passed around for other classes to retrieve
 * what they need through its getter methods.
 */
public class AppContext {

    private final NavigationManager navigationManager;
    private final SessionManager sessionManager;
    private final DAOFactory daoFactory;

    /**
     * The constructor instantiates a new NavigationManagerFactory, it calls its getNavigationManager,
     * to which it passes itself(the context) to get the correct NavigationManager instance.
     * It instantiates a new SessionManager.
     * It calls DAOFactory's(abstract class) static getDAOFactory method to get the correct DAOFactory instance.
     */
    public AppContext() {
        navigationManager = NavigationManagerFactory.getNavigationManager(this);
        sessionManager = new SessionManager();
        daoFactory = DAOFactory.getDAOFactory();
    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public DAOFactory getDAOFactory() {
        return daoFactory;
    }

}
