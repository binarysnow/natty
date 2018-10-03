package com.binarysnow.natty;

public class ConnectionProperties {
    private String address = Defaults.HOST;
    private int port = Defaults.PORT;
    private Long maxFrameSize = Defaults.FRAME_SIZE;
    private boolean tcpNoDelay = Defaults.TCP_NODELAY;

    /**
     * Construct a ConnectionProperties object with default values
     */
    public ConnectionProperties() {
    }

    public ConnectionProperties(final ConnectionProperties connectionProperties) {
        this.address = connectionProperties.address;
        this.port = connectionProperties.port;
        this.maxFrameSize = connectionProperties.maxFrameSize;
        this.tcpNoDelay = connectionProperties.tcpNoDelay;
    }

    public ConnectionProperties setAddress(final String address) {
        this.address = address;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ConnectionProperties setPort(final int port) {
        this.port = port;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ConnectionProperties setMaxFrameSize(final long maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        return this;
    }

    public long getMaxFrameSize() {
        return maxFrameSize;
    }

    public ConnectionProperties setTcpNoDelay(final boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    public boolean getTcpNoDelay() {
        return tcpNoDelay;
    }
}
