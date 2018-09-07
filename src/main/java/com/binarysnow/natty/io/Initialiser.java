package com.binarysnow.natty.io;

import com.binarysnow.natty.NatsConnection;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class Initialiser extends ChannelInitializer<SocketChannel> {

    private final NatsConnection natsConnection;

    public Initialiser(final NatsConnection natsConnection) {
        this.natsConnection = natsConnection;
    }

    @Override
    protected void initChannel(final SocketChannel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("clientMessageHandler", new ClientMessageHandler());
        pipeline.addLast("natsFrameDecoder", new NatsFrameDecoder(natsConnection));
        pipeline.addLast("serverMessageHandler", new ServerMessageHandler(natsConnection));
    }
}
