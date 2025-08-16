package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.utils.converters.TournamentMapper;
import it.simonetagliaferri.view.cli.JoinTournamentView;
import java.util.ArrayList;
import java.util.List;

public class JoinTournamentApplicationController extends ApplicationController {

    TournamentDAO tournamentDAO;
    ClubDAO clubDAO;
    HostDAO hostDAO;
    PlayerDAO playerDAO;

    public JoinTournamentApplicationController(SessionManager sessionManager, TournamentDAO tournamentDAO, ClubDAO clubDAO, HostDAO hostDAO, PlayerDAO playerDAO) {
        super(sessionManager);
        this.tournamentDAO=tournamentDAO;
        this.clubDAO=clubDAO;
        this.hostDAO = hostDAO;
        this.playerDAO = playerDAO;
    }

    private Player loadPlayer() {
        User user = getCurrentUser();
        return playerDAO.findByUsername(user.getUsername());
    }

    private Host loadHost(TournamentBean tournament) {
        ClubBean clubBean = tournament.getClub();
        HostBean hostBean = clubBean.getOwner();
        Host host = hostDAO.getHostByUsername(hostBean.getUsername());
        Club club = clubDAO.getClubByHostName(host.getUsername());
        List<Tournament> tournaments = tournamentDAO.getTournaments(club);
        host.addClub(club);
        club.setClubTournaments(tournaments);
        return host;
    }

    private List<Tournament> loadTournaments(String search) {
        List<Tournament> tournaments = tournamentDAO.getTournamentsByCity(search);
        for (Tournament tournament : tournaments) {
            String hostName = tournament.getClub().getOwner().getUsername();
            Host host = hostDAO.getHostByUsername(hostName);
            Club club = clubDAO.getClubByHostName(hostName);
            host.addClub(club);
            tournament.setClub(club);
        }
        return tournaments;
    }

    private Club loadClub(TournamentBean tournament) {
        ClubBean clubBean = tournament.getClub();
        HostBean hostBean = clubBean.getOwner();
        Host host = hostDAO.getHostByUsername(hostBean.getUsername());
        return clubDAO.getClubByHostName(host.getUsername());
    }

    public List<TournamentBean> searchTournament(String search) {
        List<TournamentBean> tournamentBeanList = new ArrayList<>();
        List<Tournament> tournaments = loadTournaments(search);
        for (Tournament tournament : tournaments) {
            tournamentBeanList.add(TournamentMapper.toBean(tournament));
        }
        return tournamentBeanList;
    }

    public JoinTournamentView.JoinError joinTournament(TournamentBean tournamentBean) {
        String tournamentName = tournamentBean.getTournamentName();
        Host host = loadHost(tournamentBean);
        Club club = host.getClub();
        Tournament tournament = club.getTournament(tournamentName);
        Player player = loadPlayer();
        JoinTournamentView.JoinError res = tournament.addPlayer(player);
        if (res == JoinTournamentView.JoinError.SUCCESS) {
            tournamentDAO.saveTournament(tournament.getClub(), tournament);
            clubDAO.saveClub(club);
            playerDAO.savePlayer(player);
            hostDAO.saveHost(host);
        }
        return res;
    }

    public boolean isNotSubscribed(TournamentBean tournamentBean) {
        Club club = loadClub(tournamentBean);
        Player player = loadPlayer();
        return club.isNotSubscribed(player);
    }

    public void addClubToFavourites(TournamentBean tournamentBean) {
        Club club = loadClub(tournamentBean);
        Player player = loadPlayer();
        club.subscribe(player);
        clubDAO.saveClub(club);
        playerDAO.savePlayer(player);
    }

}
