package it.simonetagliaferri;

import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManagerFactory;
import it.simonetagliaferri.model.dao.DAOFactory;

public class AppContext {
    private final NavigationManager navigationManager;
    private final SessionManager sessionManager;
    private final DAOFactory daoFactory;
    public AppContext() {
        navigationManager=new NavigationManagerFactory().getNavigationManager(this);
        sessionManager=new SessionManager();
        daoFactory=DAOFactory.getDAOFactory();
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
