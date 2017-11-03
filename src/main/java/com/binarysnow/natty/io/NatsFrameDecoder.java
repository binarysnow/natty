package com.binarysnow.natty.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Decodes
 */
public class NatsFrameDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NatsFrameDecoder.class);

    private static final byte HEX_0A = 0x0a;
    private static final byte HEX_0D = 0x0d;
    private static final int TWO_BYTES = 2;

    // TODO This can change after connection via server info
    private final int maxFrameSize;

    private final ServerCommandDecoder serverCommandDecoder;

    /**
     * A decoder for the NATS protocol
     * @param maxFrameSize The maximum frame size allowed by this decoder
     */
    public NatsFrameDecoder(final int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        this.serverCommandDecoder = new ServerCommandDecoder();
    }

    @Override
    protected void decode(final ChannelHandlerContext context, final ByteBuf input, final List<Object> out) throws Exception {
        final int endOfLineIndex = findEndOfLine(input);

        if (endOfLineIndex >= 0) {
            final int commandLength = endOfLineIndex - input.readerIndex();
            if (commandLength > maxFrameSize) {
                maxFrameSizeExceeded(commandLength);
            } else {
                final int readableBytes = input.readableBytes();
                if (readableBytes > maxFrameSize) {
                    maxFrameSizeExceeded(readableBytes);
                }
            }
            byte[] commandBytes = new byte[endOfLineIndex];
            input.readBytes(commandBytes, 0, endOfLineIndex);
            input.skipBytes(TWO_BYTES);
            //String commandString = input.toString(input.readerIndex(), endOfLineIndex, CharsetUtil.UTF_8);
            //input.skipBytes(endOfLineIndex + TWO_BYTES);
            //Command command = serverCommandDecoder.decodeCommand(context, input, commandString);
            Command command = serverCommandDecoder.decodeCommand(context, input, commandBytes);
        }
    }

    /**
     * Returns the index in the buffer of the end of line found.
     * Returns -1 if no end of line was found in the buffer.
     */
    private static int findEndOfLine(final ByteBuf input) {
        int i = input.forEachByte(new ByteProcessor.IndexOfProcessor(HEX_0D));
        if (i > 0 && input.getByte(i - 1) == HEX_0A) {
            i--;
        }
        return i;
    }

    /**
     * Handle the situation where the maximum frame size has been exceeded
     * @param readableBytes The number of readable bytes which caused this condition
     */
    private void maxFrameSizeExceeded(final int readableBytes) {
        final String message = "Frame length is equal to or greater than " + readableBytes + " bytes and exceeds maximum allowed of " + maxFrameSize + " bytes.";
        LOGGER.error(message);
        throw new TooLongFrameException(message);
    }
}
