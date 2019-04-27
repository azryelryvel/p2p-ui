package net.azry.p2p.ui.authentication.exceptions;

public class UserAlreadyExistsException extends IllegalArgumentException {
    public UserAlreadyExistsException() {
       super();
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
