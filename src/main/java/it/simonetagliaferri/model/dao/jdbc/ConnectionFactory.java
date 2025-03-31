package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.utils.CliUtils;
import it.simonetagliaferri.utils.PropertiesUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String DB_PROPERTIES = "src/main/resources/properties/db.properties";
    private static Connection connection;
    private ConnectionFactory() {}
    //Static block so that only one connection is opened in a run.
    static {
        // Getting the persistence layer to use in this run.
        try {
            String connection_url = PropertiesUtils.readProperty(DB_PROPERTIES, "CONNECTION_URL");
            String username = PropertiesUtils.readProperty(DB_PROPERTIES, "LOGIN_USER");
            String password = PropertiesUtils.readProperty(DB_PROPERTIES, "LOGIN_PASS");
            connection = DriverManager.getConnection(connection_url, username, password);
        } catch (IOException e) {
            CliUtils.println("Error in reading database info: " + e.getMessage());
        } catch (SQLException e) {
            CliUtils.println("SQLExceptio: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static void changeRole(Role role) throws SQLException {

        connection.close();

        try {
            String connection_url = PropertiesUtils.readProperty(DB_PROPERTIES, "CONNECTION_URL");
            String username = PropertiesUtils.readProperty(DB_PROPERTIES, role.name() + "_USER");
            String password = PropertiesUtils.readProperty(DB_PROPERTIES, role.name() + "_PASS");

            connection = DriverManager.getConnection(connection_url, username, password);
        }
        catch (IOException | SQLException e) {
            CliUtils.println("IO or SQL exception:" + e.getMessage());
        }

    }
}
