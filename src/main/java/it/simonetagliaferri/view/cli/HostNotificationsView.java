package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.utils.CliUtils;

public class HostNotificationsView {

    public void tournament(TournamentBean tournamentBean) {
        CliUtils.println("Tournament: " + tournamentBean.getTournamentName());
        CliUtils.println("Players that joined since last time: ");
    }

    public void newPlayer(PlayerBean playerBean) {
        CliUtils.println(playerBean.getUsername());
    }
}
