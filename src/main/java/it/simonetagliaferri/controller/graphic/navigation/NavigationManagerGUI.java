package it.simonetagliaferri.controller.graphic.navigation;

import it.simonetagliaferri.controller.graphic.gui.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Application;

import java.io.IOException;

public class NavigationManagerGUI extends NavigationManager {
    private final SceneManagerGUI sceneManagerGUI = new SceneManagerGUI();

    /*
        Implemented a SceneManager class to handle FXML loading and JavaFX launching.
     */
    public void start() {
        Application.launch(SceneManagerGUI.class);
    }
    // Added a login operation for readability on logout calls.
    public void login() {
        try {
            sceneManagerGUI.setRoot("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToDashboard(Role role) {
        try {
            if (role == Role.HOST) {
                sceneManagerGUI.setRoot("hostDashboard");
            } else sceneManagerGUI.setRoot("playerDashboard");
        } catch (IOException e) {
        throw new RuntimeException(e);}
    }

    public void goToAddTournament() {

    }
}
