package com.binarysnow.natty;

import com.binarysnow.natty.frame.server.Info;
import com.binarysnow.natty.frame.server.Publish;
import com.binarysnow.natty.io.Initialiser;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NatsClient {

    private final static long DEFAULT_FRAME_SIZE = 1024;

    private final String address;
    private final int port;

    private Channel channel;

    private long maxFrameSize = DEFAULT_FRAME_SIZE;

    /**
     * Construct a NatsClient
     * @param address The address of the NATS server
     * @param port The port number of the NATS server
     */
    public NatsClient(final String address, final int port) {
        this.address = address;
        this.port = port;
    }

    public void connect() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new Initialiser(this));
        bootstrap.remoteAddress(address, port);

        channel = bootstrap.connect().sync().channel();
    }

    /**
     * Get the maximum frame size for the connection
     * @return The maximum frame size for the connection
     */
    public long getMaxFrameSize() {
        return maxFrameSize;
    }

    /**
     * Set the maximum frame size for the connection
     * @param maxFrameSize The maximum frame size for the connection
     */
    public void setMaxFrameSize(long maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    /**
     * Publish a message
     */
    public void publish(final String subject, final byte[] data) {
        channel.write(new Publish(subject, data));
    }

    public void subscribe() {

    }

    public void unsubscribe() {

    }

    /**
     * Processes an INFO message
     */
    public void processInfo(final Info info) {
        System.out.println(info.toString());

        if (info.getMaximumPayload() != null) {
            setMaxFrameSize(info.getMaximumPayload());
        }
    }
}
