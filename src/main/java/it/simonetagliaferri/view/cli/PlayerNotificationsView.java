package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.utils.CliUtils;

import java.util.List;

public class PlayerNotificationsView {

    public void listNotifications(List<TournamentBean> tournaments) {
        for (TournamentBean tournamentBean : tournaments) {
            CliUtils.println("Club " + tournamentBean.getClub().getName() + " just added a new tournament: " + tournamentBean.getTournamentName());
            CliUtils.println("");
        }
    }

}
