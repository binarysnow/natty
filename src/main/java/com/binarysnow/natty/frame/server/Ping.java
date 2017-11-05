package com.binarysnow.natty.frame.server;

import com.binarysnow.natty.io.Command;

public class Ping implements Command {
    @Override
    public CommandCode getCommandCode() {
        return CommandCode.PING;
    }
}
