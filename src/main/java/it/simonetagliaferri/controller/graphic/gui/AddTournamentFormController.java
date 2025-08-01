package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.AddTournamentLogicController;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.utils.converters.DateConverter;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddTournamentFormController extends GraphicController implements GUIController {


    private AddTournamentLogicController controller;
    private HostBean user;


    @FXML private VBox addTournamentContent;
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
    @FXML private ComboBox clubs;

    private VBox prizesBox = new VBox();
    private List<Label> prizesLabels = new ArrayList<>();
    private List<TextField> prizesFields = new ArrayList<>();

    private TournamentBean tournamentBean = new TournamentBean();

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new AddTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO(),
                appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getPlayerDAO());
        postInit();
    }

    public void postInit() {
        List<ClubBean> clubBeans = this.controller.getClubBeans();
        List<String> clubNames = clubBeans.stream().map(ClubBean::getName).collect(Collectors.toList());
        clubs.setItems(FXCollections.observableList(clubNames));
        clubs.getSelectionModel().selectFirst();
        setClub();
    }

    @FXML
    public void setClub() {
        List<ClubBean> clubBeans = this.controller.getClubBeans();
        String clubName = clubs.getValue().toString();
        ClubBean clubBean = null;
        for (ClubBean club : clubBeans) {
            if (club.getName().equals(clubName)) {
                clubBean = club;
            }
        }
        tournamentBean.setClub(clubBean);
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
        numOfPrizesField.textProperty().addListener((obs, oldVal, newVal) -> requestPrizes());
        numOfPrizesField.textProperty().addListener((obs, oldVal, newVal) -> checkNumber(numOfPrizesField));
        numOfCourtsField.textProperty().addListener((obs, oldVal, newVal) -> setCourtsNumber());
        numOfTeamsField.textProperty().addListener((obs, oldVal, newVal) -> setTeamsNumber());
        startDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(controller.MinimumStartDate()));
            }
        });
        deadlinePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(controller.MinimumDeadline()));
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
        for (int i = 0; i < numOfPrizes; i++) {
            Label prizeLabel = new Label("Prize #" + (i + 1));
            TextField prizeTextField = new TextField();
            prizesBox.getChildren().addAll(prizeLabel, prizeTextField);
            prizesFields.add(prizeTextField);
            prizesLabels.add(prizeLabel);
            int me = i+1;
            prizeTextField.textProperty().addListener((obs, oldVal, newVal) -> checkPrize(prizeTextField.getText(), me));
        }
        if (!box.getChildren().contains(prizesBox)) {
            box.getChildren().add(prizesBox);
        }
        updateConfirmButtonBinding();
    }

    @FXML
    private void checkPrize(String prize, int i) {
        try {
            Double number = Double.parseDouble(prize);
            prizesLabels.get(i-1).setText("Prize #" + i);
            prizesLabels.get(i-1).setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            prizesLabels.get(i-1).setText("Prize #" + i + " Invalid prize entered.");
            prizesLabels.get(i-1).setTextFill(Color.RED);
        }
    }

    @FXML
    private int checkNumber(TextField field) {
        String input = field.getText();
        Parent parent = field.getParent();
        if (parent instanceof VBox) {
            VBox vbox = (VBox) parent;
            Node child = vbox.getChildren().get(0);
            if (child instanceof Label) {
                Label label = (Label) child;
                if (label.getUserData()==null) {
                    label.setUserData(label.getText());
                }
                String originalText = label.getUserData().toString();
                try {
                    int number=0;
                    if (!input.isEmpty())
                        number = Integer.parseInt(input);
                    if (number<0 || (number==0 && field!=numOfPrizesField)) throw new NumberFormatException();
                    label.setText(originalText);
                    label.setTextFill(Color.BLACK);
                    return number;
                } catch (NumberFormatException e) {
                    label.setText(originalText + " Invalid input entered.");
                    label.setTextFill(Color.RED);
                }
            }
        }
        return 0;
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
                    setDisable(empty || date.isAfter(controller.MinimumDeadline(tournamentBean)) || date.isBefore(controller.MinimumDeadline()));
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
                    setDisable(empty || date.isBefore(controller.MinimumStartDate(tournamentBean)));
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
    private void setTournamentName() {
        tournamentBean.setTournamentName(tournamentNameField.getText());
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
        int n = checkNumber(numOfTeamsField);
        tournamentBean.setTeamsNumber(n);
        estimateEndDate();
    }
    @FXML
    private void setCourtsNumber() {
        int n = checkNumber(numOfCourtsField);
        tournamentBean.setCourtNumber(n);
        estimateEndDate();
    }

    @FXML
    private void estimateEndDate() {
        if (!numOfTeamsField.getText().isEmpty() && !numOfCourtsField.getText().isEmpty() && startDatePicker.getValue() != null) {
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
                setDisable(empty || date.isBefore(controller.MinimumEndDate(tournamentBean)));
            }
        });
        endDatePicker.requestFocus();
        endDatePicker.show();
    }

    @FXML
    private void confirmTournamentData() {
        // Check if data is valid here if needed (optional, since button disabled when invalid)

        tournamentBean.setTournamentName(tournamentNameField.getText());
        tournamentBean.setCourtNumber(Integer.parseInt(numOfCourtsField.getText()));
        tournamentBean.setTeamsNumber(Integer.parseInt(numOfTeamsField.getText()));
        tournamentBean.setJoinFee(Double.valueOf(joinFeeField.getText()));
        tournamentBean.setCourtPrice(Double.valueOf(courtCostField.getText()));
        int numOfPrizes = Integer.parseInt(numOfPrizesField.getText());
        List<Double> prizes = new ArrayList<>();
        for (int i = 0; i < numOfPrizes; i++) {
            prizes.add(Double.valueOf(prizesFields.get(i).getText()));
        }
        tournamentBean.setPrizes(prizes);

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
                navigationManager.goToInvitePlayer(Role.HOST, tournamentBean);
            } else if (newResult == noButton) {
            }
            // If Cancel (window closed or cancel pressed), do nothing
        });
    }

    private void updateConfirmButtonBinding() {
        // First: collect all relevant observables
        List<Observable> observables = new ArrayList<>();

        // Static fields
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

        // Dynamic prize fields
        for (TextField prizeField : prizesFields) {
            observables.add(prizeField.textProperty());
        }

        // Unbind any previous binding
        confirmButton.disableProperty().unbind();

        // Bind with updated fields
        confirmButton.disableProperty().bind(
                Bindings.createBooleanBinding(this::isAnyFieldInvalid,
                        observables.toArray(new Observable[0]))
        );
    }

    private boolean isAnyFieldInvalid() {
        if (tournamentNameField.getText().trim().isEmpty()) return true;
        if (numOfCourtsField.getText().trim().isEmpty()) return true;
        if (numOfTeamsField.getText().trim().isEmpty()) return true;
        if (joinFeeField.getText().trim().isEmpty()) return true;
        if (!courtCostCheckBox.isSelected() && courtCostField.getText().trim().isEmpty()) return true;
        if (numOfPrizesField.getText().trim().isEmpty()) return true;
        if (startDatePicker.getValue() == null) return true;
        if (deadlinePicker.getValue() == null) return true;

        // Check prizes
        for (TextField prizeField : prizesFields) {
            if (prizeField.getText().trim().isEmpty()) return true;
        }

        return false;
    }
}
