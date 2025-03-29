package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.dao.demo.InMemoryLoginDAO;
import it.simonetagliaferri.model.dao.fs.FsLoginDAO;
import it.simonetagliaferri.model.dao.jdbc.JdbcLoginDAO;

public class LoginDAOFactory {
    private static final LoginDAOFactory instance = new LoginDAOFactory();

    private Class<? extends LoginDAO> loginDaoImplClazz;

    public static LoginDAOFactory getInstance() { return instance; }

    private LoginDAOFactory() {}

    public void setLoginDaoImpl(Class<? extends LoginDAO> loginDaoClazz) {
        loginDaoImplClazz = loginDaoClazz;
        System.out.println("Setting loginDAOImplClazz: " + loginDaoImplClazz);
    }

    public LoginDAO getLoginDAO() {
        if (loginDaoImplClazz == InMemoryLoginDAO.class) {
            System.out.println("In Memory LoginDAO");
            return InMemoryLoginDAO.getInstance();
        }
        if (loginDaoImplClazz == FsLoginDAO.class) {
            System.out.println("In Fs LoginDAO");
            return FsLoginDAO.getInstance();
        }
        if (loginDaoImplClazz == JdbcLoginDAO.class) {
            System.out.println("In JDBC LoginDAO");
            return JdbcLoginDAO.getInstance();
        }
        else System.out.println("Problem");
        return null;
    }
}
