package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.*;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class GraphicTournamentDetails {

    public static final String ACCORDION_LABEL = "accordion-label";

    protected List<Label> getTournamentDetails(TournamentBean tournamentBean) {
        List<Label> details = new ArrayList<>();
        Label tournamentName = new Label("Tournament name: " + tournamentBean.getTournamentName());
        details.add(tournamentName);
        Label tournamentFormat = new Label("Tournament format: " + tournamentBean.getTournamentFormat());
        details.add(tournamentFormat);
        Label tournamentType = new Label("Tournament type: " + tournamentBean.getTournamentType());
        details.add(tournamentType);
        Label courtType = new Label("Court type: " + tournamentBean.getCourtType());
        details.add(courtType);
        Label teamNumber = new Label("Number of teams: " + tournamentBean.getTeamsNumber());
        details.add(teamNumber);
        Label availableSpots = new Label("Available spots: " + tournamentBean.getAvailableSpots());
        details.add(availableSpots);
        Label joinFees = new Label("Join fee: " + tournamentBean.getJoinFee());
        details.add(joinFees);
        String courtPrice = tournamentBean.getCourtPrice() != 0 ? Double.toString(tournamentBean.getCourtPrice()) : "included in join fee";
        Label courtCost = new Label("Court cost: " + courtPrice);
        details.add(courtCost);
        if (!tournamentBean.getPrizes().isEmpty()) {
            Label prizes = new Label("Prizes:");
            details.add(prizes);
            for (int i = 0; i < tournamentBean.getPrizes().size(); i++) {
                double prize = tournamentBean.getPrizes().get(i);
                int place = i + 1;
                Label prizeLabel = new Label("\t" + place + ". " + prize);
                details.add(prizeLabel);
            }
        } else {
            Label prizes = new Label("Prizes: none");
            details.add(prizes);
        }
        Label startDate = new Label("Start date: " + tournamentBean.getStartDate());
        details.add(startDate);
        Label signupDeadline = new Label("Signup deadline: " + tournamentBean.getSignupDeadline());
        details.add(signupDeadline);
        Label estimatedEndDate = new Label("Estimated end date: " + tournamentBean.getEndDate());
        details.add(estimatedEndDate);
        for (Label label : details) {
            label.getStyleClass().add(ACCORDION_LABEL);
        }
        return details;
    }

    protected List<Label> getAllTeams(TournamentBean tournamentBean) {
        List<Label> teams = new ArrayList<>(getConfirmedTeams(tournamentBean));
        List<TeamBean> pending = tournamentBean.getPendingTeams();
        if (!pending.isEmpty()) {
            Label pendingTeams = new Label("Pending teams:");
            teams.add(pendingTeams);
            teams.addAll(getPlayers(pending));
        }
        if (!tournamentBean.isSingles()) {
            List<TeamBean> partials = tournamentBean.getPartialTeams();
            if (!partials.isEmpty()) {
                Label reservedTeams = new Label("Partial teams:");
                teams.add(reservedTeams);
                teams.addAll(getPlayers(partials));
            }
        }
        for (Label label : teams) {
            label.getStyleClass().add(ACCORDION_LABEL);
        }
        return teams;
    }

    protected List<Label> getConfirmedTeams(TournamentBean tournamentBean) {
        List<Label> teams = new ArrayList<>();
        List<TeamBean> confirmed = tournamentBean.getConfirmedTeams();
        if (!confirmed.isEmpty()) {
            Label confirmedTeams = new Label("Confirmed teams:");
            teams.add(confirmedTeams);
            teams.addAll(getPlayers(confirmed));
        }
        for (Label label : teams) {
            label.getStyleClass().add(ACCORDION_LABEL);
        }
        return teams;
    }

    private List<Label> getPlayers(List<TeamBean> teams) {
        List<Label> players = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            boolean first = true;
            for (PlayerBean playerBean : teams.get(i).getPlayers()) {
                if (playerBean != null) {
                    Label player;
                    if (first) {
                        int number = i + 1;
                        player = new Label("\t" + number + ". " + playerBean.getUsername());
                        first = false;
                    } else {
                        player = new Label("\t    " + playerBean.getUsername());
                    }
                    players.add(player);
                }
            }
        }
        return players;
    }

    protected List<Label> getClubDetails(TournamentBean tournamentBean) {
        List<Label> details = new ArrayList<>();
        ClubBean clubBean = tournamentBean.getClub();
        Label clubDetails = new Label("Club details:");
        details.add(clubDetails);
        Label clubName = new Label("Club name: " + clubBean.getName());
        details.add(clubName);
        Label clubStreet = new Label("Club street: " + clubBean.getStreet());
        details.add(clubStreet);
        Label clubNumber = new Label("Club number: " + clubBean.getNumber());
        details.add(clubNumber);
        Label clubCity = new Label("Club city: " + clubBean.getCity());
        details.add(clubCity);
        Label clubZip = new Label("Club zip code: " + clubBean.getZip());
        details.add(clubZip);
        Label clubState = new Label("Club state: " + clubBean.getState());
        details.add(clubState);
        Label clubCountry = new Label("Club country: " + clubBean.getCountry());
        details.add(clubCountry);
        Label clubPhone = new Label("Club phone: " + clubBean.getPhone());
        details.add(clubPhone);
        return details;
    }

    protected List<Label> getOwnerDetails(TournamentBean tournamentBean) {
        List<Label> details = new ArrayList<>();
        HostBean owner = tournamentBean.getClub().getOwner();
        Label clubOwnerDetails = new Label("Club owner details:");
        details.add(clubOwnerDetails);
        Label clubOwnerName = new Label("Name: " + owner.getUsername());
        details.add(clubOwnerName);
        Label clubOwnerEmail = new Label("Email: " + owner.getEmail());
        details.add(clubOwnerEmail);
        return details;
    }
}
