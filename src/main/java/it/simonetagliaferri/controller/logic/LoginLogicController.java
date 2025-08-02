package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.converters.UserMapper;

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
        then it checks that the user exists and then that the provided password matches with the user's password.
        If the checks are successful a new UserBean is instantiated with the retrieved role and the session's current user is set.
        A login response bean is used to communicate the outcome of the login process to the graphic controller.
     */
    public boolean login(UserBean bean) {
        String username = bean.getUsername();
        String password = bean.getPassword();
        User user = loginDAO.findByUsername(username);
        User currentUser;
        if (user != null && user.isPasswordCorrect(password)) {
            if (user.isHost()) {
                currentUser = new Host(user.getUsername(), user.getEmail(), user.getRole());

            } else {
                currentUser = new Player(user.getUsername(), user.getEmail(), user.getRole());
            }
            setCurrentUser(currentUser);
            return true;
        }
        return false;
    }

    /**
     * It creates a user from the bean and then passes it to loginDAO to sign the user up.
    */
    public void signup(UserBean bean) {
        User user = UserMapper.fromBean(bean); // It's okay to go from bean to model because this user doesn't exist yet.
        loginDAO.signup(user);
        if (user.isHost()) {
            Host host = new Host(user.getUsername(), user.getEmail());
            hostDAO.saveHost(host);
        }
        else {
            Player player = new Player(user.getUsername(), user.getEmail());
            playerDAO.savePlayer(player);
        }
    }

    /**
     * Used before signup to check if the provided username is already in use.
     */
    public boolean userLookUp(UserBean bean) {
        String username = bean.getUsername();
        return loginDAO.findByUsername(username) != null;
    }

    public boolean isUsernamenValid(UserBean bean) {
        User user = UserMapper.fromBean(bean);
        return user.isUsernameValid();
    }

    public boolean emailLookUp(UserBean bean) {
        String email = bean.getEmail();
        return loginDAO.findByEmail(email) != null;
    }

    protected void setCurrentUser(User user) {
        sessionManager.setCurrentUser(user);
    }

}
