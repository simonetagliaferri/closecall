package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManagerFactory;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.PasswordUtils;

import java.io.IOException;

public class LoginController {
    LoginDAO loginDAO = LoginDAOFactory.getInstance().getDAO();
    NavigationManager navigationManager = NavigationManagerFactory.getInstance().getNavigationManager();
    private User user;

    public LoginController() {
    }

    public LoginResponseBean login(UserBean bean) {
        user = loginDAO.findByUsername(bean.getUsername());
        String hashedPass = PasswordUtils.sha256Hex(bean.getPassword());
        if (user != null && user.getPassword().equals(hashedPass)) {
            // Creating new bean that has no reference to the password
            UserBean currentUser = new UserBean(user.getUsername(), user.getEmail(), user.getRole());
            SessionManager.setCurrentUser(currentUser);
            try {
                navigationManager.goToDashboard(currentUser.getRole());
                return new LoginResponseBean(LoginResult.SUCCESS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else return new LoginResponseBean(LoginResult.FAIL);
    }

    public LoginResponseBean signup(UserBean bean) {
        user = new User(bean.getUsername(), bean.getEmail(), PasswordUtils.sha256Hex(bean.getPassword()), bean.getRole());
        if (loginDAO.signup(user)!=null) {
            return new LoginResponseBean(LoginResult.SUCCESS);
        }
        else { return new LoginResponseBean(LoginResult.FAIL); }
    }

    public boolean userLookUp(UserBean bean) {
        user = loginDAO.findByUsername(bean.getUsername());
        return user != null;
    }

    public void end() {
        System.exit(0);
    }

    public User getUser() {
        return user;
    }
}
