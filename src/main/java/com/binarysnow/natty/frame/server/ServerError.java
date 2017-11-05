package com.binarysnow.natty.frame.server;

import com.binarysnow.natty.io.Command;

public class ServerError implements Command {
    @Override
    public CommandCode getCommandCode() {
        return CommandCode.ERROR;
    }
}
