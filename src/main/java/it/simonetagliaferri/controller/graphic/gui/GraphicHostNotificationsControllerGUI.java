package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HandleNotificationsLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class GraphicHostNotificationsControllerGUI extends GraphicController implements GUIController {

    HandleNotificationsLogicController controller;
    @FXML private VBox notificationList;

    @Override
    public void initializeController(AppContext appContext) {
        this.controller = new HandleNotificationsLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getHostDAO());
        getNotifications();
    }

    private void getNotifications() {
        Map<TournamentBean, List<PlayerBean>> notifications = this.controller.getHostNotifications();
        if (notifications.isEmpty()) {
            noNotifications();
        }
        TournamentBean tournamentBean;
        List<PlayerBean> players;
        for (Map.Entry<TournamentBean, List<PlayerBean>> tournamentNotifications : notifications.entrySet()) {
            tournamentBean = tournamentNotifications.getKey();
            players = tournamentNotifications.getValue();
            showTournament(tournamentBean);
            for (PlayerBean playerBean : players) {
                showPlayer(playerBean);
            }
        }
        this.controller.clearHostNotifications();
    }

    private void showTournament(TournamentBean tournamentBean) {
        Label tournamentName = new Label("New players joined tournament: " + tournamentBean.getTournamentName());
        notificationList.getChildren().add(tournamentName);
    }

    private void showPlayer(PlayerBean playerBean) {
        Label playerName = new Label("\t" + playerBean.getUsername());
        notificationList.getChildren().add(playerName);
    }

    private void noNotifications() {
        Label noNotifications = new Label("There are no new notifications for you right now.");
        notificationList.getChildren().add(noNotifications);
    }
}
