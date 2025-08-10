package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.PlayerDashboardLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.utils.CliUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;

public class GraphicPlayerHomeControllerGUI extends GraphicController implements GUIController{
    private PlayerDashboardLogicController controller;

    @FXML
    private VBox tournaments;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new PlayerDashboardLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    private void postInit() {
        List<TournamentBean> tournamentBeans = this.controller.getMyTournaments();
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
