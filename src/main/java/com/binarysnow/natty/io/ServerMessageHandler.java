package com.binarysnow.natty.io;

import com.binarysnow.natty.frame.server.Info;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerMessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(final ChannelHandlerContext context, final Object command) throws Exception {
        final Command.CommandCode commandCode = ((Command) command).getCommandCode();
        switch (commandCode) {
            case PING:
                System.out.println("PING");
                break;
            case PONG:
                System.out.println("PONG");
                break;
            case ERROR:
                System.out.println("ERROR");
                break;
            case INFO:
                Info info = (Info) command;
                System.out.println(info.toString());
                break;
            case MESSAGE:
                break;
        }
    }
}
