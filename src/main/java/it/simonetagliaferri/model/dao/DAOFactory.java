package it.simonetagliaferri.model.dao;

public abstract class DAOFactory<T> {

    // This will be the actual class that will implement the DAO.
    protected Class<? extends T> implClass;

    // Used to set the implementation class.
    public void setImplClass(Class<? extends T> implementationClass) {
        this.implClass = implementationClass;
    }

    // Returns the instance of the chosen implementation of the DAO.
    public abstract T getDAO();
}
