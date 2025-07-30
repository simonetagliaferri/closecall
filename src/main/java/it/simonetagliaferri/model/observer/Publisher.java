package it.simonetagliaferri.model.observer;

import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

public interface Publisher {
    void subscribe(Player player);
    void unsubscribe(Player player);
    void notifySubscribers(Tournament tournament);
}
