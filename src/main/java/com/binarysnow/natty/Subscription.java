package com.binarysnow.natty;

public class Subscription {

    private final String subject;
    private final String subscriptionId;
    private final MessageReceiver receiver;

    public Subscription(final String subject, final String subscriptionId, final MessageReceiver receiver) {
        this.subject = subject;
        this.subscriptionId = subscriptionId;
        this.receiver = receiver;
    }

    protected String getSubscriptionId() {
        return subscriptionId;
    }

    public String getSubject() {
        return subject;
    }

    public MessageReceiver getReceiver() {
        return receiver;
    }
}
