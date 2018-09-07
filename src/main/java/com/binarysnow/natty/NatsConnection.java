package com.binarysnow.natty;

import com.binarysnow.natty.exception.CommunicationException;
import com.binarysnow.natty.frame.server.*;
import com.binarysnow.natty.io.Initialiser;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A NatsConnection manages a connection to a NATS server
 */
public class NatsConnection {
    private final static Logger LOGGER = LoggerFactory.getLogger(NatsConnection.class);

    private final AtomicInteger subscriptionIdCounter = new AtomicInteger();

    private final Map<String, MessageReceiver> subscriberMap = new ConcurrentHashMap<>();

    private Channel channel;

    private long maxFrameSize;

    private NatsConnection() {
    }

    /**
     * Publish a message
     */
    public void publish(final String subject, final byte[] data) {
        while (!channel.isWritable()) {

        }
        channel.write(new Publish(subject, data));
    }

    public void request(final String subject, final byte[] data) {
        //final String replyTo = ;
        //channel.write(new Publish(subject, replyTo, data));
    }

    public Subscription subscribe(final String subject, final MessageReceiver receiver) throws CommunicationException, InterruptedException {
        final String subscriptionId = Integer.toHexString(subscriptionIdCounter.getAndIncrement());
        final ChannelFuture future = channel.writeAndFlush(new Subscribe(subject, subscriptionId));

        future.await();

        if (future.isSuccess()) {
            LOGGER.debug("Subscribed to {} with id {}", subject, subscriptionId);
            subscriberMap.put(subscriptionId, receiver);
            return new Subscription(subject, subscriptionId, receiver);
        } else {
            LOGGER.error("Subscribe failed, subject:'{}', id:'{}'", subject, subscriptionId);
            throw new CommunicationException("Subscribe failed", future.cause());
        }
    }

    public void unsubscribe(final Subscription subscription) throws CommunicationException, InterruptedException {
        final ChannelFuture future = channel.writeAndFlush(new Unsubscribe(subscription.getSubscriptionId()));

        future.await();

        if (future.isSuccess()) {
            LOGGER.debug("Unsubscribed from: subject:'{}' id:'{}'", subscription.getSubject(), subscription.getSubscriptionId());
            subscriberMap.remove(subscription.getSubscriptionId());
        } else {
            LOGGER.error("Unsubscribe failed: subject:'{}' id:'{}'", subscription.getSubject(), subscription.getSubscriptionId());
            throw new CommunicationException("Unsubscribe failed", future.cause());
        }
    }
    /**
     * Processes an INFO message
     */
    public void processInfo(final Info info) {
        LOGGER.debug(info.toString());

        if (info.getMaximumPayload() != null) {
            setMaxFrameSize(info.getMaximumPayload());
        }
    }

    public void processMessage(final Message message) {
        LOGGER.debug(message.toString());
        final String subscriptionId = message.getSubscriptionId();
        final MessageReceiver messageReceiver = subscriberMap.get(subscriptionId);

        try {
            if (messageReceiver != null) {
                messageReceiver.receive(message.getData());
            }
        } catch (final Exception exception) {
            LOGGER.error("Exception in MessageReceiver.receiver()", exception);
        }
    }

    /**
     * Send a ping to the NATS server
     */
    public void ping() {
        channel.writeAndFlush(new Ping());
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
     * Connect to the NATS server
     */
    private void connect(final ConnectionProperties connectionProperties) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new Initialiser(this));
        bootstrap.remoteAddress(connectionProperties.getAddress(), connectionProperties.getPort());
        ChannelFuture future = bootstrap.connect().sync();
        channel = future.channel();
    }

    /**
     * Create a new connection
     */
    public static NatsConnection createConnection(final ConnectionProperties connectionProperties) throws InterruptedException {
        final NatsConnection natsConnection = new NatsConnection();
        natsConnection.connect(connectionProperties);
        return natsConnection;
    }

}
