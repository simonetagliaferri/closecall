package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.PlayerDashboardLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GraphicPlayerDashboardControllerGUI extends GraphicController implements GUIController {

    PlayerDashboardLogicController controller;

    @FXML private VBox contentWrapper;
    @FXML private MenuButton account;
    @FXML private ToggleButton home;
    @FXML private ToggleButton joinTournament;
    @FXML private MenuButton notifications;


    @FXML
    private void initialize() {
        FontIcon icon1 = new FontIcon("oct-home-16");
        icon1.setIconSize(24);
        FontIcon icon2 = new FontIcon("oct-search-16");
        icon2.setIconSize(24);
        List<FontIcon> icons = Arrays.asList(icon1, icon2);
        List<ToggleButton> buttons = Arrays.asList(home, joinTournament);
        setButtons(buttons, icons);
        FontIcon icon3 = new FontIcon("oct-bell-16");
        icon3.setIconSize(16);
        notifications.setGraphic(icon3);
    }

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new PlayerDashboardLogicController(appContext.getSessionManager(),
                appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    public void postInit() {
        PlayerBean playerBean = this.controller.getPlayerBean();
        account.setText(playerBean.getUsername());
        showHome();
    }

    private void setButtons(List<ToggleButton> buttons, List<FontIcon> icons) {
        ToggleGroup tabs = new ToggleGroup();
        for (int i = 0; i < buttons.size(); i++) {
            ToggleButton button = buttons.get(i);
            button.setToggleGroup(tabs);
            button.setText("");
            button.setGraphic(icons.get(i));
            button.setMinHeight(40);
            button.setMaxHeight(40);
            button.setMinWidth(40);
            button.setMaxWidth(40);
        }
        tabs.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                oldToggle.setSelected(true);
            }
        });
        buttons.get(0).setSelected(true);
    }

    @FXML
    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    @FXML
    private void changeScreen() {
        if (joinTournament.isSelected()) {
            navigationManager.goToJoinTournament();
        }
        else if (home.isSelected()) {
            navigationManager.goHome(Role.PLAYER);
        }
    }

    public void showJoinTournament() {
        try {
            SceneManagerGUI.loadWrapperWithContext("joinTournament", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showHome() {
        try {
            SceneManagerGUI.loadWrapperWithContext("playerHome", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void openNotifications() {
        navigationManager.goToNotifications(Role.PLAYER);
    }

    @FXML
    private void openInvites() {
        navigationManager.goToProcessInvites();
    }

    public void showNotifications() {
        try {
            SceneManagerGUI.loadWrapperWithContext("playerNotifications", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showInvites() {
        try {
            SceneManagerGUI.loadWrapperWithContext("playerInvites", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
