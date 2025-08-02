package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.observer.Publisher;
import it.simonetagliaferri.model.observer.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Club implements Publisher {
    private final String name;
    private String street;
    private String number;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String email;
    private Host owner;
    private List<Tournament> tournaments;
    private List<Subscriber> subscribers;

    public Club(String name, Host owner) {
        this.name = name;
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

    public void updateContacts(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public void setOwner(Host owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return street + " " + number + " " + city + " " + state + " " + zip + " " + country;
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

    public String getEmail() {
        return email;
    }

    public Host getHost() {
        return this.owner;
    }


    @Override
    public void subscribe(Subscriber player) {
        if (subscribers == null) {
            subscribers = new ArrayList<>();
        }
        subscribers.add(player);
    }

    @Override
    public void unsubscribe(Subscriber player) {
        if (subscribers != null) {
            subscribers.remove(player);
        }
    }

    @Override
    public void notifySubscribers(Tournament tournament) {
        if (subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.update(this, tournament);
            }
        }
    }

    public boolean isSubscribed(Player player) {
        if (subscribers != null) {
            return subscribers.contains(player);
        }
        return false;
    }

    public boolean addTournament(Tournament tournament) {
        if (tournaments == null) {
            tournaments = new ArrayList<>();
        }
        if (tournamentAlreadyExists(tournament)) {
            return false;
        }
        tournaments.add(tournament);
        return true;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    private boolean tournamentAlreadyExists(Tournament tournament) {
        return tournaments.contains(tournament);
    }

    public Tournament getTournament(Tournament tournament) {
        int index = tournaments.indexOf(tournament);
        return index >= 0 ? tournaments.get(index) : null;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Club)) return false;
        Club club = (Club) o;
        if (this == club) return true;
        return Objects.equals(getName(), club.getName()) && Objects.equals(getStreet(), club.getStreet()) && Objects.equals(getNumber(), club.getNumber()) && Objects.equals(getCity(), club.getCity()) && Objects.equals(owner, club.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getStreet(), getNumber(), getCity(), owner);
    }
}
