package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;

public class InMemoryHostDAO implements HostDAO {
    @Override
    public boolean additionalInfo(Host host) {
        return true;
    }
}
