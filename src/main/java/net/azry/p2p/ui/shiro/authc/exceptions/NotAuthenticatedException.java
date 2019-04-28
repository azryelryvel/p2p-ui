package net.azry.p2p.ui.shiro.authc.exceptions;

import org.apache.shiro.authc.AuthenticationException;

public class NotAuthenticatedException extends AuthenticationException {

	public NotAuthenticatedException() {
		super();
	}

	public NotAuthenticatedException(String message) {
		super(message);
	}

	public NotAuthenticatedException(Throwable cause) {
		super(cause);
	}

	public NotAuthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}
}
