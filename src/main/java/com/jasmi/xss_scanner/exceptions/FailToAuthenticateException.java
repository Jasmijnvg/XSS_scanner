package com.jasmi.xss_scanner.exceptions;

public class FailToAuthenticateException extends RuntimeException {

    public FailToAuthenticateException() {
        super ("Failed to Authenticate");
    }

    public FailToAuthenticateException(String message) {
        super(message);
    }
}
