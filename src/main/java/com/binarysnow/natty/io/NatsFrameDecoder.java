package com.binarysnow.natty.io;

import com.binarysnow.natty.NatsConnection;
import com.binarysnow.natty.frame.server.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Decodes a NATS frame into an object
 */
public class NatsFrameDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NatsFrameDecoder.class);

    private static final int TWO_BYTES = 2;

    private final NatsConnection natsConnection;

    private final ServerCommandDecoder serverCommandDecoder;

    private final EndOfLineProcessor endOfLineProcessor;

    /**
     * A decoder for the NATS protocol
     * @param natsConnection The NatsConnection object for the connection
     */
    public NatsFrameDecoder(final NatsConnection natsConnection) {
        this.natsConnection = natsConnection;
        this.serverCommandDecoder = new ServerCommandDecoder(natsConnection);
        this.endOfLineProcessor = new EndOfLineProcessor();
    }

    @Override
    protected void decode(final ChannelHandlerContext context, final ByteBuf input, final List<Object> out) {
        final long maxFrameSize = natsConnection.getConnectionProperties().getMaxFrameSize();

        final int endOfLineIndex = findEndOfLine(input, maxFrameSize);

        if (endOfLineIndex > 0) {
            final int commandLength = endOfLineIndex - input.readerIndex();
            if (commandLength > maxFrameSize) {
                maxFrameSizeExceeded(commandLength, maxFrameSize);
            } else {
                final int readableBytes = input.readableBytes();
                if (readableBytes > maxFrameSize) {
                    maxFrameSizeExceeded(readableBytes, maxFrameSize);
                }
            }
            byte[] commandBytes = new byte[commandLength];
            input.readBytes(commandBytes, 0, commandLength);
            input.skipBytes(TWO_BYTES);

            //String commandString = input.toString(input.readerIndex(), endOfLineIndex, CharsetUtil.UTF_8);
            Command command = serverCommandDecoder.decodeCommand(context, input, commandBytes);

            out.add(command);
        }
    }

    /**
     * Returns the index in the buffer of the end of line found.
     * Returns -1 if no end of line was found in the buffer.
     */
    private int findEndOfLine(final ByteBuf input, final long maxFrameSize) {
        endOfLineProcessor.reset(maxFrameSize);
        int i = input.forEachByte(endOfLineProcessor);

        if (i > 0) {
            i -= 1;
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
