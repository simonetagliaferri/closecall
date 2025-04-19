package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.logic.AddTournamentController;
import it.simonetagliaferri.view.cli.AddTournamentCLIView;

import java.time.LocalDate;

public class GraphicAddTournamentControllerCLI {

    AddTournamentCLIView view = new AddTournamentCLIView();
    TournamentBean tournamentBean = new TournamentBean();
    AddTournamentController controller = new AddTournamentController();
    public void start() {
        view.welcome();
        boolean validDate = false;
        String strDate;
        LocalDate date;
        tournamentBean.setHostUsername(this.controller.getHostBean().getUsername());
        tournamentBean.setTournamentName(view.tournamentName());
        tournamentBean.setTournamentType(view.tournamentType());
        tournamentBean.setTournamentFormat(view.tournamentFormat());
        tournamentBean.setMatchFormat(view.matchFormat());
        tournamentBean.setCourtType(view.courtType());
        tournamentBean.setCourtNumber(view.courtNumber());
        tournamentBean.setTeamsNumber(view.numberOfTeams());
        tournamentBean.setPrizes(view.prizes());
        while (!validDate) {
            strDate=view.startDate();
            date=TournamentBean.isDateValid(strDate);
            if (date != null) {
                validDate = true;
                tournamentBean.setStartDate(date);
            }
            else {
                view.invalidDate();
            }
        }
        validDate = false;
        while (!validDate) {
            strDate = view.signupDeadline();
            date=TournamentBean.isDateValid(strDate);
            if (date!=null && tournamentBean.isDeadlineValid(date)) {
                validDate = true;
                tournamentBean.setSignupDeadline(date);
            }
            else {
                view.invalidDate();
            }
        }
        System.out.println("You: " + tournamentBean.getHostUsername() + " Created tournament " + tournamentBean.getTournamentName() + ".");
        EstimatedEndDate();
        this.controller.addTournament(tournamentBean);
    }

    public void EstimatedEndDate() {
        LocalDate endDate = this.controller.EstimatedEndDate(tournamentBean);
        //view.showEstimatedEndDate();
        int choice;
        //view.changeEndDate();
        tournamentBean.setEndDate(endDate);
        System.out.println("The tournament starts at " + tournamentBean.getStartDate() + " and the estimated end date is " + endDate);
    }

}
