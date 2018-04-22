package com.binarysnow.natty.frame.server;

import java.util.Arrays;
import java.util.Optional;

public class Publish implements ClientCommand {

    private final String subject;

    private final String replyTo;

    private final byte[] data;

    public Publish(final String subject, final byte[] data) {
        this.subject = subject;
        this.replyTo = null;
        this.data = Arrays.copyOf(data, data.length);
    }

    public Publish(final String subject, final String replyTo, final byte[] data) {
        this.subject = subject;
        this.replyTo = replyTo;
        this.data = Arrays.copyOf(data, data.length);
    }

    @Override
    public CommandCode getCommandCode() {
        return CommandCode.PUBLISH;
    }

    public String getSubject() {
        return subject;
    }

    public Optional<String> getReplyTo() {
        return Optional.of(replyTo);
    }

    public byte[] getData() {
        return data;
    }
}
