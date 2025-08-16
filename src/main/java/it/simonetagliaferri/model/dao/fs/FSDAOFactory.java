package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FSDAOFactory extends DAOFactory {

    private final Path baseDir;

    public FSDAOFactory() {
        this(Paths.get("data"));
    }

    /** Tests: point to @TempDir so every test is isolated. */
    public FSDAOFactory(Path baseDir) {
        this.baseDir = baseDir;
    }

    private LoginDAO loginDAO;
    private TournamentDAO tournamentDAO;
    private HostDAO hostDAO;
    private PlayerDAO playerDAO;
    private ClubDAO clubDAO;

    @Override
    public LoginDAO getLoginDAO() {
        if (loginDAO == null) {
            loginDAO = new FSLoginDAO(baseDir);
        }
        return loginDAO;
    }

    @Override
    public TournamentDAO getTournamentDAO() {
        if (tournamentDAO == null) {
            tournamentDAO = new FSTournamentDAO(baseDir);
        }
        return tournamentDAO;
    }

    @Override
    public HostDAO getHostDAO() {
        if (hostDAO == null) {
            hostDAO = new FSHostDAO(baseDir);
        }
        return hostDAO;
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        if (playerDAO == null) {
            playerDAO = new FSPlayerDAO(baseDir);
        }
        return playerDAO;
    }

    @Override
    public ClubDAO getClubDAO() {
        if (clubDAO == null) {
            clubDAO = new FSClubDAO(baseDir);
        }
        return clubDAO;
    }

}
