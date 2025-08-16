package integration;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.logic.LoginApplicationController;
import it.simonetagliaferri.exception.DuplicateUserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class LoginControllerIntegrationTest {

    protected LoginApplicationController controller;

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

    @Test
    void hostSignupAndLogin() {
        UserBean u = mkHost();
        controller.signup(u);
        assertTrue(controller.login(u));
    }

    @Test
    void duplicateUser() {
        UserBean u = mkHost();
        controller.signup(u);
        assertThrows(DuplicateUserException.class, () -> controller.signup(u));
    }

    @Test
    void wrongPassword() {
        UserBean u = mkHost();
        UserBean u2 = mkHostWithWrongPassword();
        controller.signup(u);
        assertFalse(controller.login(u2));
    }

    @Test
    void playerSignupAndLogin() {
        UserBean u = mkPlayer();
        controller.signup(u);
        assertTrue(controller.login(u));
    }
}
