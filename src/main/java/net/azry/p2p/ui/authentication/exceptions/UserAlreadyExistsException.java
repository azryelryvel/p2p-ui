package net.azry.p2p.ui.authentication.exceptions;

public class UserAlreadyExistsException extends IllegalArgumentException {
    UserAlreadyExistsException() {
       super();
    }

    UserAlreadyExistsException(String message) {
        super(message);
    }

    UserAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
