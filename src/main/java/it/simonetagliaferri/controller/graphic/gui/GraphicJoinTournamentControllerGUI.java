package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.JoinTournamentLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class GraphicJoinTournamentControllerGUI extends GraphicController implements GUIController {

    private JoinTournamentLogicController controller;
    @FXML private Button searchButton;
    @FXML private TextField searchField;
    @FXML private Accordion tournamentList;
    List<TitledPane> tournaments;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        FontIcon icon = new FontIcon("oct-search-16");
        icon.setIconSize(24);
        searchButton.setGraphic(icon);
        Platform.runLater(() -> this.controller = new JoinTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO(), appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO()));
    }

    @FXML private void searchTournaments() {
        tournamentList.getPanes().clear();
        List<TournamentBean> tournamentBeans = this.controller.searchTournament(searchField.getText());
        for (TournamentBean tournamentBean : tournamentBeans) {
            if (tournaments == null) tournaments = new ArrayList<>();
            tournaments.clear();
            TitledPane tournament = new TitledPane();
            tournament.setText("Tournament " + tournamentBean.getTournamentName() + " hosted by " + tournamentBean.getClub().getName());
            tournamentList.getPanes().add(tournament);
        }
    }

}
