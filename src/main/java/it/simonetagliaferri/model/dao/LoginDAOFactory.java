package it.simonetagliaferri.model.dao;

public class LoginDAOFactory {
    private static LoginDAOFactory instance = new LoginDAOFactory();

    private Class<? extends LoginDAO> loginDaoImplClazz;

    public static LoginDAOFactory getInstance() { return instance; }

    private LoginDAOFactory() {}

    public void setLoginDaoImpl(Class<? extends LoginDAO> loginDaoClazz) {
        loginDaoImplClazz = loginDaoClazz;
    }

    public LoginDAO getLoginDAO() {
        if (loginDaoImplClazz == InMemoryLoginDAO.class) {
            System.out.println("In Memory LoginDAO");
            return InMemoryLoginDAO.getInstance();
        }
        return null;
    }
}
