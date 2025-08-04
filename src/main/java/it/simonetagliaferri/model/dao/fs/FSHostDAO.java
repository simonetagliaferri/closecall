package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.utils.CliUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FSHostDAO extends FSDAO implements HostDAO {

    private final Map<String, Host> hosts;

    protected FSHostDAO() {
        super("hosts.json");
        hosts = new HashMap<>();
        loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        if (!file.exists()) return;

        try {
            // Deserialize the map of hosts from JSON
            Map<String, Host> loaded = mapper.readValue(file,
                    mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Host.class));

            if (loaded != null) {
                hosts.clear();
                hosts.putAll(loaded);
            }

            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error loading hosts: " + e.getMessage());
        }
    }


    @Override
    public Host getHostByUsername(String username) {
        reloadIfChanged();
        return hosts.get(username);
    }

    @Override
    public void saveHost(Host host) {
        reloadIfChanged();
        hosts.put(host.getUsername(), host);
        saveHosts();
    }

    private void saveHosts() {
        try {
            // Serialize the map of hosts back to JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, hosts);
            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error saving hosts: " + e.getMessage());
        }
    }

}
