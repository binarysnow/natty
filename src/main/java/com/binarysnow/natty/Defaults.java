package com.binarysnow.natty;

public final class Defaults {
    /**
     * The default NATS hostname
     */
    public static final String HOST;

    /**
     * The default NATS port
     */
    public static final int PORT;

    static {
        HOST = "localhost";
        PORT = 4222;
    }

    private Defaults() {
        // Hide the constructor
    }

}
