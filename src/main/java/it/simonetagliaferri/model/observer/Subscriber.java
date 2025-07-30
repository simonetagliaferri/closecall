package it.simonetagliaferri.model.observer;

import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;

public interface Subscriber {
    void update(Club club, Tournament tournament);
}
