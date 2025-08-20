package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.utils.CliUtils;
import it.simonetagliaferri.utils.PropertiesUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DB_PROPERTIES = "src/main/resources/properties/db.properties";
    private static Connection connection;

    private static volatile Connection overrideForTests;
    public static void setTestConnection(Connection c) { overrideForTests = c; }
    public static void clearTestConnection() { overrideForTests = null; }

    private ConnectionFactory() {
    }

    /**
     * Used to get establish a connection with the database.
     * The returned Connection is a Singleton, there is no need to open/close multiple connections
     * towards the database in one instance of the application.
     */
    public static Connection getConnection() {
        if (overrideForTests != null) return overrideForTests;
        // Lazy initialization, no need to establish a connection if JDBC is not the chosen persistence provider.
        if (connection == null) {
            try {
                String connectionUrl = PropertiesUtils.readProperty(DB_PROPERTIES, "CONNECTION_URL");
                String username = PropertiesUtils.readProperty(DB_PROPERTIES, "USER_USER");
                String password = PropertiesUtils.readProperty(DB_PROPERTIES, "USER_PASS");
                connection = DriverManager.getConnection(connectionUrl, username, password);
            } catch (IOException e) {
                CliUtils.println("Error in reading database info: " + e.getMessage());
            } catch (SQLException e) {
                CliUtils.println("SQLException: " + e.getMessage());
            }
        }
        return connection;
    }
}
