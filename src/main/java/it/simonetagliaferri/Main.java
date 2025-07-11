package it.simonetagliaferri;

public class Main {
    public static void main(String[] args) {
        // Starts the appropriate UI.
        AppContext context = new AppContext();
        context.getNavigationManager().start();
    }
}
