package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.ManageTournamentsLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.utils.CliUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;

public class GraphicHostHomeControllerGUI extends GraphicController implements GUIController {

    private ManageTournamentsLogicController controller;

    @FXML private VBox tournaments;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new ManageTournamentsLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    @FXML
    public void initialize() {

    }

    private void postInit() {
        List<TournamentBean> tournamentBeans = this.controller.getTournaments();
        for (TournamentBean tournamentBean : tournamentBeans) {
            Button button = new Button(tournamentBean.getTournamentName());
            button.setOnAction(event -> showDetails(tournamentBean));
            tournaments.getChildren().add(button);
        }
    }

    private void showDetails(TournamentBean tournamentBean) {
        CliUtils.println("Tournament: " + tournamentBean.getTournamentName());
    }



}
