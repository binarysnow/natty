package com.binarysnow.natty;

public class ConnectionProperties {
    private String address = Defaults.HOST;
    private int port = Defaults.PORT;
    private Long maxFrameSize = Defaults.FRAME_SIZE;

    public ConnectionProperties() {

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
}
