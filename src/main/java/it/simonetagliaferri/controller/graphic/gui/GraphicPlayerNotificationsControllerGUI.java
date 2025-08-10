package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HandleNotificationsLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class GraphicPlayerNotificationsControllerGUI extends GraphicController implements GUIController{

    HandleNotificationsLogicController controller;
    @FXML private VBox notificationList;

    @Override
    public void initializeController(AppContext appContext) {
        this.controller = new HandleNotificationsLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getHostDAO());
        getNotifications();
    }

    private void getNotifications() {
        List<TournamentBean> newTournaments = this.controller.getPlayerNotifications();
        for (TournamentBean tournament : newTournaments) {
            showTournament(tournament);
        }
    }

    private void showTournament(TournamentBean tournament) {
        Label label = new Label("New tournament: " + tournament.getTournamentName() + "added by " + tournament.getClub().getName());
        notificationList.getChildren().add(label);
    }
}
