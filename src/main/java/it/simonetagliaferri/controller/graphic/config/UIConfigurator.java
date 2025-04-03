package it.simonetagliaferri.controller.graphic.config;

import it.simonetagliaferri.controller.graphic.cli.FlowControlCLI;
import it.simonetagliaferri.controller.graphic.gui.FlowControlGUI;

public class UIConfigurator {
    public static void configureUI(UIMode ui) {
        switch (ui) {
            case CLI:
                new FlowControlCLI().startApp();
                break;
            case GUI:
                new FlowControlGUI().startApp();
                break;
            default:
                throw new IllegalArgumentException("Unsupported UI mode: " + ui);
        }
    }
}
