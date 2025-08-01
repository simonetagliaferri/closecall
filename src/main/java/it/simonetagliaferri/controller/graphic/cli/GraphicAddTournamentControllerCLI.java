package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.AddTournamentLogicController;
import it.simonetagliaferri.utils.converters.DateConverter;
import it.simonetagliaferri.view.cli.AddTournamentCLIView;
import it.simonetagliaferri.view.cli.InvitePlayersHostView;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GraphicAddTournamentControllerCLI extends GraphicController {

    AddTournamentCLIView view;
    TournamentBean tournamentBean;
    AddTournamentLogicController controller;
    public GraphicAddTournamentControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new AddTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO() ,appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getPlayerDAO());
        this.view = new AddTournamentCLIView();
        this.tournamentBean = new TournamentBean();
    }
    public void start() {
        view.welcome();
        boolean validDate = false;
        String strDate;
        LocalDate startDate;
        LocalDate deadline;
        List<ClubBean> clubs = this.controller.getClubBeans();
        List<String> clubNames = clubs.stream().map(ClubBean::getName).collect(Collectors.toList());
        int clubNumber = view.getClub(clubNames);
        ClubBean club = clubs.get(clubNumber);
        tournamentBean.setClub(club);
        tournamentBean.setTournamentName(view.tournamentName());
        tournamentBean.setTournamentType(view.tournamentType());
        tournamentBean.setTournamentFormat(view.tournamentFormat());
        tournamentBean.setMatchFormat(view.matchFormat());
        tournamentBean.setCourtType(view.courtType());
        tournamentBean.setCourtNumber(view.courtNumber());
        tournamentBean.setTeamsNumber(view.numberOfTeams());
        tournamentBean.setPrizes(view.prizes());
        tournamentBean.setJoinFee(view.joinFee());
        int courtCost = view.includedCourt();
        if (courtCost == 2) {
            tournamentBean.setCourtPrice(view.courtCost());
        }
        else {
            tournamentBean.setCourtPrice(0);
        }
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
        validDate = false;
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
        estimatedEndDate();
        if (!this.controller.addTournament(tournamentBean)) {
            view.tournamentAlreadyExists();
        } else {
            addPlayersToTournament();
        }
    }

    public void estimatedEndDate() {
        LocalDate endDate = this.controller.estimatedEndDate(tournamentBean);
        int choice = view.showEstimatedEndDate(endDate);
        if (choice == 1) {
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
    }

    public void addPlayersToTournament() {
        if (view.askToAddPlayer() == InvitePlayersHostView.InviteChoices.YES)
            navigationManager.goToInvitePlayer(this.controller.getCurrentUserRole(), tournamentBean);
    }
}
