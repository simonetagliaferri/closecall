package it.simonetagliaferri.exception;

public class ClubAlreadyAddedException extends RuntimeException {

    public ClubAlreadyAddedException(String message) {
        super(message);
    }
}
