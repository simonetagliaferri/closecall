package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.converters.TournamentMapper;
import it.simonetagliaferri.view.cli.JoinTournamentView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JoinTournamentLogicController extends LogicController {

    TournamentDAO tournamentDAO;
    ClubDAO clubDAO;
    HostDAO hostDAO;
    PlayerDAO playerDAO;

    public JoinTournamentLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO, ClubDAO clubDAO, HostDAO hostDAO, PlayerDAO playerDAO) {
        super(sessionManager);
        this.tournamentDAO=tournamentDAO;
        this.clubDAO=clubDAO;
        this.hostDAO = hostDAO;
        this.playerDAO = playerDAO;
    }

    public List<TournamentBean> searchTournament(String search) {
        List<TournamentBean> tournamentBeanList = new ArrayList<>();
        List<Tournament> tournaments = tournamentDAO.getTournamentsByCity(search);
        for (Tournament tournament : tournaments) {
            tournamentBeanList.add(TournamentMapper.toBean(tournament));
        }
        return tournamentBeanList;
    }

    public Tournament getTournamentFromBean(TournamentBean tournamentBean) {
        ClubBean clubBean = tournamentBean.getClub();
        HostBean hostBean = clubBean.getOwner();
        Host host = hostDAO.getHostByUsername(hostBean.getUsername());
        Club club = host.getClub();
        String tournamentName = tournamentBean.getTournamentName();
        String tournamentFormat = tournamentBean.getTournamentFormat();
        String tournamentType = tournamentBean.getTournamentType();
        LocalDate tournamentStartDate = tournamentBean.getStartDate();
        return club.getTournament(tournamentName, tournamentFormat, tournamentType, tournamentStartDate);
    }

    public Club getClubFromBean(TournamentBean tournamentBean) {
        ClubBean clubBean = tournamentBean.getClub();
        HostBean hostBean = clubBean.getOwner();
        Host host = hostDAO.getHostByUsername(hostBean.getUsername());
        return host.getClub();
    }

    public JoinTournamentView.JoinError joinTournament(TournamentBean tournamentBean) {
        Tournament tournament = getTournamentFromBean(tournamentBean);
        Player player = playerDAO.findByUsername(getCurrentUser().getUsername());
        JoinTournamentView.JoinError res = tournament.addPlayer(player);
        Club club = tournament.getClub();
        Host host = club.getOwner();
        if (res == JoinTournamentView.JoinError.SUCCESS) {
            tournamentDAO.saveTournament(tournament.getClub(), tournament);
            clubDAO.saveClub(club);
            playerDAO.savePlayer(player);
            hostDAO.saveHost(host);
        }
        return res;
    }

    public boolean isSubscribed(TournamentBean tournamentBean) {
        Club club = getClubFromBean(tournamentBean);
        Player player = playerDAO.findByUsername(getCurrentUser().getUsername());
        return club.isSubscribed(player);
    }

    public void addClubToFavourites(TournamentBean tournamentBean) {
        Club club = getClubFromBean(tournamentBean);
        Player player = playerDAO.findByUsername(getCurrentUser().getUsername());
        club.subscribe(player);
        clubDAO.saveClub(club);
        playerDAO.savePlayer(player);
    }

}
