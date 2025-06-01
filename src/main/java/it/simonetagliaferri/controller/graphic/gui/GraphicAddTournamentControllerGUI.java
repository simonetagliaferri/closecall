package it.simonetagliaferri.controller.graphic.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GraphicAddTournamentControllerGUI {

    @FXML private VBox addTournamentContent;


    @FXML
    private void initialize() {
        loadAddTournamentForm();
    }

    @FXML
    public void loadAddTournamentForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gui/addTournament.fxml"));
            Node node = loader.load();

            AddTournamentFormController controller = loader.getController();
            controller.setParentController(this); // inject wrapper controller

            addTournamentContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadAddPlayersForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gui/addPlayers.fxml"));
            Node node = loader.load();

            AddPlayersFormController controller = loader.getController();
            controller.setParentController(this); // inject wrapper controller

            addTournamentContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
