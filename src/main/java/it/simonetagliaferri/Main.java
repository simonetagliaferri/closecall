package it.simonetagliaferri;

import it.simonetagliaferri.controller.graphic.config.UIConfigurator;
import it.simonetagliaferri.controller.graphic.config.UIMode;
import it.simonetagliaferri.model.dao.config.PersistenceProvider;
import it.simonetagliaferri.model.dao.config.DAOConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        String persistenceMode;
        PersistenceProvider provider=null;
        try (InputStream input = new FileInputStream("src/main/resources/persistence.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            persistenceMode = properties.getProperty("PERSISTENCE_MODE");
            provider = PersistenceProvider.valueOf(persistenceMode.trim().toUpperCase());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String uiMode;
        UIMode ui=null;
        try (InputStream input = new FileInputStream("src/main/resources/ui.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            uiMode = properties.getProperty("UI_MODE");
            ui = UIMode.valueOf(uiMode.trim().toUpperCase());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        DAOConfigurator.configure(provider);
        UIConfigurator.configure(ui);
    }
}
