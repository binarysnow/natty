package com.binarysnow.natty.io;

import com.binarysnow.natty.NatsClient;
import com.binarysnow.natty.frame.server.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Decodes a NATS frame into an object
 */
public class NatsFrameDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NatsFrameDecoder.class);

    private static final byte HEX_0A = 0x0a;
    private static final byte HEX_0D = 0x0d;
    private static final int TWO_BYTES = 2;

    private final NatsClient natsClient;

    private final ServerCommandDecoder serverCommandDecoder;

    /**
     * A decoder for the NATS protocol
     * @param natsClient The NatsClient object for the connection
     */
    public NatsFrameDecoder(final NatsClient natsClient) {
        this.natsClient = natsClient;
        this.serverCommandDecoder = new ServerCommandDecoder(natsClient);
    }

    @Override
    protected void decode(final ChannelHandlerContext context, final ByteBuf input, final List<Object> out) throws Exception {
        final long maxFrameSize = natsClient.getMaxFrameSize();

        final int endOfLineIndex = findEndOfLine(input);

        if (endOfLineIndex >= 0) {
            final int commandLength = endOfLineIndex - input.readerIndex();
            if (commandLength > maxFrameSize) {
                maxFrameSizeExceeded(commandLength, maxFrameSize);
            } else {
                final int readableBytes = input.readableBytes();
                if (readableBytes > maxFrameSize) {
                    maxFrameSizeExceeded(readableBytes, maxFrameSize);
                }
            }
            byte[] commandBytes = new byte[endOfLineIndex];
            input.readBytes(commandBytes, 0, endOfLineIndex);
            input.skipBytes(TWO_BYTES);

            //String commandString = input.toString(input.readerIndex(), endOfLineIndex, CharsetUtil.UTF_8);
            Command command = serverCommandDecoder.decodeCommand(context, input, commandBytes);

            out.add(command);
        }
    }

    /**
     * Returns the index in the buffer of the end of line found.
     * Returns -1 if no end of line was found in the buffer.
     * TODO add a check for frame size
     * TODO continue if 0D is not preceeded by 0A
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
    private void maxFrameSizeExceeded(final int readableBytes, final long maxFrameSize) {
        final String message = "Frame length is equal to or greater than " + readableBytes + " bytes and exceeds maximum allowed of " + maxFrameSize + " bytes.";
        LOGGER.error(message);
        throw new TooLongFrameException(message);
    }
}
