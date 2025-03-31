package it.simonetagliaferri;

import it.simonetagliaferri.controller.graphic.config.UIConfigurator;
import it.simonetagliaferri.controller.graphic.config.UIMode;
import it.simonetagliaferri.model.dao.config.PersistenceProvider;
import it.simonetagliaferri.model.dao.config.DAOConfigurator;
import it.simonetagliaferri.utils.CliUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class Main {
    private static final String PERSISTENCE_PROPERTIES = "src/main/resources/persistence.properties";
    private static final String UI_PROPERTIES = "src/main/resources/ui.properties";
    private static final String PERSISTENCE_KEY = "PERSISTENCE_MODE";
    private static final String UI_KEY = "UI_MODE";
    public static void main(String[] args) {
        //PersistenceProvider is a ENUM of the available persistence layers.
        PersistenceProvider provider=null;
        //Getting the persistence layer to use in this run.
        try {
            provider = loadProperties(PERSISTENCE_PROPERTIES, PERSISTENCE_KEY, PersistenceProvider.class);
        } catch (IOException e) {
            System.out.println("Error in reading persistence mode: " + e.getMessage());
        }
        //Repeat the same thing as above, but for the UI.
        UIMode ui=null;
        try {
            ui = loadProperties(UI_PROPERTIES, UI_KEY, UIMode.class);
        } catch (IOException e) {
            System.out.println("Error in reading UI mode: " + e.getMessage());
        }
        if (provider == null) {
            //Using a utils class to have System.out code smells contained.
            CliUtils.println("Persistence mode not recognized");
            System.exit(1);
        }
        if (ui == null) {
            CliUtils.println("UI mode not recognized");
            System.exit(1);
        }
        DAOConfigurator.configure(provider);
        UIConfigurator.configure(ui);
    }

    public static String readProperties(String fileName, String property) throws IOException {
        //Using try with resources so that after the try block the BufferedReader is closed.
        try(BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(property);
        }
    }

    //Used generics to make it work with any kind of ENUM.
    private static <T extends Enum<T>> T loadProperties(String filePath, String property, Class<T> enumType) throws IOException {
        //Trim spaces and set to upper case to avoid false negatives.
        String value = readProperties(filePath, property).trim().toUpperCase();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Empty property: " + property);
        }
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value '" + value + "' for property '" + property +
                    "'. Expected one of: " + Arrays.toString(enumType.getEnumConstants()));
        }
    }
}
