package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.LoginDAOFactory;
import it.simonetagliaferri.model.domain.User;

public class LoginController {
    LoginDAO loginDAO = LoginDAOFactory.getInstance().getLoginDAO();
    public LoginController() {}
    public int login(UserBean bean) {
        User user = new User(bean.getUsername(), bean.getPassword(), null);
        user=loginDAO.login(user);
        if(user!=null) {
            return 1;
        }
        return 0;
    }
    public int signup(UserBean bean) {
        User user = new User(bean.getUsername(), bean.getEmail(), bean.getPassword(), bean.getRole());
        user=loginDAO.signup(user);
        if(user!=null) {
            return 1;
        }
        return 0;
    }
}
