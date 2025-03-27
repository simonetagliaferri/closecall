package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.controller.graphic.cli.GraphicStartControllerCLI;
import it.simonetagliaferri.model.dao.InMemoryLoginDAO;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.dao.PersistenceProvider;

public class StartController {
    LoginDAOFactory loginDAOFactory = LoginDAOFactory.getInstance();
    public void setPersistenceProvider(String provider) {
        System.out.println("Setting persistence provider to " + provider);
        if (provider.equals(PersistenceProvider.IN_MEMORY.toString())) {
            System.out.println("In memory PersistenceProvider");
            loginDAOFactory.setLoginDaoImpl(InMemoryLoginDAO.class);
        }
        else if (provider.equals(PersistenceProvider.JDBC.toString())) {
            System.out.println("JDBC PersistenceProvider");
        }
        else if (provider.equals(PersistenceProvider.FS.toString())) {
            System.out.println("FS PersistenceProvider");
        }
        else {
            System.out.println("Unknown PersistenceProvider");
            System.exit(1);
        }
    }
    public void start() {
    }
}
