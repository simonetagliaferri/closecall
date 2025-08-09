package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HostDashboardLogicController;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class GraphicHostDashboardControllerGUI extends GraphicController implements GUIController {

    private HostDashboardLogicController controller;

    @FXML private MenuButton account;
    @FXML private ToggleButton home;
    @FXML private ToggleButton newTournaments;
    @FXML private VBox contentWrapper;
    private List<ToggleButton> buttons;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new HostDashboardLogicController(appContext.getSessionManager(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    public void postInit() {
        HostBean hostBean = this.controller.getHostBean();
        account.setText(hostBean.getUsername());
        additionalInfo();
    }

    @FXML
    private void initialize() {
        FontIcon icon1 = new FontIcon("oct-home-16");
        icon1.setIconSize(24);
        FontIcon icon2 = new FontIcon("oct-plus-16");
        icon2.setIconSize(24);
        List<FontIcon> icons = Arrays.asList(icon1, icon2);
        buttons = Arrays.asList(home, newTournaments);
        setButtons(buttons, icons);
    }

    private void additionalInfo() {
        if (this.controller.additionalInfoNeeded()) {
            disableButtons(buttons);
            showAddClub();
        } else {
            showHome();
        }
    }

    private void disableButtons(List<ToggleButton> buttons) {
        for (ToggleButton button : buttons) {
            button.setDisable(true);
        }
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
        if (newTournaments.isSelected()) {
            navigationManager.goToAddTournament();
        }
        else if (home.isSelected()) {
            navigationManager.goHome(Role.HOST);
        }
    }

    @FXML
    public void showAddClub() {
        try {
            SceneManagerGUI.loadWrapperWithContext("addClub", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showAddTournament() {
        try {
            SceneManagerGUI.loadWrapperWithContext("addTournament", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showInvitePlayers(TournamentBean tournamentBean) {
        GraphicInvitePlayersHostControllerGUI addPlayersFormController;
        try {
            addPlayersFormController = SceneManagerGUI.loadWrapperWithContext("addPlayers", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addPlayersFormController.setTournamentBean(tournamentBean);
    }

    @FXML public void showHome() {
        try {
            SceneManagerGUI.loadWrapperWithContext("hostHome", contentWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showNotifications() {

    }

}
