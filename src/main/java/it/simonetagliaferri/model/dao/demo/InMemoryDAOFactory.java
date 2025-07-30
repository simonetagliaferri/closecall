package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.model.invite.Invite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDAOFactory extends DAOFactory {

    // Storing here the hashmaps, that will be passed to the DAOs constructors, so that consecutive calls reference the same memory area.
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, List<Tournament>> tournaments = new HashMap<>();
    private final Map<String, List<Club>> clubs = new HashMap<>();
    private final Map<String, Host> hosts = new HashMap<>();
    private final Map<String, Player> players = new HashMap<>();
    private final Map<String, List<Invite>> invites = new HashMap<>();

    private LoginDAO loginDAO;
    private TournamentDAO tournamentDAO;
    private HostDAO hostDAO;
    private PlayerDAO playerDAO;
    private ClubDAO clubDAO;
    private InviteDAO inviteDAO;

    public InMemoryDAOFactory() {
        populate();
    }

    // Just for tests
    public void populate() {
        User marco = new User("marco", "marco@gmail.com", "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", Role.HOST); //a
        User p1 = new User("p1", "p1@gmail.com", "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", Role.PLAYER); //a
        User p2 = new User("p2", "p2@gmail.com", "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", Role.PLAYER); //a
        users.put(marco.getUsername(), marco);
        users.put(p1.getUsername(), p1);
        users.put(p2.getUsername(), p2);
        hosts.put(marco.getUsername(), new Host(marco.getUsername(), marco.getEmail()));
        HostDAO hostDAO1 = getHostDAO();
        Host host = hostDAO1.getHostByUsername(marco.getUsername());
        players.put(p1.getUsername(), new Player(p1.getUsername(), p1.getEmail()));
        players.put(p2.getUsername(), new Player(p2.getUsername(), p2.getEmail()));
//        Club sanP = new Club("SanP", host);
//        sanP.updateAddress("a", "a", "a", "a", "a", "a");
//        sanP.updateContacts("a", "a");
//        ClubDAO clubDAO1 = getClubDAO();
//        clubDAO1.saveClub(sanP);
    }

    @Override
    public LoginDAO getLoginDAO() {
        if (loginDAO == null) { loginDAO = new InMemoryLoginDAO(users);}
        return loginDAO;
    }

    @Override
    public TournamentDAO getTournamentDAO() {
        if (tournamentDAO == null) { tournamentDAO = new InMemoryTournamentDAO(tournaments);}
        return tournamentDAO;
    }

    @Override
    public HostDAO getHostDAO() {
        if (hostDAO == null) { hostDAO = new InMemoryHostDAO(hosts);}
        return hostDAO;
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        if (playerDAO == null) { playerDAO = new InMemoryPlayerDAO(players);}
        return playerDAO;
    }

    @Override
    public ClubDAO getClubDAO() {
        if (clubDAO == null) { clubDAO = new InMemoryClubDAO(clubs);}
        return clubDAO;
    }

    @Override
    public InviteDAO getInviteDAO() {
        if (inviteDAO == null) { inviteDAO = new InMemoryInviteDAO(invites);}
        return inviteDAO;
    }
}
