package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FSHostDAO extends FSDAO implements HostDAO {

    private static final String FILE_NAME = "hosts.db";
    private final Map<String, Host> hosts;

    public FSHostDAO(Path baseDir) {
        super(baseDir, FILE_NAME);
        hosts = new HashMap<>();
        loadHosts();
    }

    @SuppressWarnings("unchecked")
    private void loadHosts() {
        Path f = file();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                Map<String, Host> loaded = (Map<String, Host>) obj;
                hosts.clear();
                hosts.putAll(loaded);
            }
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Error loading hosts", e);
        }
    }

    private void saveHosts() {
        Path f = file();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(f, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(hosts);
        } catch (IOException e) {
            throw new DAOException("Error saving hosts", e);
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
