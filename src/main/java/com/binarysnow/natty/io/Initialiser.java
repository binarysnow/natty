package com.binarysnow.natty.io;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class Initialiser extends ChannelInitializer<SocketChannel> {

    private final int maxFrameSize;

    public Initialiser(final int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void initChannel(final SocketChannel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("natsFrameDecoder", new NatsFrameDecoder(maxFrameSize));
        pipeline.addLast("serverMessageHandler", new ServerMessageHandler());
    }
}
