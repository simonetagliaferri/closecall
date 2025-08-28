package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.PlayerDashboardApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class GraphicPlayerHomeControllerGUI extends GraphicController implements GUIController {

    @FXML
    private Accordion tournamentsAccordion;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        PlayerDashboardApplicationController controller = new PlayerDashboardApplicationController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO());
        postInit(controller.getMyTournaments());
    }

    protected void postInit(List<TournamentBean> tournamentBeans) {
        GraphicTournamentDetails tournamentDetails = new GraphicTournamentDetails();
        for (TournamentBean t : tournamentBeans) {
            TitledPane pane = new TitledPane();
            pane.setAlignment(Pos.CENTER);
            pane.setText(t.getTournamentName());
            VBox vbox = new VBox();
            vbox.getChildren().addAll(tournamentDetails.getTournamentDetails(t));
            vbox.getChildren().addAll(tournamentDetails.getConfirmedTeams(t));
            pane.setContent(vbox);
            tournamentsAccordion.getPanes().add(pane);
        }
    }

}
