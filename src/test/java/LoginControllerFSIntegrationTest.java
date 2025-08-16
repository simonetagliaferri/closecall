import it.simonetagliaferri.controller.logic.LoginApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.navigation.UIMode;
import it.simonetagliaferri.model.dao.fs.FSDAOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;

@Tag("integration")
public class LoginControllerFSIntegrationTest extends LoginControllerIntegrationTest {

    @TempDir
    Path temp;

    @BeforeEach
    void setup() {
        FSDAOFactory fsDAOFactory = new FSDAOFactory(temp);
        AppContext appContext = new AppContext(fsDAOFactory, UIMode.CLI);
        this.controller = new LoginApplicationController(appContext.getSessionManager(),
                appContext.getDAOFactory().getLoginDAO(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO());
    }

}
