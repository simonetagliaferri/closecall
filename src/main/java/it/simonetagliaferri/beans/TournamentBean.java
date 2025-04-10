package it.simonetagliaferri.beans;

import java.util.Date;
import java.util.List;

public class TournamentBean {
    private String name;
    private String tournamentType;
    private String tournamentFormat;
    private String matchFormat;
    private int teamsNumber;
    private List<Float> prizes;
    private Date startDate;
    private Date endDate;


    public TournamentBean(String name, String tournamentType, String tournamentFormat, String matchFormat) {
        this.name = name;
        this.tournamentType = tournamentType;
        this.tournamentFormat = tournamentFormat;
        this.matchFormat = matchFormat;
    }
}
