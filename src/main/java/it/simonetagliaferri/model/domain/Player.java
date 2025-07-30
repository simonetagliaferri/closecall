package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.observer.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends User implements Subscriber {

    public Player(String username, String email, Role role) {
        super(username, email, role);
    }
    public Player(String username, String email) {
        super(username, email);
    }

    private List<Club> favouriteClubs;
    private Map<Club, List<Tournament>> notifications = new HashMap<>();

    @Override
    public void update(Club club, Tournament newTournament) {
        if (favouriteClubs == null) { favouriteClubs = new ArrayList<>(); }
        notifications.computeIfAbsent(club, k -> new ArrayList<>()).add(newTournament);
    }

    public List<Tournament> getNotificationsForClub(Club club) {
        return notifications.getOrDefault(club, List.of());
    }

    public Map<Club, List<Tournament>> getNotifications() {
        return notifications;
    }

    public void clearNotificationsForClub(Club club) {
        List<Tournament> list = notifications.get(club);
        if (list != null) {
            list.clear(); // Keep the key, just empty the list
        }
    }


}
