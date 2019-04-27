package net.azry.p2p.ui.authentication.exceptions;

public class UserNotAuthenticatedException extends IllegalArgumentException {
    public UserNotAuthenticatedException() {
       super();
    }

    public UserNotAuthenticatedException(String message) {
        super(message);
    }

    public UserNotAuthenticatedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
