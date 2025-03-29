package it.simonetagliaferri.controller.graphic.config;

import it.simonetagliaferri.controller.graphic.cli.GraphicStartControllerCLI;

public class UIConfigurator {
    public static void configure(UIMode ui) {
        switch (ui) {
            case CLI:
                new GraphicStartControllerCLI().start();
                break;
            case GUI:
                break;
            default:
                throw new IllegalArgumentException("Unsupported UI mode: " + ui);
        }
    }
}
