package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;

public class FSHostDAO implements HostDAO {
    @Override
    public boolean additionalInfo(Host host) {
        return true;
    }
}
