package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GraphicAddTournamentControllerGUI extends GraphicController {

    @FXML private VBox addTournamentContent;


    @FXML
    private void initialize() {
        loadAddTournamentForm();
    }

    // Need to revise how the root changes
    @FXML
    public void loadAddTournamentForm() {
        try {
            FXMLLoader loader = SceneManagerGUI.getLoader("addTournament");
            Node node = SceneManagerGUI.getRoot(loader);
            AddTournamentFormController controller = SceneManagerGUI.getController(loader);
            controller.setParentController(this); // inject wrapper controller
            addTournamentContent.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void loadAddPlayersForm() {
        try {
            FXMLLoader loader = SceneManagerGUI.getLoader("addPlayers");
            Node node = SceneManagerGUI.getRoot(loader);
            AddTournamentFormController controller = SceneManagerGUI.getController(loader);
            controller.setParentController(this); // inject wrapper controller
            addTournamentContent.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
