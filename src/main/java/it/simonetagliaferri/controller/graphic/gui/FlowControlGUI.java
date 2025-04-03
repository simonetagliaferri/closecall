package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.controller.graphic.FlowControl;
import javafx.application.Application;

public class FlowControlGUI implements FlowControl {
    public void startApp() {
        Application.launch(GraphicLoginControllerGUI.class);
    }
}
