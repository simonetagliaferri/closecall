package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.ManageTournamentsLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class GraphicHostHomeControllerGUI extends GraphicController implements GUIController {

    ManageTournamentsLogicController tournamentController;

    @FXML private VBox clubs;
    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.tournamentController = new ManageTournamentsLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    @FXML
    public void initialize() {

    }

    private void postInit() {

    }


}
