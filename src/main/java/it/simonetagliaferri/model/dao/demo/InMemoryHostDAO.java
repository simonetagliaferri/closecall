package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;

import java.util.Map;

public class InMemoryHostDAO implements HostDAO {

    private final Map<String, Host> hosts;

    protected InMemoryHostDAO(Map<String, Host> hosts) {
        this.hosts = hosts;
    }

    @Override
    public Host getHostByUsername(String username) {
        return hosts.get(username);
    }

    @Override
    public void saveHost(Host host) {
        hosts.put(host.getUsername(), host);
    }


}
