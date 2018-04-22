package com.binarysnow.natty.frame.server;

/**
 * A Ping request is sent to determine if the connection is still functioning
 */
public class Ping implements ServerCommand, ClientCommand {
    @Override
    public CommandCode getCommandCode() {
        return CommandCode.PING;
    }
}
