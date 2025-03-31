package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.PasswordUtils;

public class LoginController {
    LoginDAO loginDAO = LoginDAOFactory.getInstance().getDAO();
    public LoginController() {}
    public LoginResponseBean login(UserBean bean) {
        User user=loginDAO.findByUsername(bean.getUsername());
        String hashedPass=PasswordUtils.sha256Hex(bean.getPassword());
        if(user!=null && user.getPassword().equals(hashedPass)) {
            return new LoginResponseBean(LoginResult.SUCCESS, user.getRole());
        }
        return new LoginResponseBean(LoginResult.FAIL);
    }
    public LoginResponseBean signup(UserBean bean) {
        User existingUser;
        existingUser=loginDAO.findByUsername(bean.getUsername());
        if(existingUser!=null) {
            return new LoginResponseBean(LoginResult.FAIL);
        }
        User user = new User(bean.getUsername(), bean.getEmail(), PasswordUtils.sha256Hex(bean.getPassword()), bean.getRole());
        loginDAO.signup(user);
        return new LoginResponseBean(LoginResult.SUCCESS);
    }

    public boolean userLookUp(UserBean bean) {
        User user=loginDAO.findByUsername(bean.getUsername());
        return user != null;
    }

    public void end() {
        System.exit(0);
    }
}
