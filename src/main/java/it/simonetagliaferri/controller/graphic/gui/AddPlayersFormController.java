package it.simonetagliaferri.controller.graphic.gui;

import javafx.fxml.FXML;

public class AddPlayersFormController {

    GraphicAddTournamentControllerGUI parentController;

    @FXML
    public void setParentController(GraphicAddTournamentControllerGUI parentController) {
        this.parentController = parentController;
    }
}
