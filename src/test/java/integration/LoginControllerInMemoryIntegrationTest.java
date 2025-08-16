package integration;

import it.simonetagliaferri.controller.logic.LoginApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.navigation.UIMode;
import it.simonetagliaferri.model.dao.PersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

@Tag("in-memory")
class LoginControllerInMemoryIntegrationTest extends LoginControllerIntegrationTest {

    @BeforeEach
    void setup() {
        AppContext appContext = new AppContext(PersistenceProvider.IN_MEMORY, UIMode.CLI);
        this.controller = new LoginApplicationController(appContext.getSessionManager(),
                appContext.getDAOFactory().getLoginDAO(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO());
    }

}
