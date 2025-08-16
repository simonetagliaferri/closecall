import it.simonetagliaferri.controller.logic.LoginApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.navigation.UIMode;
import it.simonetagliaferri.model.dao.PersistenceProvider;
import it.simonetagliaferri.model.dao.jdbc.ConnectionFactory;
import it.simonetagliaferri.utils.PropertiesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Savepoint;

@Tag("integration")
public class LoginControllerJDBCIntegrationTest extends LoginControllerIntegrationTest {

    private Connection connection;
    private Savepoint savepoint;
    private final static String DB_PROPERTIES = "src/main/resources/properties/db.properties";

    @BeforeEach
    void open() throws Exception {
        String connectionUrl = PropertiesUtils.readProperty(DB_PROPERTIES, "CONNECTION_URL");
        String username = PropertiesUtils.readProperty(DB_PROPERTIES, "USER_USER");
        String password = PropertiesUtils.readProperty(DB_PROPERTIES, "USER_PASS");
        connection = DriverManager.getConnection(connectionUrl, username, password);
        connection.setAutoCommit(false);
        ConnectionFactory.setTestConnection(connection);
        savepoint = connection.setSavepoint();
        AppContext appContext = new AppContext(PersistenceProvider.JDBC, UIMode.CLI);
        this.controller = new LoginApplicationController(appContext.getSessionManager(),
                appContext.getDAOFactory().getLoginDAO(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO());
    }

    @AfterEach
    void close() throws Exception {
        connection.rollback(savepoint);
        connection.close();
        ConnectionFactory.clearTestConnection();
    }

}
