package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HandleNotificationsApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class GraphicPlayerNotificationsControllerGUI extends GraphicController implements GUIController{

    HandleNotificationsApplicationController controller;
    @FXML private VBox notificationList;

    @Override
    public void initializeController(AppContext appContext) {
        this.controller = new HandleNotificationsApplicationController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getHostDAO());
        getNotifications();
    }

    private void getNotifications() {
        List<TournamentBean> newTournaments = this.controller.getPlayerNotifications();
        if (newTournaments.isEmpty()) {
            noNotifications();
        }
        for (TournamentBean tournament : newTournaments) {
            showTournament(tournament);
        }
        this.controller.clearPlayerNotifications();
    }

    private void showTournament(TournamentBean tournament) {
        Label label = new Label("New tournament: " + tournament.getTournamentName() + " added by " + tournament.getClub().getName());
        notificationList.getChildren().add(label);
    }

    private void noNotifications() {
        Label noNotifications = new Label("There are no new notifications for you right now.");
        notificationList.getChildren().add(noNotifications);
    }
}
