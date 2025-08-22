package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Host;

public interface HostDAO {
    Host getHostByUsername(String username);

    void saveHost(Host host);
}
