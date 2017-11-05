package com.binarysnow.natty.frame.server;

import com.binarysnow.natty.io.Command;

public class Pong implements Command {
    @Override
    public CommandCode getCommandCode() {
        return CommandCode.PONG;
    }
}
