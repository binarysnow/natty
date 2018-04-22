package com.binarysnow.natty.frame.server;

import java.util.Optional;

public class Unsubscribe implements Command {

    private final String subscriptionId;

    private final Integer maxMessages;

    public Unsubscribe(final String subscriptionId) {
        this.subscriptionId = subscriptionId;
        this.maxMessages = null;
    }

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
        return Optional.of(maxMessages);
    }
}
