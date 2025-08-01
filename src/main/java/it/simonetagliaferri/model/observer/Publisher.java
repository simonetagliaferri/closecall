package it.simonetagliaferri.model.observer;

import it.simonetagliaferri.model.domain.Tournament;

public interface Publisher {
    void subscribe(Subscriber subscriber);
    void unsubscribe(Subscriber subscriber);
    void notifySubscribers(Tournament tournament);
}
