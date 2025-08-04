package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;

public class ClubKey {
    private String name;
    private String street;
    private String number;
    private String city;
    private String hostUsername;

    ClubKey() {}

    ClubKey(Club club) {
        this.name = club.getName();
        this.street = club.getStreet();
        this.number = club.getNumber();
        this.city = club.getCity();
        this.hostUsername = club.getOwner().getUsername();
    }

    protected static ClubKey of(Club club) {
        return new ClubKey(club);
    }

    protected Club toClub(Host owner) {
        return new Club(this.name, this.street, this.number, this.city, owner);
    }

    @Override
    public String toString() {
        return this.name + "|" + this.street + "|" + this.number + "|" + this.city + "|" + this.hostUsername;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

}

