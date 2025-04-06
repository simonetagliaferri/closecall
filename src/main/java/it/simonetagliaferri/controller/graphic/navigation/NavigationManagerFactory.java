package it.simonetagliaferri.controller.graphic.navigation;


public class NavigationManagerFactory {
    private static final NavigationManagerFactory instance = new NavigationManagerFactory();
    private Class<? extends NavigationManager> implClass;

    private NavigationManagerFactory() {
    }

    public static NavigationManagerFactory getInstance() {
        return instance;
    }

    public void setImplClass(final Class<? extends NavigationManager> implClass) {
        this.implClass = implClass;
    }

    public NavigationManager getNavigationManager() {
        if (implClass == NavigationManagerCLI.class) {
            return NavigationManagerCLI.getInstance();
        } else if (implClass == NavigationManagerGUI.class) {
            return NavigationManagerGUI.getInstance();
        }
        return null;
    }
}
