package com.binarysnow.natty.frame.server;

public class Ok implements ServerCommand, ClientCommand {
    @Override
    public CommandCode getCommandCode() {
        return CommandCode.OK;
    }
}
