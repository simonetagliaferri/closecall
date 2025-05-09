package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.logic.AddTournamentController;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.view.cli.AddTournamentCLIView;

import java.time.LocalDate;

public class GraphicAddTournamentControllerCLI {

    AddTournamentCLIView view = new AddTournamentCLIView();
    TournamentBean tournamentBean = new TournamentBean();
    AddTournamentController controller = new AddTournamentController();
    public void start() {
        view.welcome();
        if (this.controller.firstTime()) {
            askNeededInfo();
        }
        boolean validDate = false;
        String strDate;
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
            try {
                tournamentBean.setStartDate(this.controller.getStartDate(tournamentBean, strDate));
                validDate = true;
            } catch (InvalidDateException e) {
                view.invalidDate();
            }
        }
        validDate = false;
        while (!validDate) {
            strDate=view.signupDeadline();
            try {
                tournamentBean.setSignupDeadline(this.controller.getSignupDeadline(tournamentBean, strDate));
                validDate = true;
            } catch (InvalidDateException e) {
                view.invalidDate();
            }
        }
        EstimatedEndDate();
        addPlayersToTournament();
        this.controller.addTournament(tournamentBean);
    }

    public void askNeededInfo() {
        //Todo
    }

    public void EstimatedEndDate() {
        LocalDate endDate = this.controller.estimatedEndDate(tournamentBean);
        int choice = view.showEstimatedEndDate(endDate);
        if (choice == 1) {
            String strEndDate;
            boolean validDate = false;
            while (!validDate) {
                strEndDate=view.editEndDate();
                try {
                    tournamentBean.setEndDate(this.controller.getEndDate(tournamentBean, strEndDate));
                    validDate = true;
                } catch (InvalidDateException e) {
                    view.invalidDate();
                }
            }
        }
    }

    public void addPlayersToTournament() {
        int choice = view.askToAddPlayer();
        if (choice == 1) {
            if (tournamentBean.isSingles()) addSoloTeam();
            else addDuoTeam();
        }
    }

    public void addSoloTeam() {
        String player;
        boolean added = false;
        while (!added) {
            player = view.getPlayer();
            if (this.controller.addPlayer(player, tournamentBean)) {
                added = true;
            } else {
                view.invalidPlayer();
            }
        }
    }

    public void addDuoTeam() {
        String player1 = view.getPlayer();
        String player2 = view.getPlayer();
        this.controller.addTeam(player1, player2, tournamentBean);
    }
}
