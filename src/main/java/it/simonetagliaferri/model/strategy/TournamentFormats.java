package it.simonetagliaferri.model.strategy;

public enum TournamentFormats {
    ROUND_ROBIN("RoundRobin"),
    SINGLE_ELIMINATION("Single-Elimination"),
    DOUBLE_ELIMINATION("Double-Elimination");

    private final String label;

    TournamentFormats(String label) {
        this.label = label;
    }

    public static TournamentFormats fromString(String label) {
        for (TournamentFormats format : values()) {
            if (format.label.equalsIgnoreCase(label)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown tournament format: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}

