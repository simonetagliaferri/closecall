package it.simonetagliaferri;

import it.simonetagliaferri.controller.graphic.cli.GraphicStartControllerCLI;
import it.simonetagliaferri.controller.logic.StartController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        String persistenceMode=null;
        try (InputStream input = new FileInputStream("src/main/resources/persistence.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            persistenceMode = properties.getProperty("PERSISTENCE_MODE");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String uiMode;
        try (InputStream input = new FileInputStream("src/main/resources/ui.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            uiMode = properties.getProperty("UI_MODE");
            System.out.println(uiMode);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        StartController startController = new StartController();
        startController.setPersistenceProvider(persistenceMode);
        new GraphicStartControllerCLI().start();
    }
}
