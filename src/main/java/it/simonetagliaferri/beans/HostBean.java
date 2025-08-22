package it.simonetagliaferri.beans;

import java.util.List;

public class HostBean extends UserBean {
    private List<TournamentBean> tournaments;

    public HostBean(String username, String email) {
        super();
        this.username = username;
        this.email = email;
    }

    public List<TournamentBean> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<TournamentBean> tournaments) {
        this.tournaments = tournaments;
    }

}
