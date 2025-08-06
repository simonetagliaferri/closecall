package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FSHostDAO implements HostDAO {

    private final String outputFile = "hosts.db";
    private Map<String, Host> hosts;

    public FSHostDAO() {
        hosts = new HashMap<>();
        loadHosts();
    }

    @SuppressWarnings("unchecked")
    private void loadHosts() {
        File file = new File(outputFile);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            hosts = (Map<String, Host>) ois.readObject();
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveHosts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            oos.writeObject(hosts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Host getHostByUsername(String username) {
        return hosts.get(username);
    }


    @Override
    public void saveHost(Host host) {
        hosts.put(host.getUsername(), host);
        saveHosts();
    }
}
