package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.observer.Publisher;
import it.simonetagliaferri.model.observer.Subscriber;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Club implements Publisher {
    private String name;
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

    public boolean tournamentAlreadyExists(Tournament tournament) {
        for (Tournament to : tournaments) {
            if (to.equals(tournament)) {
                return true;
            }
        }
        return false;
    }
}
