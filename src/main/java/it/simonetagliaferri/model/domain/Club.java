package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.observer.Publisher;
import it.simonetagliaferri.model.observer.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Club implements Publisher, Serializable {
    private final String name;
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

    public Club(String name, Host owner) {
        this.name = name;
        this.owner = owner;
    }

    public Club(String name) {
        this.name = name;
    }

    public Club(String name, String street, String number, String city, Host owner) {
        this.name = name;
        this.street = street;
        this.number = number;
        this.city = city;
        this.owner = owner;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribedPlayers = subscribers;
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

    public void setOwner(Host owner) {
        this.owner = owner;
    }

    public String getOwnerUsername() {
        return this.owner.getUsername();
    }

    public void setClubTournaments(List<Tournament> clubTournaments) {
        for (Tournament tournament : clubTournaments) {
            tournament.setClub(this);
            tournament.subscribe(owner);
        }
        this.clubTournaments = clubTournaments;
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

    public List<Player> getSubscribedPlayers() {
        List<Player> subscribers = new ArrayList<>();
        for (Subscriber subscriber : this.subscribedPlayers) {
            if (subscriber instanceof Player) {
                subscribers.add((Player) subscriber);
            }
        }
        return subscribers;
    }

    public boolean isNotSubscribed(Player player) {
        if (subscribedPlayers != null) {
            for (Subscriber subscriber : subscribedPlayers) {
                if (subscriber instanceof Player && (subscriber).equals(player)) {
                    return false;
                }
            }
        }
        return true;
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
        return true;
    }

    public boolean tournamentAlreadyExists(Tournament tournament) {
        return getTournament(tournament.getName()) != null;
    }

    public Tournament getTournament(String tournamentName) {
        for (Tournament tournament : clubTournaments) {
            if (tournament.getName().equals(tournamentName)) {
                return tournament;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Club)) return false;
        Club club = (Club) o;
        if (club == this) return true;
        return Objects.equals(getName(), club.getName()) && Objects.equals(getOwner(), club.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getOwner());
    }
}
