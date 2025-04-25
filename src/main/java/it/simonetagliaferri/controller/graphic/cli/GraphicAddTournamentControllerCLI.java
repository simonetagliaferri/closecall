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
                this.controller.setStartDate(tournamentBean, strDate);
                validDate = true;
            } catch (InvalidDateException e) {
                view.invalidDate();
            }
        }
        validDate = false;
        while (!validDate) {
            strDate=view.signupDeadline();
            try {
                this.controller.setSignupDeadline(tournamentBean, strDate);
                validDate = true;
            } catch (InvalidDateException e) {
                view.invalidDate();
            }
        }
        System.out.println("You: " + tournamentBean.getHostUsername() + " Created tournament " + tournamentBean.getTournamentName() + ".");
        EstimatedEndDate();
        addPlayersToTournament();
        this.controller.addTournament(tournamentBean);
    }

    public void askNeededInfo() {
        //Todo
    }

    public void EstimatedEndDate() {
        LocalDate endDate = this.controller.EstimatedEndDate(tournamentBean);
        int choice = view.showEstimatedEndDate(endDate);
        if (choice == 1) {
            String strEndDate;
            boolean validDate = false;
            while (!validDate) {
                strEndDate=view.editEndDate();
                try {
                    this.controller.setEndDate(tournamentBean, strEndDate);
                    validDate = true;
                } catch (InvalidDateException e) {
                    view.invalidDate();
                }
            }
        }
        System.out.println("The tournament starts at " + tournamentBean.getStartDate() + " and the estimated end date is " + tournamentBean.getEndDate());
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
            if (this.controller.addPlayer(player)) {
                added = true;
                System.out.println("Player " + player + " added to tournament.");
            } else {
                view.invalidPlayer();
            }
        }
    }

    public void addDuoTeam() {
        String player1 = view.getPlayer();
        String player2 = view.getPlayer();
        this.controller.addTeam(player1, player2);
    }
}
