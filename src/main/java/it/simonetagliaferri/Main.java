package it.simonetagliaferri;

import it.simonetagliaferri.controller.graphic.config.UIConfigurator;
import it.simonetagliaferri.controller.graphic.config.UIMode;
import it.simonetagliaferri.model.dao.config.PersistenceProvider;
import it.simonetagliaferri.model.dao.config.DAOConfigurator;
import it.simonetagliaferri.utils.CliUtils;
import java.io.*;
import static it.simonetagliaferri.utils.PropertiesUtils.loadProperty;

public class Main {
    private static final String PERSISTENCE_PROPERTIES = "src/main/resources/properties/persistence.properties";
    private static final String UI_PROPERTIES = "src/main/resources/properties/ui.properties";
    private static final String PERSISTENCE_KEY = "PERSISTENCE_MODE";
    private static final String UI_KEY = "UI_MODE";
    public static void main(String[] args) {
        // PersistenceProvider is a ENUM of the available persistence layers.
        PersistenceProvider provider=null;
        // Getting the persistence layer to use in this run.
        try {
            provider = loadProperty(PERSISTENCE_PROPERTIES, PERSISTENCE_KEY, PersistenceProvider.class);
        } catch (IOException e) {
            CliUtils.println("Error in reading persistence mode: " + e.getMessage());
        }
        // Repeat the same thing as above, but for the UI.
        UIMode ui=null;
        try {
            ui = loadProperty(UI_PROPERTIES, UI_KEY, UIMode.class);
        } catch (IOException e) {
            CliUtils.println("Error in reading UI mode: " + e.getMessage());
        }
        if (provider == null) {
            // Using a utils class to have System.out code smells contained.
            CliUtils.println("Persistence mode not recognized");
            System.exit(1);
        }
        if (ui == null) {
            CliUtils.println("UI mode not recognized");
            System.exit(1);
        }
        //Calls to persistence and UI configurators.
        DAOConfigurator.configureDAOs(provider);
        UIConfigurator.configureUI(ui);
    }
}
