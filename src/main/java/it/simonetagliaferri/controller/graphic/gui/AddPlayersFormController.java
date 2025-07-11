package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.controller.graphic.GraphicController;
import javafx.fxml.FXML;

public class AddPlayersFormController extends GraphicController {

    GraphicAddTournamentControllerGUI parentController;

    @FXML
    public void setParentController(GraphicAddTournamentControllerGUI parentController) {
        this.parentController = parentController;
    }

    private void initialize() {

    }
}
