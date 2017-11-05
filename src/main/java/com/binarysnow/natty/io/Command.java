package com.binarysnow.natty.io;

public interface Command {
    CommandCode getCommandCode();

    public enum CommandCode {
        PING, PONG, ERROR, INFO, MESSAGE;
    }
}
