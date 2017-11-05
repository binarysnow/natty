package com.binarysnow.natty;

import com.binarysnow.natty.io.Initialiser;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NatsClient {

    private final String address;
    private final int port;

    public NatsClient(final String address, final int port) {
        this.address = address;
        this.port = port;
    }

    public void connect() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new Initialiser(1024));
        bootstrap.remoteAddress(address, port);
        bootstrap.connect();
    }
}
