package it.simonetagliaferri.controller.graphic.navigation;

import it.simonetagliaferri.model.domain.Role;

import java.io.IOException;

public interface NavigationManager {

    void start();

    void login() throws IOException;

    void goToDashboard(Role role) throws IOException;


}
