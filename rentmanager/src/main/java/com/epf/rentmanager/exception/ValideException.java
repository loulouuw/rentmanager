package com.epf.rentmanager.exception;

public class ValideException extends Exception {
    public ValideException(String message) {
        super(message);
    }

    public ValideException(Throwable cause) {
        super(cause);
    }
}
