package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.AddClubLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GraphicAddClubControllerGUI extends GraphicController implements GUIController{

    private AddClubLogicController controller;

    @FXML private HBox addClub;
    @FXML private VBox firstClubWarning;
    @FXML private VBox clubDetails;
    @FXML private TextField clubName;
    @FXML private TextField clubStreet;
    @FXML private TextField clubNumber;
    @FXML private TextField clubCity;
    @FXML private TextField clubZip;
    @FXML private TextField clubState;
    @FXML private TextField clubCountry;
    @FXML private TextField clubPhone;
    @FXML private Button confirmButton;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new AddClubLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getClubDAO(),
                appContext.getDAOFactory().getHostDAO());
        postInit();
    }

    private void postInit() {
        firstClubWarning.setVisible(true);
        BooleanBinding fieldsEmpty =
                clubName.textProperty().isEmpty()
                        .or(clubStreet.textProperty().isEmpty())
                        .or(clubNumber.textProperty().isEmpty())
                        .or(clubCity.textProperty().isEmpty())
                        .or(clubZip.textProperty().isEmpty())
                        .or(clubState.textProperty().isEmpty())
                        .or(clubCountry.textProperty().isEmpty())
                        .or(clubPhone.textProperty().isEmpty());

        confirmButton.disableProperty().bind(fieldsEmpty);
    }

    @FXML
    public void logout() {
        navigationManager.login();
    }

    @FXML
    public void addClub() {
        clubDetails.setVisible(true);
        firstClubWarning.setVisible(false);
        addClub.getChildren().setAll(clubDetails);
    }

    @FXML
    public void confirmClubDetails() {
        ClubBean clubBean = new ClubBean();
        clubBean.setName(clubName.getText());
        clubBean.setStreet(clubStreet.getText());
        clubBean.setNumber(clubNumber.getText());
        clubBean.setCity(clubCity.getText());
        clubBean.setZip(clubZip.getText());
        clubBean.setState(clubState.getText());
        clubBean.setCountry(clubCountry.getText());
        clubBean.setPhone(clubPhone.getText());
        controller.addClub(clubBean);
        navigationManager.goToDashboard(Role.HOST);
    }

}
