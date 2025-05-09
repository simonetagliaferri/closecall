package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.controller.logic.HostDashboardController;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class GraphicHostDashboardControllerGUI {
    private final HostDashboardController controller = new HostDashboardController();
    private final HostBean user = this.controller.getHostBean();
    private NavigationManager navigationManager = NavigationManager.getInstance();


    @FXML private TabPane tabPane;
    @FXML private MenuButton account;
    @FXML
    private void initialize() {
        account.setText(user.getUsername());
        setTabPaneLeftTabsHorizontal(tabPane);
    }

    @FXML
    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    void setTabPaneLeftTabsHorizontal(TabPane tabPane){

        tabPane.setSide(Side.LEFT);
        tabPane.setRotateGraphic(true);

        Label l;
        StackPane stp;
        for(Tab t : tabPane.getTabs()){
            l = new Label(t.getText());
            l.setRotate(90);
            stp = new StackPane(new Group(l));
            stp.setRotate(90);
            t.setGraphic(stp);
            t.setText("");
        }
        tabPane.setTabMinHeight(100);
        tabPane.setTabMaxHeight(100);
    }
}
