package com.binarysnow.natty;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionCleanupThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionCleanupThread.class);

    private final Channel channel;
    private final EventLoopGroup group;

    public ConnectionCleanupThread(final Channel channel, final EventLoopGroup group) {
        this.channel = channel;
        this.group = group;
    }

    @Override
    public void run() {
        try {
            if (channel != null) {
                channel.closeFuture().sync();
            }
        } catch (final InterruptedException interrupted) {
            LOGGER.warn("ConnectionCleanupThread interrupted.");
        } finally {
            group.shutdownGracefully();
        }
    }
}
