package com.binarysnow.natty.io;

import com.binarysnow.natty.NatsClient;
import com.binarysnow.natty.frame.server.Command;
import com.binarysnow.natty.frame.server.Info;
import com.binarysnow.natty.frame.server.Pong;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerMessageHandler extends ChannelInboundHandlerAdapter {

    private final NatsClient natsClient;

    /**
     * Create a ServerMessageHandler
     * @param natsClient The NatsClient for the connection
     */
    public ServerMessageHandler(final NatsClient natsClient) {
        this.natsClient = natsClient;
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, final Object command) throws Exception {
        final Command.CommandCode commandCode = ((Command) command).getCommandCode();
        switch (commandCode) {
            case PING:
                System.out.println("<-PING");
                context.write(new Pong());
                break;
            case PONG:
                System.out.println("<-PONG");
                break;
            case ERROR:
                System.out.println("<-ERROR");
                break;
            case INFO:
                final Info info = (Info) command;
                natsClient.processInfo(info);
                break;
            case MESSAGE:
                System.out.println("<-MESSAGE");
                break;
        }
    }
}
