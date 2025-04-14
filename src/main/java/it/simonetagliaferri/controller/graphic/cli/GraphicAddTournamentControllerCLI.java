package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.view.cli.AddTournamentCLIView;

import java.time.LocalDate;

public class GraphicAddTournamentControllerCLI {

    AddTournamentCLIView view = new AddTournamentCLIView();
    TournamentBean tournamentBean = new TournamentBean();
    public void start() {
        view.welcome();
        boolean validDate = false;
        String strDate;
        LocalDate date;
        tournamentBean.setTournamentName(view.tournamentName());
        tournamentBean.setTournamentType(view.tournamentType());
        tournamentBean.setTournamentFormat(view.tournamentFormat());
        tournamentBean.setMatchFormat(view.matchFormat());
        tournamentBean.setCourtType(view.courtType());
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
        System.out.println(tournamentBean.toString());
    }
}
