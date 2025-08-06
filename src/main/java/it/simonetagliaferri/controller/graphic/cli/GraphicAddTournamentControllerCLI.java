package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.AddTournamentLogicController;
import it.simonetagliaferri.utils.converters.DateConverter;
import it.simonetagliaferri.view.cli.AddTournamentCLIView;
import java.time.DateTimeException;
import java.time.LocalDate;

public class GraphicAddTournamentControllerCLI extends GraphicController {

    AddTournamentCLIView view;
    TournamentBean tournamentBean;
    AddTournamentLogicController controller;
    public GraphicAddTournamentControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new AddTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO() ,appContext.getDAOFactory().getHostDAO());
        this.view = new AddTournamentCLIView();
        this.tournamentBean = new TournamentBean();
    }
    public void start() {
        view.welcome();
        tournamentBean.setTournamentName(view.tournamentName());
        tournamentBean.setTournamentType(view.tournamentType());
        tournamentBean.setTournamentFormat(view.tournamentFormat());
        tournamentBean.setMatchFormat(view.matchFormat());
        tournamentBean.setCourtType(view.courtType());
        tournamentBean.setCourtNumber(view.courtNumber());
        tournamentBean.setTeamsNumber(view.numberOfTeams());
        tournamentBean.setPrizes(view.prizes());
        tournamentBean.setJoinFee(view.joinFee());
        AddTournamentCLIView.Choice courtCost = view.includedCourt();
        if (courtCost == AddTournamentCLIView.Choice.NO) {
            tournamentBean.setCourtPrice(view.courtCost());
        }
        else {
            tournamentBean.setCourtPrice(0);
        }
        setStartDate();
        setSignupDeadline();
        estimatedEndDate();
        if (!this.controller.addTournament(tournamentBean)) {
            view.tournamentAlreadyExists();
        } else {
            addPlayersToTournament();
        }
    }

    public void setStartDate() {
        boolean validDate = false;
        String strDate;
        LocalDate startDate;
        while (!validDate) {
            strDate=view.startDate();
            try {
                startDate = DateConverter.formatDate(strDate);
                tournamentBean.setStartDate(this.controller.getStartDate(tournamentBean, startDate));
                validDate = true;
            } catch (DateTimeException | InvalidDateException e) {
                view.invalidDate();
            }
        }
    }

    public void setSignupDeadline() {
        boolean validDate = false;
        String strDate;
        LocalDate deadline;
        while (!validDate) {
            strDate=view.signupDeadline();
            try {
                deadline = DateConverter.formatDate(strDate);
                tournamentBean.setSignupDeadline(this.controller.getSignupDeadline(tournamentBean, deadline));
                validDate = true;
            } catch (DateTimeException | InvalidDateException e) {
                view.invalidDate();
            }
        }
    }

    public void estimatedEndDate() {
        LocalDate endDate = this.controller.estimatedEndDate(tournamentBean);
        AddTournamentCLIView.Choice choice = view.showEstimatedEndDate(endDate);
        if (choice == AddTournamentCLIView.Choice.YES) {
            String strEndDate;
            boolean validDate = false;
            while (!validDate) {
                strEndDate=view.editEndDate();
                try {
                    LocalDate newEndDate = DateConverter.formatDate(strEndDate);
                    tournamentBean.setEndDate(this.controller.getEndDate(tournamentBean, newEndDate));
                    validDate = true;
                } catch (DateTimeException | InvalidDateException e) {
                    view.invalidDate();
                }
            }
        }
        else {
            tournamentBean.setEndDate(this.controller.getEndDate(tournamentBean, endDate));
        }
    }

    public void addPlayersToTournament() {
        if (view.askToAddPlayer() == AddTournamentCLIView.Choice.YES)
            navigationManager.goToInvitePlayer(this.controller.getCurrentUserRole(), tournamentBean);
    }
}
