package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class JoinTournamentLogicController extends LogicController {

    TournamentDAO tournamentDAO;

    public JoinTournamentLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO) {
        super(sessionManager);
        this.tournamentDAO=tournamentDAO;
    }

    public List<TournamentBean> searchTournament(String search) {
        List<TournamentBean> tournamentBeanList = new ArrayList<>();
        List<Tournament> tournaments = tournamentDAO.getTournamentsByCity(search);
        for (Tournament tournament : tournaments) {
            tournamentBeanList.add(TournamentMapper.toBean(tournament));
        }
        return tournamentBeanList;
    }

}
