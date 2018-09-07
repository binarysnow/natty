package com.binarysnow.natty.io;

import com.binarysnow.natty.NatsConnection;
import com.binarysnow.natty.frame.server.Command;
import com.binarysnow.natty.frame.server.Info;
import com.binarysnow.natty.frame.server.Message;
import com.binarysnow.natty.frame.server.Pong;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMessageHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class);

    private final NatsConnection natsConnection;

    /**
     * Create a ServerMessageHandler
     * @param natsConnection The NatsConnection for the connection
     */
    public ServerMessageHandler(final NatsConnection natsConnection) {
        this.natsConnection = natsConnection;
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, final Object command) throws Exception {
        final Command.CommandCode commandCode = ((Command) command).getCommandCode();
        switch (commandCode) {
            case PING:
                LOGGER.debug("<-PING");
                context.write(new Pong());
                break;
            case PONG:
                LOGGER.debug("<-PONG");
                break;
            case ERROR:
                LOGGER.debug("<-ERROR");
                break;
            case INFO:
                final Info info = (Info) command;
                natsConnection.processInfo(info);
                break;
            case MESSAGE:
                LOGGER.debug("<-MESSAGE");
                final Message message = (Message) command;
                natsConnection.processMessage(message);
                break;
            case OK:
                LOGGER.debug("<-OK");
                break;
            default:
                LOGGER.error("UNKNOWN SERVER COMMAND");
                break;
        }
    }
}
