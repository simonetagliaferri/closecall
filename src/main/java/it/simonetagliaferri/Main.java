package it.simonetagliaferri;

import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;

public class Main {
    public static void main(String[] args) {
        // Starts the appropriate UI.
        NavigationManager.getInstance().start();
    }
}
