package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDAOFactory extends DAOFactory {

    // Storing here the hashmaps, that will be passed to the DAOs constructors, so that consecutive calls reference the same memory area.
    private final Map<String, User> users = new HashMap<>();

    public InMemoryDAOFactory() {}

    @Override
    public LoginDAO getLoginDAO() {
        return new InMemoryLoginDAO(users);
    }
}
