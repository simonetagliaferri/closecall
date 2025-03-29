package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.domain.User;

public class LoginController {
    LoginDAO loginDAO = LoginDAOFactory.getInstance().getLoginDAO();
    public LoginController() {}
    public LoginResponseBean login(UserBean bean) {
        User user = new User(bean.getUsername(), bean.getPassword(), null);
        user=loginDAO.findByUsername(user);
        if(user!=null) {
            return new LoginResponseBean(LoginResult.SUCCESS, user.getRole());
        }
        return new LoginResponseBean(LoginResult.FAIL);
    }
    public LoginResponseBean signup(UserBean bean) {
        User user = new User(bean.getUsername(), bean.getEmail(), bean.getPassword(), bean.getRole());
        User loginTry;
        loginTry=loginDAO.findByUsername(user);
        if(loginTry!=null) {
            return new LoginResponseBean(LoginResult.FAIL);
        }
        loginDAO.signup(user);
        return new LoginResponseBean(LoginResult.SUCCESS);
    }
}
