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

    private ConnectionFactory() {
    }

    // Lazy initialization, no need to establish a connection if JDBC is not the chosen persistence provider.
    public static Connection getConnection() {
        if (connection == null) {
            try {
                String connectionUrl = PropertiesUtils.readProperty(DB_PROPERTIES, "CONNECTION_URL");
                String username = PropertiesUtils.readProperty(DB_PROPERTIES, "LOGIN_USER");
                String password = PropertiesUtils.readProperty(DB_PROPERTIES, "LOGIN_PASS");
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
