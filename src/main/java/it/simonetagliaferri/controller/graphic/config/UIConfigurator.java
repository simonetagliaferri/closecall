package it.simonetagliaferri.controller.graphic.config;

import it.simonetagliaferri.controller.graphic.gui.GraphicLoginControllerGUI;
import it.simonetagliaferri.controller.graphic.cli.GraphicLoginControllerCLI;
import javafx.application.Application;

public class UIConfigurator {
    public static void configure(UIMode ui) {
        switch (ui) {
            case CLI:
                new GraphicLoginControllerCLI().start();
                break;
            case GUI:
                Application.launch(GraphicLoginControllerGUI.class);
                break;
            default:
                throw new IllegalArgumentException("Unsupported UI mode: " + ui);
        }
    }
}
