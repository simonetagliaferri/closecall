package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.view.cli.StartCLIView;

import java.io.IOException;

public class GraphicStartControllerCLI {
    public void start() {
        int choice;
        StartCLIView startCLIView = new StartCLIView();
        try {
            choice = startCLIView.showMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (choice == 1) {
            new GraphicLoginControllerCLI().start();
        }
        else {
            startCLIView.end();
        }
    }
}
