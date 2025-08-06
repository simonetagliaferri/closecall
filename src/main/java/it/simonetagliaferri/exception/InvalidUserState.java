package it.simonetagliaferri.exception;

public class InvalidUserState extends RuntimeException {
    public InvalidUserState(String message) {
        super(message);
    }
}
