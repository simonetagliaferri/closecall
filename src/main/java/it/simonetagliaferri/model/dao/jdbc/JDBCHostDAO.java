package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;

public class JDBCHostDAO implements HostDAO {
    @Override
    public boolean additionalInfo(Host host) {
        return true;
    }
}
