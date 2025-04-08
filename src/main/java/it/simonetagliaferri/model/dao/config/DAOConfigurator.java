package it.simonetagliaferri.model.dao.config;


public final class DAOConfigurator {

    private DAOConfigurator() {}

    public static void configureDAOs(PersistenceProvider provider) {
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


    /*
    Alternative version that respects the open-closed principle, it could be the right solution in prospect of
    turning this code into a library because this way it could be possible to add more supported persistence layers
    without modifying the source code of the library.
    For now, I decided to keep the switch version since it's easier to understand and, given the scope of the project,
    for which I know there won't be more persistence layers to be supported I think it's the better choice.
     */

    /*
    import static it.simonetagliaferri.model.dao.config.PersistenceProvider.*;
    import java.util.Map;

    public static void configureDAOs(PersistenceProvider provider) {
        DAOConfiguratorStrategy strategy;
        Map<PersistenceProvider, DAOConfiguratorStrategy> strategyMap = Map.of(
                IN_MEMORY, new InMemoryDAOConfigurator(),
                FS, new FSDAOConfigurator(),
                JDBC, new JDBCDAOConfigurator());
        strategy = strategyMap.get(provider);
        strategy.configureAllDAOs();
    }
     */
}
