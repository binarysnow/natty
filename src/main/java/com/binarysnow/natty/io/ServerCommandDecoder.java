package com.binarysnow.natty.io;

import com.binarysnow.natty.NatsClient;
import com.binarysnow.natty.frame.server.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import java.nio.charset.StandardCharsets;


public class ServerCommandDecoder {

    private static final int TWO_BYTES = 2;

    private final NatsClient natsClient;

    /**
     * Create a ServerCommandDecoder
     * @param natsClient The NatsClient for the connection
     */
    public ServerCommandDecoder(final NatsClient natsClient) {
        this.natsClient = natsClient;
    }

    /**
     * Decode a command String and build the corresponding object
     * @param context The ChannelHandlerContext
     * @param input The ByteBuf containing the input bytes
     * @param commandString The command received in the input
     * @return The command object corresponding to the input command
     */
    public Command decodeCommand(final ChannelHandlerContext context, final ByteBuf input, final String commandString) {
        return null;
    }

    /**
     * Decode a command String and build the corresponding object
     * @param context The ChannelHandlerContext
     * @param input The ByteBuf containing the input bytes, used if further reading is required
     * @param commandBytes The command received in the input up to the line terminator
     * @return The command object corresponding to the input command
     */
    public Command decodeCommand(final ChannelHandlerContext context, final ByteBuf input, final byte[] commandBytes) {
        final Command result;

        int opCodeEnd;
        final int opCode = (commandBytes[0] << 16) | (commandBytes[1] << 8) | commandBytes[2];

        switch (opCode) {
            case Command.INF:
                opCodeEnd = (commandBytes[3] << 8) | commandBytes[4];
                if (opCodeEnd != Command.O_) {
                    throw new CorruptedFrameException("Malformed INFO command.");
                }
                result = Info.parseString(new String(commandBytes, 5, commandBytes.length-5, StandardCharsets.US_ASCII));
                break;
            case Command.MSG:
                Message message = Message.parseString(new String(commandBytes, 4, commandBytes.length-4, StandardCharsets.US_ASCII));
                final int payloadSize = message.getPayloadSize();
                final byte[] data = new byte[payloadSize];
                input.readBytes(data, 0, payloadSize);
                message.setData(data);
                input.skipBytes(TWO_BYTES);
                result = message;
                break;
            case Command.PIN:
                opCodeEnd = commandBytes[3];
                if (opCodeEnd != Command.G || commandBytes.length > 4) {
                    throw new CorruptedFrameException("Malformed PING command.");
                }
                result = new Ping();
                break;
            case Command.PON:
                opCodeEnd = commandBytes[3];
                if (opCodeEnd != Command.G || commandBytes.length > 4) {
                    throw new CorruptedFrameException("Malformed PONG command.");
                }
                result = new Pong();
                break;
            case Command._ER:
                opCodeEnd = (commandBytes[3] << 8) | commandBytes[4];
                if (opCodeEnd != Command.R_) {
                    throw new CorruptedFrameException("Malformed -ERR command");
                }
                result = new ServerError();
                break;
            case Command.OK:
                result = new Ok();
                break;
            default:
                System.out.println(new String(commandBytes, StandardCharsets.US_ASCII));
                throw new CorruptedFrameException("Unknown command.");
        }

        return result;
    }
}
