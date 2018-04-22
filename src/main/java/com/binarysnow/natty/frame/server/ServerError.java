package com.binarysnow.natty.frame.server;

public class ServerError implements Command {
    @Override
    public CommandCode getCommandCode() {
        return CommandCode.ERROR;
    }
}
