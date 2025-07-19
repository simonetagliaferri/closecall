package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.ArrayList;
import java.util.List;

public class HostMapper {

    private HostMapper() {}

    public static HostBean toBean(Host host) {
        return new HostBean(host.getUsername(), host.getEmail());
    }

    public static Host fromBean(HostBean hostBean) {
        Host host = new Host(hostBean.getUsername(), hostBean.getEmail());
        List<Tournament> tournaments = new ArrayList<>();
        if (hostBean.getTournaments() != null) {
            for (TournamentBean torunamentBean : hostBean.getTournaments()) {
                tournaments.add(TournamentMapper.fromBean(torunamentBean));
            }
        }
        host.setTournaments(tournaments);
        List<Club> clubs = new ArrayList<>();
        if (hostBean.getClubs() != null) {
            for (ClubBean clubBean : hostBean.getClubs()) {
                clubs.add(ClubMapper.fromBean(clubBean));
            }
        }
        host.setClubs(clubs);
        return host;
    }
}
