package it.simonetagliaferri.controller.graphic;

import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.navigation.NavigationManager;

public abstract class GraphicController {
    
    protected NavigationManager navigationManager; // Not final only because I need to initialize it with initializeController for GUI controllers.

    public GraphicController() {}

    public GraphicController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
    }
}
