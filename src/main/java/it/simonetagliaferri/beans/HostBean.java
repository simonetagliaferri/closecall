package it.simonetagliaferri.beans;


public class HostBean extends UserBean {
    private String clubName = "Carlo";

    public HostBean(String username, String email) {
        super();
        this.username=username;
        this.email=email;
    }

    public HostBean(String username) {
        super();
        this.username=username;
    }

    public String getClubName() {
        return clubName;
    }
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

}
