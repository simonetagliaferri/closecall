package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.infrastructure.AppContext;

public interface GUIController {

    /**
     * This method is used to inject AppContext into JavaFX GUI controllers,
     * which are instantiated by the FXML loader and cannot receive constructor parameters.
     */
    void initializeController(AppContext appContext);

}
