package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.AddTournamentLogicController;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.utils.converters.DateConverter;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphicAddTournamentControllerGUI extends GraphicController implements GUIController {

    private AddTournamentLogicController controller;
    private final TournamentBean tournamentBean = new TournamentBean();

    @FXML private ChoiceBox<String> tournamentTypeChoice;
    @FXML private ChoiceBox<String> tournamentFormatChoice;
    @FXML private ChoiceBox<String> matchFormatChoice;
    @FXML private ChoiceBox<String> courtTypeChoice;
    @FXML private TextField tournamentNameField;
    @FXML private TextField numOfCourtsField;
    @FXML private TextField numOfTeamsField;
    @FXML private TextField numOfPrizesField;
    @FXML private Label startDateLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private Label deadlineLabel;
    @FXML private DatePicker deadlinePicker;
    @FXML private Label endDateLabel1;
    @FXML private Label endDateLabel2;
    @FXML private Hyperlink endDateHyper;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField joinFeeField;
    @FXML private CheckBox courtCostCheckBox;
    @FXML private TextField courtCostField;
    @FXML private Button confirmButton;
    @FXML private Label tournamentNameLabel;
    @FXML private Label joinFeeLabel;
    @FXML private Label courtCostLabel;
    @FXML private Label numOfPrizesLabel;
    @FXML private Label numOfTeamsLabel;
    @FXML private Label numOfCourtsLabel;

    private final VBox prizesBox = new VBox();
    private final List<Label> prizesLabels = new ArrayList<>();
    private final List<TextField> prizesFields = new ArrayList<>();
    List<Double> prizes = new ArrayList<>();
    private static final String PRIZE = "Prize #";

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new AddTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO(),
                appContext.getDAOFactory().getPlayerDAO());
    }

    @FXML private void initialize() {
        List<String> tournamentTypes = Arrays.asList("Men's singles", "Women's singles", "Men's doubles", "Women's doubles", "Mixed doubles");
        List <String> tournamentFormat = Arrays.asList("RoundRobin", "Single-elimination", "Double-elimination");
        List <String> matchFormat = Arrays.asList("Best of three sets", "Best of five sets");
        List <String> courtType = Arrays.asList("Hard", "Clay", "Grass");
        setChoiceBox(tournamentTypeChoice, tournamentTypes);
        setChoiceBox(tournamentFormatChoice, tournamentFormat);
        setChoiceBox(matchFormatChoice, matchFormat);
        setChoiceBox(courtTypeChoice, courtType);
        tournamentFormatChoice.requestFocus();
        tournamentNameField.textProperty().addListener((observable, oldValue, newValue) -> setTournamentName());
        numOfPrizesField.textProperty().addListener((obs, oldVal, newVal) -> requestPrizes());
        numOfPrizesField.textProperty().addListener((obs, oldVal, newVal) -> checkNumOfPrizes());
        numOfCourtsField.textProperty().addListener((obs, oldVal, newVal) -> setCourtsNumber());
        numOfTeamsField.textProperty().addListener((obs, oldVal, newVal) -> setTeamsNumber());
        joinFeeField.textProperty().addListener((obs, oldVal, newVal) -> setJoinFee());
        courtCostField.textProperty().addListener((obs, oldVal, newVal) -> setCourtCost());
        startDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(controller.minimumStartDate()));
            }
        });
        deadlinePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(controller.maxDeadline()));
            }
        });
        updateConfirmButtonBinding();
    }

    @FXML
    private void setChoiceBox(ChoiceBox<String> choiceBox, List<String> list) {
        choiceBox.getItems().addAll(list);
        choiceBox.getSelectionModel().select(0);
    }

    @FXML
    private void courtCost() {
        if (courtCostCheckBox.isSelected()) {
            courtCostField.clear();
            courtCostField.setDisable(true);
        } else {
            courtCostField.setDisable(false);
        }
    }

    @FXML
    private void requestPrizes() {
        int numOfPrizes;
        if (numOfPrizesField.getText().isEmpty()) {
            prizesBox.getChildren().clear();
            return;
        }
        try {
            numOfPrizes = Integer.parseInt(numOfPrizesField.getText());
            if (numOfPrizes < 0) return;
        } catch (NumberFormatException e) {
            return;
        }
        VBox box = (VBox) numOfPrizesField.getParent();
        prizesBox.getChildren().clear();
        prizesFields.clear();
        prizesLabels.clear();
        prizes.clear();
        for (int i = 0; i < numOfPrizes; i++) {
            Label prizeLabel = new Label(PRIZE + (i + 1));
            TextField prizeTextField = new TextField();
            prizesBox.getChildren().addAll(prizeLabel, prizeTextField);
            prizesFields.add(prizeTextField);
            prizesLabels.add(prizeLabel);
            int me = i+1;
            prizeTextField.textProperty().addListener((obs, oldVal, newVal) -> checkPrize(me));
        }
        if (!box.getChildren().contains(prizesBox)) {
            box.getChildren().add(prizesBox);
        }
        updateConfirmButtonBinding();
    }

    @FXML
    private void checkPrize(int i) {
        Label lbl = prizesLabels.get(i - 1);
        TextField tf = prizesFields.get(i - 1);
        lbl.setText(PRIZE + i);
        lbl.setTextFill(Color.BLACK);
        if (!checkDouble(tf, lbl)) return;
        List<Double> current = new ArrayList<>();
        for (TextField f : prizesFields) {
            if (f.getText().isBlank()) return;
            current.add(Double.parseDouble(f.getText()));
        }
        tournamentBean.setPrizes(current);
    }

    private boolean checkInt(TextField textField, Label label) {
        try {
            Integer.parseInt(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            label.setText("Please enter a valid number.");
            label.setTextFill(Color.RED);
            return false;
        }
    }

    private void checkNumOfPrizes() {
        numOfPrizesLabel.setText("Number of prizes");
        numOfPrizesLabel.setTextFill(Color.BLACK);
        checkInt(numOfPrizesField, numOfPrizesLabel);
    }

    @FXML
    private void checkStartDate() {
        try {
            tournamentBean.setStartDate(this.controller.getStartDate(tournamentBean, startDatePicker.getValue()));
            startDateLabel.setText("Start date");
            startDateLabel.setTextFill(Color.BLACK);
            deadlinePicker.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isAfter(controller.maxDeadline(tournamentBean)) || date.isBefore(controller.maxDeadline()));
                }
            });
            estimateEndDate();
        } catch (DateTimeException | InvalidDateException e) {
            startDateLabel.setText("The chosen start date is not a valid date.");
            startDateLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void checkSignupDeadline() {
        try {
            tournamentBean.setSignupDeadline(this.controller.getSignupDeadline(tournamentBean, deadlinePicker.getValue()));
            deadlineLabel.setText("Signup deadline");
            deadlineLabel.setTextFill(Color.BLACK);
            startDatePicker.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isBefore(controller.minimumStartDate(tournamentBean)));
                }
            });
        } catch (DateTimeException | InvalidDateException e) {
            deadlineLabel.setText("The chosen signup deadline is not valid.");
            deadlineLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void updateEndDate() {
        tournamentBean.setEndDate(endDatePicker.getValue());
        endDateLabel2.setText(DateConverter.dateToString(endDatePicker.getValue()));
    }

    @FXML
    private void setTournamentName() {
        tournamentNameLabel.setText("Tournament name");
        tournamentNameLabel.setTextFill(Color.BLACK);
        tournamentBean.setTournamentName(tournamentNameField.getText());
        if (this.controller.invalidTournamentName(tournamentBean)) {
            tournamentNameLabel.setText("You already have a tournament with this name.");
            tournamentNameLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void setTournamentFormat() {
        tournamentBean.setTournamentFormat(tournamentFormatChoice.getValue());
        estimateEndDate();
    }

    @FXML
    private void setTournamentType() {
        tournamentBean.setTournamentType(tournamentTypeChoice.getValue());
        estimateEndDate();
    }

    @FXML
    private void setMatchFormatChoice() {
        tournamentBean.setMatchFormat(matchFormatChoice.getValue());
    }

    @FXML
    private void setCourtTypeChoice() {
        tournamentBean.setCourtType(courtTypeChoice.getValue());
    }


    @FXML
    private void setTeamsNumber() {
        numOfTeamsLabel.setText("Number of teams");
        numOfTeamsLabel.setTextFill(Color.BLACK);
        if (checkInt(numOfTeamsField, numOfTeamsLabel)) {
            int n = Integer.parseInt(numOfTeamsField.getText());
            tournamentBean.setTeamsNumber(n);
            estimateEndDate();
        }
    }

    @FXML
    private void setCourtsNumber() {
        numOfCourtsLabel.setText("Number of available courts");
        numOfCourtsLabel.setTextFill(Color.BLACK);
        if (checkInt(numOfCourtsField, numOfCourtsLabel)) {
            int n = Integer.parseInt(numOfCourtsField.getText());
            tournamentBean.setCourtNumber(n);
            estimateEndDate();
        }
    }

    @FXML
    private void setJoinFee() {
        joinFeeLabel.setText("Join fee");
        joinFeeLabel.setTextFill(Color.BLACK);
        if (checkDouble(joinFeeField, joinFeeLabel)) {
            double fee = Double.parseDouble(joinFeeField.getText());
            tournamentBean.setJoinFee(fee);
        }
    }

    private boolean checkDouble(TextField textField, Label label) {
        try {
            Double.parseDouble(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            label.setText("Please enter a valid number.");
            label.setTextFill(Color.RED);
            return false;
        }
    }

    @FXML
    private void setCourtCost() {
        courtCostLabel.setText("Court cost");
        courtCostLabel.setTextFill(Color.BLACK);
        if (courtCostField.getText().isEmpty()) {
            tournamentBean.setCourtPrice(0);
        } else {
            if (checkDouble(courtCostField, courtCostLabel)) {
                double courtCost = Double.parseDouble(courtCostField.getText());
                tournamentBean.setCourtPrice(courtCost);
            }
        }
    }

    @FXML
    private void estimateEndDate() {
        if ( (numOfTeamsField.getText().isEmpty() || checkInt(numOfTeamsField, numOfTeamsLabel)) &&
        (numOfCourtsField.getText().isEmpty() || checkInt(numOfCourtsField, numOfCourtsLabel)) && startDatePicker.getValue() != null) {
            endDateLabel1.setVisible(true);
            LocalDate endDate = this.controller.estimatedEndDate(tournamentBean);
            endDateLabel2.setText(DateConverter.dateToString(endDate));
            endDateLabel2.setVisible(true);
            tournamentBean.setEndDate(endDate);
            endDateHyper.setVisible(true);
        }
    }

    @FXML
    private void editEndDate() {
        endDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(controller.minimumEndDate(tournamentBean)));
            }
        });
        endDatePicker.requestFocus();
        endDatePicker.show();
    }

    @FXML
    private void confirmTournamentData() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Tournament Creation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to add players now?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Non-blocking
        alert.show();

        // Listen to user's choice
        alert.resultProperty().addListener((obs, oldResult, newResult) -> {
            this.controller.addTournament(tournamentBean);
            if (newResult == yesButton) {
                navigationManager.goToInvitePlayer(tournamentBean);
            } else {
                navigationManager.goToDashboard(Role.HOST);
            }
        });
    }

    private void updateConfirmButtonBinding() {
        List<Observable> observables = new ArrayList<>();
        observables.add(tournamentNameField.textProperty());
        observables.add(numOfCourtsField.textProperty());
        observables.add(numOfTeamsField.textProperty());
        observables.add(joinFeeField.textProperty());
        observables.add(courtCostCheckBox.selectedProperty());
        observables.add(courtCostField.textProperty());
        observables.add(courtCostField.disabledProperty());
        observables.add(numOfPrizesField.textProperty());
        observables.add(startDatePicker.valueProperty());
        observables.add(deadlinePicker.valueProperty());
        for (TextField prizeField : prizesFields) {
            observables.add(prizeField.textProperty());
        }
        confirmButton.disableProperty().unbind();
        confirmButton.disableProperty().bind(
                Bindings.createBooleanBinding(this::isAnyFieldInvalid,
                        observables.toArray(new Observable[0]))
        );
    }

    private boolean isAnyFieldInvalid() {
        return isTournamentNameInvalid() || isNumOfCourtsInvalid() || isNumOfTeamsInvalid() ||
                isJoinFeeInvalid() || isCourtCostInvalid() || isNumOfPrizesInvalid() ||
                isStartDateInvalid() || isDeadlineInvalid() || arePrizesInvalid();

    }

    private boolean isTournamentNameInvalid() {
        return tournamentNameField.getText().trim().isEmpty() || this.controller == null || this.controller.invalidTournamentName(tournamentBean);
    }

    private boolean isNumOfCourtsInvalid() {
        return numOfCourtsField.getText().isEmpty() || !checkInt(numOfCourtsField, numOfCourtsLabel);
    }

    private boolean isNumOfTeamsInvalid() {
        return numOfTeamsField.getText().isEmpty() || !checkInt(numOfTeamsField, numOfTeamsLabel);
    }

    private boolean isJoinFeeInvalid() {
        return joinFeeField.getText().isEmpty() || !checkDouble(joinFeeField, joinFeeLabel);
    }

    private boolean isCourtCostInvalid() {
        return !courtCostCheckBox.isSelected() && courtCostField.getText().trim().isEmpty();
    }

    private boolean isNumOfPrizesInvalid() {
        return numOfPrizesField.getText().isEmpty() || !checkInt(numOfPrizesField, numOfPrizesLabel);
    }

    private boolean isStartDateInvalid() {
        return startDatePicker.getValue() == null;
    }

    private boolean isDeadlineInvalid() {
        return deadlinePicker.getValue() == null;
    }

    private boolean arePrizesInvalid() {
        for (int i = 0 ; i < prizesFields.size(); i++) {
            if (prizesFields.get(i).getText().isEmpty() || !checkDouble(prizesFields.get(i), prizesLabels.get(i))) return true;
        }
        return false;
    }
}
