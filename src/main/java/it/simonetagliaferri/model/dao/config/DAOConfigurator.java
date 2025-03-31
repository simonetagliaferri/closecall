package it.simonetagliaferri.model.dao.config;

public class DAOConfigurator {
    public static void configure(PersistenceProvider provider) {
        DAOConfiguratorStrategy strategy;
        switch (provider) {
            case IN_MEMORY:
               strategy = new InMemoryDAOConfigurator();
                break;
            case FS:
                strategy = new FSDAOConfigurator();
                break;
            case JDBC:
                strategy = new JDBCDAOConfigurator();
                break;
            default:
                throw new IllegalArgumentException("Unsupported persistence mode: " + provider);
        }
        strategy.configureAllDAOs();
    }
}
