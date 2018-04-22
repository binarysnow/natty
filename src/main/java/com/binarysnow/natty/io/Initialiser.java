package com.binarysnow.natty.io;

import com.binarysnow.natty.NatsClient;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class Initialiser extends ChannelInitializer<SocketChannel> {

    private final NatsClient natsClient;

    public Initialiser(final NatsClient natsClient) {
        this.natsClient = natsClient;
    }

    @Override
    protected void initChannel(final SocketChannel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("clientMessageHandler", new ClientMessageHandler());
        pipeline.addLast("natsFrameDecoder", new NatsFrameDecoder(natsClient));
        pipeline.addLast("serverMessageHandler", new ServerMessageHandler(natsClient));
    }
}
