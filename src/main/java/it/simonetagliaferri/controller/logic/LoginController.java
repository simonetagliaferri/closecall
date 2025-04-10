package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.PasswordUtils;

import java.io.IOException;

public class LoginController {
    LoginDAO loginDAO;
    private User user;
    private final SessionManager sessionManager = NavigationManager.getInstance().getSessionManager();

    //The correct DAO is set on initialization.
    public LoginController() {
        try {
            loginDAO = DAOFactory.getDAOFactory().getLoginDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        It tries to get a user from the DAO passing to it the provided username, it hashes the provided password,
        then it checks that the user exists and then that the provided password matches.
        If the checks are successful a new UserBean is instantiated with retrieved role and the session current user is set.
        A login response bean is used to communicate the outcome of the login process to the graphic controller.
     */
    public LoginResponseBean login(UserBean bean) {
        user = loginDAO.findByUsername(bean.getUsername());
        String hashedPass = PasswordUtils.sha256Hex(bean.getPassword());
        if (user != null && user.getPassword().equals(hashedPass)) {
            // Creating new bean that has no reference to the password, no need to keep that around.
            UserBean currentUser = new UserBean(user.getUsername(), user.getRole());
            sessionManager.setCurrentUser(currentUser);
            return new LoginResponseBean(LoginResult.SUCCESS);
        } else return new LoginResponseBean(LoginResult.FAIL);
    }

    /*
        It creates a user from the bean and then passes it to loginDAO to sign the user up.
    */
    public LoginResponseBean signup(UserBean bean) {
        user = new User(bean.getUsername(), bean.getEmail(), PasswordUtils.sha256Hex(bean.getPassword()), bean.getRole());
        if (loginDAO.signup(user) != null) {
            return new LoginResponseBean(LoginResult.SUCCESS);
        } else {
            return new LoginResponseBean(LoginResult.FAIL);
        }
    }

    /*
        Used before signup to check if the provided username is already in use.
     */
    public boolean userLookUp(UserBean bean) {
        user = loginDAO.findByUsername(bean.getUsername());
        return user != null;
    }

    public boolean emailLookUp(UserBean bean) {
        user = loginDAO.findByEmail(bean.getEmail());
        return user != null;
    }

}
