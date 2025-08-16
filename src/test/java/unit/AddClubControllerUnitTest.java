package unit;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.controller.logic.AddClubApplicationController;
import it.simonetagliaferri.exception.ClubAlreadyAddedException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AddClubControllerUnitTest {

    private AddClubApplicationController controller;

    @BeforeEach
    void setup() {
        User user = new User("Test", "Test", Role.HOST);
        Host host = new Host("Test", "Test");
        FakeSessionManager sessionManager = new FakeSessionManager();
        sessionManager.setCurrentUser(user);
        FakeHostDAO hostDAO = new FakeHostDAO();
        hostDAO.saveHost(host);
        FakeClubDAO clubDAO = new FakeClubDAO();
        controller = new AddClubApplicationController(sessionManager, clubDAO, hostDAO);
    }

    @Test
    void addClubSucceeds() {
        ClubBean bean = new ClubBean();
        bean.setName("TestClub");
        assertTrue(controller.addClub(bean));
    }

    @Test
    void addTwoClubsFails() {
        ClubBean bean = new ClubBean();
        bean.setName("TestClub");
        controller.addClub(bean);
        ClubBean bean2 = new ClubBean();
        bean2.setName("TestClub2");
        assertThrows(ClubAlreadyAddedException.class, () -> controller.addClub(bean2));
    }

    private static final class FakeSessionManager extends SessionManager {
        // Created just to point out that it's a mock class.
    }

    private static final class FakeHostDAO implements HostDAO {

        private final Map<String, Host> hosts = new HashMap<>();

        @Override
        public Host getHostByUsername(String username) {
            return hosts.get(username);
        }

        @Override
        public void saveHost(Host host) {
            hosts.put(host.getUsername(), host);
        }
    }

    private static final class FakeClubDAO implements ClubDAO {

        private final Map<String, Club> clubs = new HashMap<>();

        @Override
        public Club getClubByHostName(String hostName) {
            return clubs.get(hostName);
        }

        @Override
        public void saveClub(Club club) {
            String hostName = club.getOwner().getUsername();
            clubs.put(hostName, club);
        }
    }
}
