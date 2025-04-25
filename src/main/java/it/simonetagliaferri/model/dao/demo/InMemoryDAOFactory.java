package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDAOFactory extends DAOFactory {

    // Storing here the hashmaps, that will be passed to the DAOs constructors, so that consecutive calls reference the same memory area.
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, List<Tournament>> tournaments = new HashMap<>();

    @Override
    public LoginDAO getLoginDAO() {
        return new InMemoryLoginDAO(users);
    }

    @Override
    public TournamentDAO getTournamentDAO() { return new InMemoryTournamentDAO(tournaments); }

    @Override
    public HostDAO getHostDAO() { return new InMemoryHostDAO(); }

    @Override
    public PlayerDAO getPlayerDAO() { return new InMemoryPlayerDAO(); }
}
