package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HostDashboardLogicController;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.utils.CliUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class GraphicHostDashboardControllerGUI extends GraphicController implements GUIController {
    private HostDashboardLogicController controller;
    private HostBean hostBean;


    @FXML private Text logo;
    @FXML private MenuButton account;
    @FXML private ToggleButton home;
    @FXML private ToggleButton newTournaments;
    @FXML private ToggleButton searchScreen;
    @FXML private VBox contentWrapper;
    private Node addClubView;
    private Node addTournamentView;
    private Node homeView;
    List<ToggleButton> buttons;



    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new HostDashboardLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    public void postInit() {
        hostBean = this.controller.getHostBean();
        account.setText(hostBean.getUsername());
        additionalInfo();
        showHome();
    }

    @FXML
    private void initialize() {
        FontIcon icon1 = new FontIcon("oct-home-16");
        icon1.setIconSize(24);
        FontIcon icon2 = new FontIcon("oct-plus-16");
        icon2.setIconSize(24);
        FontIcon icon3 = new FontIcon("oct-search-16");
        icon3.setIconSize(24);
        List<FontIcon> icons = Arrays.asList(icon1, icon2, icon3);
        buttons = Arrays.asList(home, newTournaments, searchScreen);
        setButtons(buttons, icons);
    }

    private void additionalInfo() {
        if (this.controller.additionalInfoNeeded()) {
            disableButtons(buttons);
            showAddClub();
        }
    }

    private void disableButtons(List<ToggleButton> buttons) {
        for (ToggleButton button : buttons) {
            button.setDisable(true);
        }
    }

    private void setButtons(List<ToggleButton> buttons, List<FontIcon> icons) {
        if (buttons == null ) {
            CliUtils.println("No buttons selected");
        } else {
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
                    oldToggle.setSelected(true); // Re-select the old one
                }
            });
            buttons.get(0).setSelected(true);
        }
    }

    @FXML
    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    @FXML
    private void changeScreen() {
        if (newTournaments.isSelected()) {
            showAddTournament();
        }
        else if (searchScreen.isSelected()) {
        }
        else if (home.isSelected()) {
            showHome();
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

    TournamentBean tournamentBean;

    public void setTournamentBean(TournamentBean tournamentBean) {
        this.tournamentBean = tournamentBean;
    }

    public TournamentBean getTournamentBean(TournamentBean tournamentBean) {
        return this.tournamentBean;
    }

}
