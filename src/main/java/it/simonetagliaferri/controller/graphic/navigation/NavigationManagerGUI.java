package it.simonetagliaferri.controller.graphic.navigation;

import it.simonetagliaferri.controller.graphic.gui.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Application;

import java.io.IOException;

public class NavigationManagerGUI extends NavigationManager {

    public void start() {
        Application.launch(SceneManagerGUI.class);
    }

    public void login() throws IOException {
        SceneManagerGUI.setRoot("start");
    }

    public void goToDashboard(Role role) throws IOException {
        if (role == Role.HOST) {
            SceneManagerGUI.setRoot("hostDashboard");
        } else SceneManagerGUI.setRoot("playerDashboard");
    }

    public void goToAddTournament() {

    }
}
