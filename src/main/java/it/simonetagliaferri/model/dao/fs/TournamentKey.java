package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;

import java.time.LocalDate;

public class TournamentKey {
    private String name;
    private String format;
    private String type;
    private LocalDate startDate;
    private ClubKey clubKey;

    TournamentKey() {} // Jackson needs default constructor

    TournamentKey(Tournament t) {
        this.name = t.getTournamentName();
        this.format = t.getTournamentFormat();
        this.type = t.getTournamentType();
        this.startDate = t.getStartDate();
        this.clubKey = ClubKey.of(t.getClub());
    }

    protected static TournamentKey of(Tournament t) {
        return new TournamentKey(t);
    }

    protected Tournament toTournament(Host owner) {
        return new Tournament(this.name, this.format, this.type, this.startDate, this.clubKey.toClub(owner));
    }

    @Override
    public String toString() {
        return this.name + "|" + this.format + "|" + this.type + "|" + this.startDate + "|" + this.clubKey.toString();
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setClubKey(ClubKey clubKey) {
        this.clubKey = clubKey;
    }
}
