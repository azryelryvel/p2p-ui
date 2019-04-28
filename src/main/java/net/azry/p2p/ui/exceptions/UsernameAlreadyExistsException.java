package net.azry.p2p.ui.exceptions;

public class UsernameAlreadyExistsException extends IllegalArgumentException {
    public UsernameAlreadyExistsException() {
       super();
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
