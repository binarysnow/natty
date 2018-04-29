package com.binarysnow.natty.frame.server;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Iterator;

public class Message implements ServerCommand {

    private final static Splitter splitter = Splitter.on(' ').omitEmptyStrings().trimResults();
    private final static Gson gson = new GsonBuilder().create();

    @SerializedName("subject")
    private final String subject;

    @SerializedName("subscription_id")
    private final String subscriptionId;

    @SerializedName("reply_to")
    private final String replyTo;

    @SerializedName("payload_size")
    private final int payloadSize;

    private byte[] data;

    private Message(final String subject, final String subscriptionId, final String replyTo, final int payloadSize) {
        this.subject = subject;
        this.subscriptionId = subscriptionId;
        this.replyTo = replyTo;
        this.payloadSize = payloadSize;
    }

    public static Message parseString(final String messageString) {
        System.out.println("Message: " + messageString);
        final Iterable<String> messageParts = splitter.split(messageString);
        final Iterator<String> iterator = messageParts.iterator();

        final String subject = iterator.next();
        final String subscriptionId = iterator.next();

        final int byteCount;
        final String replyTo;

        final String next = iterator.next();

        if (!iterator.hasNext()) {
            replyTo = null;
            byteCount = Integer.parseInt(next);
        } else {
            replyTo = next;
            byteCount = Integer.parseInt(iterator.next());
        }

        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Malformed message string. " + messageString);
        }

        return new Message(subject, subscriptionId, replyTo, byteCount);
    }

    @Override
    public CommandCode getCommandCode() {
        return CommandCode.MESSAGE;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getPayloadSize() {
        return payloadSize;
    }

    public String toString() {
        return gson.toJson(this);
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getSubject() {
        return subject;
    }
}
