package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.AddClubLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GraphicAddClubControllerGUI extends GraphicController implements GUIController{

    @FXML private VBox addClub;
    AddClubLogicController controller;

    @FXML private VBox firstClubWarning;
    @FXML private VBox clubDetails;
    @FXML private TextField clubName;
    @FXML private TextField clubStreet;
    @FXML private TextField clubNumber;
    @FXML private TextField clubCity;
    @FXML private TextField clubState;
    @FXML private TextField clubCountry;
    @FXML private TextField clubPhone;
    @FXML private TextField clubEmail;

    public void postInit() {
        if (this.controller.firstClub()) {
            firstClubWarning.setVisible(true);
        }
    }

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new AddClubLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getClubDAO(),
                appContext.getDAOFactory().getHostDAO());
        postInit();
    }

    @FXML
    public void initialize() {
    }


    @FXML
    public void logout() {
        // Go back to login screen
        navigationManager.login();
    }

    @FXML
    public void addClub() {
        clubDetails.setVisible(true);
        firstClubWarning.setVisible(false);
        addClub.getChildren().setAll(clubDetails);
    }

}
