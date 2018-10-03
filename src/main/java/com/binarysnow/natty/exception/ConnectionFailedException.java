package com.binarysnow.natty.exception;

public class ConnectionFailedException extends Exception {
    public ConnectionFailedException(final Exception exception) {
        super(exception);
    }
}
