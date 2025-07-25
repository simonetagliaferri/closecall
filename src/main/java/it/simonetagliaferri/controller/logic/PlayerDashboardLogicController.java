package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.utils.converters.ClubMapper;
import it.simonetagliaferri.utils.converters.InviteMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class PlayerDashboardLogicController extends LogicController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;
    ClubDAO clubDAO;
    InviteDAO inviteDAO;

    public PlayerDashboardLogicController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO, ClubDAO clubDAO, InviteDAO inviteDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
        this.inviteDAO = inviteDAO;
    }

    public List<TournamentBean> searchTournament(String search) {
        List<TournamentBean> tournamentBeanList = new ArrayList<>();
        List<Tournament> tournaments = tournamentDAO.getTournamentsByCity(search);
        for (Tournament tournament : tournaments) {
            tournamentBeanList.add(TournamentMapper.toBean(tournament));
        }
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

    public List<InviteBean> getInvites() {
        List<InviteBean> inviteBeanList = new ArrayList<>();
        List<Invite> invites = inviteDAO.getInvites(sessionManager.getCurrentUser().getUsername());
        if (invites != null && !invites.isEmpty()) {
            for (Invite invite : invites) {
                InviteBean inviteBean = InviteMapper.toBean(invite);
                inviteBeanList.add(inviteBean);
            }
        }
        return inviteBeanList;
    }
}
