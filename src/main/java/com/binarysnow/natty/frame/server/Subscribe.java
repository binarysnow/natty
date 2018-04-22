package com.binarysnow.natty.frame.server;

import java.util.Optional;

/**
 * A Subscribe message is sent to initiate a new subscription to a subject
 */
public class Subscribe implements ClientCommand {

    private final String subject;

    private final String queueGroup;

    private final String subscriptionId;

    /**
     * Construct A Subscribe command with a subject and subscriptionId
     * @param subject The subject to subscribe to
     * @param subscriptionId The connection-unique subscription ID
     */
    public Subscribe(final String subject, final String subscriptionId) {
        this.subject = subject;
        this.queueGroup = null;
        this.subscriptionId = subscriptionId;
    }

    public Subscribe(final String subject, final String queueGroup, final String subscriptionId) {
        this.subject = subject;
        this.queueGroup = queueGroup;
        this.subscriptionId = subscriptionId;
    }

    @Override
    public CommandCode getCommandCode() {
        return CommandCode.SUBSCRIBE;
    }

    /**
     * Get the subject for the subscription
     * @return The subject for the subscription
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Get the queue group for the subscription
     * @return The optional queue group for the subscription
     */
    public Optional<String> getQueueGroup() {
        return Optional.of(queueGroup);
    }

    /**
     * Get the subscription identifier
     * @return The subscription identifier
     */
    public String getSubscriptionId() {
        return subscriptionId;
    }
}
