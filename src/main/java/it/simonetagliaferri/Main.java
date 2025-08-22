package it.simonetagliaferri;

import it.simonetagliaferri.infrastructure.AppContext;

public class Main {

    /**
     * The context of the app gets instantiated, the navigation manager gets
     * retrieved from the context itself and the start method of the navigation manager is called.
     */
    public static void main(String[] args) {
        AppContext context = new AppContext();
        context.startNavigation();
    }

}
