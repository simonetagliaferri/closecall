package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.utils.converters.ClubMapper;

import java.util.ArrayList;
import java.util.List;

public class PlayerDashboardLogicController extends LogicController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;
    ClubDAO clubDAO;

    public PlayerDashboardLogicController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
    }

    public List<TournamentBean> searchTournament(String search) {
        List<TournamentBean> tournamentBeanList = new ArrayList<>();
        return tournamentBeanList;
    }

    public List<ClubBean> searchClub(String search) {
        List<ClubBean> clubBeanList = new ArrayList<>();
        List<Club> clubs = clubDAO.getClubsByCity(search);
        for (Club club : clubs) {
            ClubBean clubBean = ClubMapper.toBean(club);
            clubBeanList.add(clubBean);
        }
        return clubBeanList;
    }
}
