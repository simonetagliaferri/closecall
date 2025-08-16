package it.simonetagliaferri.model.observer;

import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;

import java.io.Serializable;

public interface Subscriber extends Serializable {

    void update(Club club, Tournament tournament);
}
