package com.binarysnow.natty.frame.server;

/**
 * A Pong is sent in response to a Ping to acknowledge that the connection is still functioning
 */
public class Pong implements ServerCommand, ClientCommand {
    @Override
    public CommandCode getCommandCode() {
        return CommandCode.PONG;
    }
}
