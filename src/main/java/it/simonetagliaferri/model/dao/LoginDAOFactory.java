package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.dao.demo.InMemoryLoginDAO;
import it.simonetagliaferri.model.dao.fs.FSLoginDAO;
import it.simonetagliaferri.model.dao.jdbc.JDBCLoginDAO;

public class LoginDAOFactory extends DAOFactory<LoginDAO> {

    private static final LoginDAOFactory loginDAOFactory = new LoginDAOFactory();
    private LoginDAOFactory() {}
    public static LoginDAOFactory getInstance() {
        return loginDAOFactory;
    }
    @Override
    public LoginDAO getDAO() {
        if (implClass == null) {
            throw new IllegalStateException("LoginDAO implementation not set.");
        }
        if (implClass == InMemoryLoginDAO.class) {
            return InMemoryLoginDAO.getInstance();
        } else if (implClass == FSLoginDAO.class) {
            return new FSLoginDAO();
        } else if (implClass == JDBCLoginDAO.class) {
            return new JDBCLoginDAO();
        }
        throw new IllegalArgumentException("Unsupported LoginDAO implementation: " + implClass.getName());
    }
}
