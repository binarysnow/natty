package com.binarysnow.natty.io;

import com.binarysnow.natty.frame.server.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Handler for messages being send from the client to the server
 */
public class ClientMessageHandler extends ChannelOutboundHandlerAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

    @Override
    public void write(final ChannelHandlerContext context, final Object command, final ChannelPromise promise) {
        final Command.CommandCode commandCode = ((ClientCommand) command).getCommandCode();
        switch (commandCode) {
            case CONNECT:
                LOGGER.debug("->CONNECT");
                break;
            case PUBLISH:
                LOGGER.debug("->PUBLISH");
                sendPublish(context, (Publish) command);
                break;
            case SUBSCRIBE:
                LOGGER.debug("->SUBSCRIBE");
                sendSubscribe(context, (Subscribe) command, promise);
                break;
            case UNSUBSCRIBE:
                LOGGER.debug("->UNSUBSCRIBE");
                sendUnsubscribe(context, (Unsubscribe) command, promise);
                break;
            case PING:
                LOGGER.debug("->PING");
                sendSimple(context, commandCode.getBytes());
                break;
            case PONG:
                LOGGER.debug("->PONG");
                sendSimple(context, commandCode.getBytes());
                break;
        }
    }

    /**
     * Send a Publish message
     * @param context The ChannelHandlerContext
     * @param publish The Publish object containing the message details
     */
    private void sendPublish(final ChannelHandlerContext context, final Publish publish) {
        final ByteBuf buffer = context.alloc().buffer();

        buffer.writeBytes(publish.getCommandCode().getBytes());
        buffer.writeByte(Command.SPACE);

        buffer.writeCharSequence(publish.getSubject(), StandardCharsets.US_ASCII);
        buffer.writeByte(Command.SPACE);

        if (publish.getReplyTo().isPresent()) {
            buffer.writeCharSequence(publish.getReplyTo().get(), StandardCharsets.US_ASCII);
            buffer.writeByte(Command.SPACE);
        }

        buffer.writeCharSequence(String.valueOf(publish.getData().length), StandardCharsets.US_ASCII);
        buffer.writeBytes(Command.LINE_TERMINATOR);
        buffer.writeBytes(publish.getData());
        buffer.writeBytes(Command.LINE_TERMINATOR);

        context.writeAndFlush(buffer, context.voidPromise());
    }

    /**
     * Send a Subscribe message
     * @param context The ChannelHandlerContext
     * @param subscribe The Subscribe object containing the message details
     * @param promise The ChannelPromise to notify when the operation is complete
     */
    private void sendSubscribe(final ChannelHandlerContext context, final Subscribe subscribe, final ChannelPromise promise) {
        final ByteBuf buffer = context.alloc().buffer();

        buffer.writeBytes(subscribe.getCommandCode().getBytes());
        buffer.writeByte(Command.SPACE);

        buffer.writeCharSequence(subscribe.getSubject(), StandardCharsets.US_ASCII);
        buffer.writeByte(Command.SPACE);

        if (subscribe.getQueueGroup().isPresent()) {
            buffer.writeCharSequence(subscribe.getQueueGroup().get(), StandardCharsets.US_ASCII);
            buffer.writeByte(Command.SPACE);
        }

        buffer.writeCharSequence(subscribe.getSubscriptionId(), StandardCharsets.US_ASCII);
        buffer.writeBytes(Command.LINE_TERMINATOR);

        context.writeAndFlush(buffer, promise);
    }

    /**
     * Send an Unsubscribe message
     * @param context The ChannelHandlerContext
     * @param unsubscribe The Unsubscribe object containing the message details
     * @param promise The ChannelPromise to notify when the operation is complete
     */
    private void sendUnsubscribe(final ChannelHandlerContext context, final Unsubscribe unsubscribe, final ChannelPromise promise) {
        final ByteBuf buffer = context.alloc().buffer();

        buffer.writeBytes(unsubscribe.getCommandCode().getBytes());
        buffer.writeByte(Command.SPACE);
        buffer.writeCharSequence(unsubscribe.getSubscriptionId(), StandardCharsets.US_ASCII);

        if (unsubscribe.getMaxMessages().isPresent()) {
            buffer.writeCharSequence(unsubscribe.getMaxMessages().get().toString(), StandardCharsets.US_ASCII);
            buffer.writeByte(Command.SPACE);
        }

        buffer.writeBytes(Command.LINE_TERMINATOR);

        context.writeAndFlush(buffer, promise);
    }

    /**
     * Send a command which requires no parameters.
     * @param context The ChannelHandlerContext
     * @param bytes The bytes to send
     */
    private void sendSimple(final ChannelHandlerContext context, final byte[] bytes) {
        final ByteBuf buffer = context.alloc().buffer(bytes.length + 2);
        buffer.writeBytes(bytes);
        buffer.writeBytes(Command.LINE_TERMINATOR);

        context.writeAndFlush(buffer, context.voidPromise());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Exception", cause);
    }
}
