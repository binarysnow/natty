package com.binarysnow.natty.frame.server;

import java.util.Optional;

/**
 * An Unsubscribe command is issued to unsubscribe from a subscription with a given identifier
 */
public class Unsubscribe implements ClientCommand {

    private final String subscriptionId;

    private final Integer maxMessages;

    /**
     * Construct an Unsubscribe command with the given subscription identifier
     * @param subscriptionId The identifier of the subscription to unsubscribe from
     */
    public Unsubscribe(final String subscriptionId) {
        this.subscriptionId = subscriptionId;
        this.maxMessages = null;
    }

    /**
     * Construct an Unsubscribe command with the given subscription identifier and the number of messages
     * after which the unsubscribe should occur
     * @param subscriptionId The identifier of the subscription to unsubscribe from
     * @param maxMessages The max number of message to receive on the subscription before unsubscribing
     */
    public Unsubscribe(final String subscriptionId, final Integer maxMessages) {
        this.subscriptionId = subscriptionId;
        this.maxMessages = maxMessages;
    }

    @Override
    public CommandCode getCommandCode() {
        return CommandCode.UNSUBSCRIBE;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public Optional<Integer> getMaxMessages() {
        return Optional.ofNullable(maxMessages);
    }
}
