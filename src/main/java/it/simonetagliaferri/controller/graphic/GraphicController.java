package it.simonetagliaferri.controller.graphic;

import it.simonetagliaferri.AppContext;

public abstract class GraphicController {
    protected AppContext appContext;

    public GraphicController() {

    }

    public GraphicController(AppContext appContext) {
        this.appContext = appContext;
    }
    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
    }
}
