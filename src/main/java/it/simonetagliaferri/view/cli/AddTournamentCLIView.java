package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class AddTournamentCLIView {
    public void welcome() {
        CliUtils.println("Welcome to the tournament generation.");
    }

    public String tournamentName() {
        return CliUtils.prompt("Enter tournament name: ");
    }

    public int tournamentType() {
        CliUtils.println("Enter tournament type: ");
        CliUtils.println("1. Men's singles");
        CliUtils.println("2. Women's singles");
        CliUtils.println("3. Men's doubles");
        CliUtils.println("4. Women's doubles");
        CliUtils.println("5. Mixed doubles");
        return CliUtils.promptInt("Enter tournament type: ");
    }

    public int tounrmanetFormat() {
        CliUtils.println("Enter tournament format: ");
        CliUtils.println("1. RoundRobin");
        CliUtils.println("2. Single-elimination(Knockout)");
        CliUtils.println("3. Double-elimination");
        return CliUtils.promptInt("Enter tournament format: ");
    }

    public int matchFormat() {
        CliUtils.println("Enter match format: ");
        CliUtils.println("1. Best-of-three sets");
        CliUtils.println("2. Best-of-five sets");
        return CliUtils.promptInt("Enter match format: ");
    }
}
