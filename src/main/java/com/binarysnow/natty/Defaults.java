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

    /**
     * The default frame size
     */
    public static final long FRAME_SIZE;
    public static final boolean TCP_NODELAY;

    static {
        HOST = "localhost";
        PORT = 4222;
        FRAME_SIZE = 1024;
        TCP_NODELAY = false;
    }

    private Defaults() {
        // Hide the constructor
    }

}
