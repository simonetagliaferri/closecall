package unit;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.logic.LoginApplicationController;
import it.simonetagliaferri.exception.DuplicateUserException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginControllerUnitTest {

    LoginApplicationController controller;

    @BeforeEach
    void setup() {
        FakeSessionManager sessionManager = new FakeSessionManager();
        FakeLoginDAO loginDAO = new FakeLoginDAO();
        FakeHostDAO hostDAO = new FakeHostDAO();
        FakePlayerDAO playerDAO = new FakePlayerDAO();
        controller = new LoginApplicationController(sessionManager, loginDAO, hostDAO, playerDAO);
    }

    @Test
    void hostSignupAndLoginSucceeds() {
        UserBean u = mkHost();
        controller.signup(u);
        assertTrue(controller.login(u));
    }

    @Test
    void duplicateUserThrowsException() {
        UserBean u = mkHost();
        controller.signup(u);
        assertThrows(DuplicateUserException.class, () -> controller.signup(u));
    }

    @Test
    void wrongPasswordLoginFail() {
        UserBean u = mkHost();
        UserBean u2 = mkHostWithWrongPassword();
        controller.signup(u);
        assertFalse(controller.login(u2));
    }

    @Test
    void playerSignupAndLoginSucceeds() {
        UserBean u = mkPlayer();
        controller.signup(u);
        assertTrue(controller.login(u));
    }

    private static UserBean mkHost() {
        UserBean u = new UserBean();
        u.setUsername("testUsername");
        u.setPassword("testPassword","testPassword");
        u.setEmail("testEmail@test.com");
        u.setRole("HOST");
        return u;
    }

    private static UserBean mkHostWithWrongPassword() {
        UserBean u = new UserBean();
        u.setUsername("testUsername");
        u.setPassword("wrongTestPassword","wrongTestPassword");
        u.setEmail("testEmail@test.com");
        u.setRole("HOST");
        return u;
    }

    private static UserBean mkPlayer() {
        UserBean u = new UserBean();
        u.setUsername("testUsername");
        u.setPassword("testPassword","testPassword");
        u.setEmail("testEmail@test.com");
        u.setRole("PLAYER");
        return u;
    }

    private static final class FakeSessionManager extends SessionManager {
        // Created just to point out that it's a mock class.
    }

    private static final class FakeLoginDAO implements LoginDAO {

        Map<String, User> users = new HashMap<>();

        @Override
        public User findByUsername(String username) {
            return users.get(username);
        }

        @Override
        public void signup(User user) {
            users.put(user.getUsername(), user);
        }

        @Override
        public User findByEmail(String email) {
            for (User user : users.values()) {
                if (user.getEmail().equals(email)) {
                    return user;
                }
            }
            return null;
        }
    }

    private static final class FakeHostDAO implements HostDAO {

        Map<String, Host> hosts = new HashMap<>();

        @Override
        public Host getHostByUsername(String username) {
            return hosts.get(username);
        }

        @Override
        public void saveHost(Host host) {
            hosts.put(host.getUsername(), host);
        }
    }

    private static final class FakePlayerDAO implements PlayerDAO {

        Map<String, Player> players = new HashMap<>();

        @Override
        public Player findByUsername(String username) {
            return players.get(username);
        }

        @Override
        public Player findByEmail(String email) {
            for (Player player : players.values()) {
                if (player.getEmail().equals(email)) {
                    return player;
                }
            }
            return null;
        }

        @Override
        public void savePlayer(Player player) {
            players.put(player.getUsername(), player);
        }
    }

}
