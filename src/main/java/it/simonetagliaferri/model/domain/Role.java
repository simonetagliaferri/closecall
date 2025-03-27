package it.simonetagliaferri.model.domain;

public enum Role {
    PLAYER(1),
    HOST(2);
    private final int id;
    Role(int id) {this.id = id;}
    public static Role fromInt(int id) {
        for (Role role : Role.values()) {
            if (role.id == id) {
                return role;
            }
        }
        return null;
    }
}
