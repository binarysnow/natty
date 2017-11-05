package com.binarysnow.natty.io;

import com.binarysnow.natty.frame.server.Info;
import com.binarysnow.natty.frame.server.Ping;
import com.binarysnow.natty.frame.server.Pong;
import com.binarysnow.natty.frame.server.ServerError;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.CharsetUtil;

import java.util.Arrays;

public class ServerCommandDecoder {

    private static final int INF = 4804166;
    private static final int _ER = 2966866;
    private static final int O_ = 20256;
    private static final int R_ = 21024;
    private static final int G = 71;
    private static final int MSG = 5067591;
    private static final int PIN = 5261646;
    private static final int PON = 5263182;

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
            case INF:
                opCodeEnd = (commandBytes[3] << 8) | commandBytes[4];
                if (opCodeEnd != O_) {
                    throw new CorruptedFrameException("Malformed INFO command.");
                }
                result = Info.parseString(new String(commandBytes, 5, commandBytes.length-5, CharsetUtil.US_ASCII));
                break;
            case MSG:
                result = null;
                break;
            case PIN:
                opCodeEnd = commandBytes[3];
                if (opCodeEnd != G || commandBytes.length > 4) {
                    throw new CorruptedFrameException("Malformed PING command.");
                }
                result = new Ping();
                break;
            case PON:
                opCodeEnd = commandBytes[3];
                if (opCodeEnd != G || commandBytes.length > 4) {
                    throw new CorruptedFrameException("Malformed PONG command.");
                }
                result = new Pong();
                break;
            case _ER:
                opCodeEnd = (commandBytes[3] << 8) | commandBytes[4];
                if (opCodeEnd != R_) {
                    throw new CorruptedFrameException("Malformed -ERR command");
                }
                result = new ServerError();
                break;
            default:
                System.out.println(new String(commandBytes, CharsetUtil.US_ASCII));
                throw new CorruptedFrameException("Unknown command.");
        }

        return result;
    }
}
