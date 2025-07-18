package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.PasswordUtils;

public class LoginLogicController extends LogicController {

    private final LoginDAO loginDAO;
    private final HostDAO hostDAO;
    private final PlayerDAO playerDAO;

    public LoginLogicController(SessionManager sessionManager, LoginDAO loginDAO, HostDAO hostDAO, PlayerDAO playerDAO) {
        super(sessionManager);
        this.loginDAO = loginDAO;
        this.hostDAO = hostDAO;
        this.playerDAO = playerDAO;
    }

    /**
        It tries to get a user from the DAO passing to it the provided username, it hashes the provided password,
        then it checks that the user exists and then that the provided password matches.
        If the checks are successful a new UserBean is instantiated with retrieved role and the session's current user is set.
        A login response bean is used to communicate the outcome of the login process to the graphic controller.
     */
    public LoginResponseBean login(UserBean bean) {
        User user = loginDAO.findByUsername(bean.getUsername());
        String hashedPass = PasswordUtils.sha256Hex(bean.getPassword());
        User currentUser;
        if (user != null && user.getPassword().equals(hashedPass)) {
            if (user.getRole() == Role.PLAYER) {
                currentUser = new Player(user.getUsername(), user.getEmail(), user.getRole());
            } else {
                currentUser = new Host(user.getUsername(), user.getEmail(), user.getRole());
            }
            setCurrentUser(currentUser);
            return new LoginResponseBean(LoginResult.SUCCESS);
        }
        else return new LoginResponseBean(LoginResult.FAIL);
    }

    /*
        It creates a user from the bean and then passes it to loginDAO to sign the user up.
    */
    public LoginResponseBean signup(UserBean bean) {
        User user = new User(bean.getUsername(), bean.getEmail(), PasswordUtils.sha256Hex(bean.getPassword()), bean.getRole());
        if (loginDAO.signup(user) != null) {
            if (user.getRole() == Role.HOST) {
                hostDAO.addHost(new Host(user.getUsername(), user.getEmail()));
            }
            else {
                playerDAO.addPlayer(new Player(user.getUsername(), user.getEmail()));
            }
            return new LoginResponseBean(LoginResult.SUCCESS);
        } else {
            return new LoginResponseBean(LoginResult.FAIL);
        }
    }

    /*
        Used before signup to check if the provided username is already in use.
     */
    public boolean userLookUp(UserBean bean) {
        User user = loginDAO.findByUsername(bean.getUsername());
        return user != null;
    }

    public boolean emailLookUp(UserBean bean) {
        User user = loginDAO.findByEmail(bean.getEmail());
        return user != null;
    }
}
