package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.dao.demo.InMemoryLoginDAO;
import it.simonetagliaferri.model.dao.fs.FSLoginDAO;
import it.simonetagliaferri.model.dao.jdbc.JDBCLoginDAO;

public class LoginDAOFactory extends DAOFactory<LoginDAO> {

    private static LoginDAOFactory loginDAOFactory;

    public static LoginDAOFactory getInstance() {
        if (loginDAOFactory == null) {
            loginDAOFactory = new LoginDAOFactory();
        }
        return loginDAOFactory;
    }

    @Override
    public LoginDAO getDAO() {
        Class<? extends LoginDAO> impl = getImplClass();
        if (impl == null) {
            throw new IllegalStateException("LoginDAO implementation not set.");
        }
        if (impl == InMemoryLoginDAO.class) {
            return new InMemoryLoginDAO();
        } else if (impl == FSLoginDAO.class) {
            return new FSLoginDAO();
        } else if (impl == JDBCLoginDAO.class) {
            return new JDBCLoginDAO();
        }
        throw new IllegalArgumentException("Unsupported LoginDAO implementation: " + impl.getName());
    }
}
