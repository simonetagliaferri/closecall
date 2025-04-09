package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.dao.demo.InMemoryLoginDAO;
import it.simonetagliaferri.model.dao.fs.FsLoginDAO;
import it.simonetagliaferri.model.dao.jdbc.JDBCLoginDAO;

public class LoginDAOFactory extends DAOFactory<LoginDAO> {

    private LoginDAOFactory() {
    }

    private static class LazyHolder {
        private static final LoginDAOFactory instance = new LoginDAOFactory();
    }

    public static LoginDAOFactory getInstance() {
        return LazyHolder.instance;
    }

    @Override
    public LoginDAO getDAO() {
        Class<? extends LoginDAO> impl = getImplClass();
        if (impl == null) {
            throw new IllegalStateException("LoginDAO implementation not set.");
        }
        if (impl == InMemoryLoginDAO.class) {
            return InMemoryLoginDAO.getInstance();
        } else if (impl == FsLoginDAO.class) {
            return new FsLoginDAO();
        } else if (impl == JDBCLoginDAO.class) {
            return new JDBCLoginDAO();
        }
        throw new IllegalArgumentException("Unsupported LoginDAO implementation: " + impl.getName());
    }
}
