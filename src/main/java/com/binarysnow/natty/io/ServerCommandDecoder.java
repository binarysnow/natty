package com.binarysnow.natty.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ServerCommandDecoder {

    private static final int INF = 4804166;
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
     * @param input The ByteBuf containing the input bytes
     * @param commandBytes The command received in the input
     * @return The command object corresponding to the input command
     */
    public Command decodeCommand(final ChannelHandlerContext context, final ByteBuf input, final byte[] commandBytes) {
        final int opCode = (commandBytes[0] << 16) | (commandBytes[1] << 8) | commandBytes[2];

        switch (opCode) {
            case INF:
                break;
            case MSG:
                break;
            case PIN:
                break;
            case PON:
                break;
        }

        return null;
    }
}
