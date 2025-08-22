package it.simonetagliaferri.beans;

public class ClubBean {
    private String name;
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;
    private String zip;
    private String phone;
    private HostBean owner;

    public String getName() {
        return name;
    }

    public void setName(String clubName) {
        this.name = clubName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HostBean getOwner() {
        return owner;
    }

    public void setOwner(HostBean owner) {
        this.owner = owner;
    }

}
