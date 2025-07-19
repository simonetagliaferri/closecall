package it.simonetagliaferri.beans;

import java.util.List;

public class HostBean extends UserBean {
    private List<TournamentBean> tournaments;
    private List<ClubBean> clubs;

    public HostBean(String username, String email) {
        super();
        this.username=username;
        this.email=email;
    }

    public HostBean(String username) {
        super();
        this.username=username;
    }

    public List<TournamentBean> getTournaments() {
        return tournaments;
    }
    public void setTournaments(List<TournamentBean> tournaments) {
        this.tournaments = tournaments;
    }
    public List<ClubBean> getClubs() {
        return clubs;
    }
    public void setClubs(List<ClubBean> clubs) {
        this.clubs = clubs;
    }

}
