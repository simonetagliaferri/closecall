package it.simonetagliaferri.controller.graphic.navigation;


public class NavigationManagerFactory {
    private static final NavigationManagerFactory instance = new NavigationManagerFactory();
    private Class<?> implClass;

    private NavigationManagerFactory() {
    }

    public static NavigationManagerFactory getInstance() {
        return instance;
    }

    public void setImplClass(final Class<?> implClass) {
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
