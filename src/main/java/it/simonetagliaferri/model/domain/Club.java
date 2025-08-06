package it.simonetagliaferri.model.domain;
import it.simonetagliaferri.model.observer.Publisher;
import it.simonetagliaferri.model.observer.Subscriber;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Club implements Publisher, Serializable {
    private String name;
    private String street;
    private String number;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private Host owner;
    private List<Tournament> clubTournaments = new ArrayList<>();
    private List<Subscriber> subscribedPlayers = new ArrayList<>();

    public Club() {}

    public Club(String name, Host owner) {
        this.name = name;
        this.owner = owner;
    }

    public Club(String name) {
        this.name = name;
    }

    public void setOwner(Host owner) {
        this.owner = owner;
    }

    public Club(String name, String street, String number, String city, Host owner) {
        this.name = name;
        this.street = street;
        this.number = number;
        this.city = city;
        this.owner = owner;
    }

    public void updateAddress(String street, String number, String city, String state, String zip, String country) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    public void updateContacts(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public Host getOwner() {
        return this.owner;
    }

    @Override
    public void subscribe(Subscriber player) {
        if (subscribedPlayers == null) {
            subscribedPlayers = new ArrayList<>();
        }
        subscribedPlayers.add(player);
    }

    @Override
    public void unsubscribe(Subscriber player) {
        if (subscribedPlayers != null) {
            subscribedPlayers.remove(player);
        }
    }

    @Override
    public void notifySubscribers(Tournament tournament) {
        if (subscribedPlayers != null) {
            for (Subscriber subscriber : subscribedPlayers) {
                subscriber.update(this, tournament);
            }
        }
    }

    public boolean isSubscribed(Player player) {
        if (subscribedPlayers != null) {
            return subscribedPlayers.contains(player);
        }
        return false;
    }

    public boolean addTournament(Tournament tournament) {
        if (clubTournaments == null) {
            clubTournaments = new ArrayList<>();
        }
        if (tournamentAlreadyExists(tournament)) {
            return false;
        }
        tournament.setClub(this);
        clubTournaments.add(tournament);
        tournament.subscribe(owner);
        notifySubscribers(tournament);
        return true;
    }

    public List<Tournament> getClubTournaments() {
        return clubTournaments;
    }

    private boolean tournamentAlreadyExists(Tournament tournament) {
        return clubTournaments.contains(tournament);
    }

    public Tournament getTournament(String tournamentName, String tournamentFormat, String tournamentType, LocalDate tournamentStartDate) {
        for (Tournament tournament : clubTournaments) {
            if (tournament.getName().equals(tournamentName) &&
            tournament.getTournamentFormat().equals(tournamentFormat) &&
            tournament.getTournamentType().equals(tournamentType) &&
            tournament.getStartDate().equals(tournamentStartDate)) {
                return tournament;
            }
        }
        return null;
    }

}
