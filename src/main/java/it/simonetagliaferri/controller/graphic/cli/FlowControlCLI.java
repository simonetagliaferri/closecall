package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.controller.graphic.FlowControl;

public class FlowControlCLI implements FlowControl {
    public void startApp() {
        new GraphicLoginControllerCLI().start();
    }
}
