package it.simonetagliaferri.model.dao;

public enum PersistenceProvider {
    IN_MEMORY,
    JDBC,
    FS;
    @Override
    public String toString() {
        switch (this) {
            case IN_MEMORY:
                return "In memory";
            case JDBC:
                return "JDBC";
            case FS:
                return "FS";
            default:
                throw new AssertionError();
        }
    }
}
