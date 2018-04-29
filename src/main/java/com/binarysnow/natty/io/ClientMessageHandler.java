package com.binarysnow.natty.io;

import com.binarysnow.natty.frame.server.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
                sendSubscribe(context, (Subscribe) command);
                break;
            case UNSUBSCRIBE:
                LOGGER.debug("->UNSUBSCRIBE");
                sendUnsubscribe(context, (Unsubscribe) command);
                break;
            case PING:
                LOGGER.debug("->PING");
                sendSimple(context, commandCode.getByteBuffer());
                break;
            case PONG:
                LOGGER.debug("->PONG");
                sendSimple(context, commandCode.getByteBuffer());
                break;
        }
    }

    /**
     * Send a Publish message
     * @param context The ChannelHandlerContext
     * @param publish The Publish object containing the message details
     */
    private void sendPublish(final ChannelHandlerContext context, final Publish publish) {
        context.write(publish.getCommandCode().getByteBuffer());
        context.write(Command.SPACE);

        context.write(Unpooled.copiedBuffer(publish.getSubject(), StandardCharsets.US_ASCII));
        context.write(Command.SPACE);

        if (publish.getReplyTo().isPresent()) {
            ByteBuf replyToBuf = Unpooled.copiedBuffer(publish.getReplyTo().get(), StandardCharsets.US_ASCII);
            context.write(replyToBuf);
            context.write(Command.SPACE);
        }

        ByteBuf payloadSizeBuf = Unpooled.copiedBuffer(String.valueOf(publish.getData().length), StandardCharsets.US_ASCII);
        context.write(payloadSizeBuf);
        context.write(Command.LINE_TERMINATOR);
        context.write(Unpooled.copiedBuffer(publish.getData()));
        context.write(Command.LINE_TERMINATOR);
        context.flush();
    }

    /**
     * Send a Subscribe message
     * @param context The ChannelHandlerContext
     * @param subscribe The Subscribe object containing the message details
     */
    private void sendSubscribe(final ChannelHandlerContext context, final Subscribe subscribe) {
        context.write(subscribe.getCommandCode().getByteBuffer());
        context.write(Command.SPACE);

        context.write(Unpooled.copiedBuffer(subscribe.getSubject(), StandardCharsets.US_ASCII));
        context.write(Command.SPACE);

        if (subscribe.getQueueGroup().isPresent()) {
            ByteBuf queueGroupBuf = Unpooled.copiedBuffer(subscribe.getQueueGroup().get(), StandardCharsets.US_ASCII);
            context.write(queueGroupBuf);
            context.write(Command.SPACE);
        }

        context.write(Unpooled.copiedBuffer(subscribe.getSubscriptionId(), StandardCharsets.US_ASCII));
        context.write(Command.LINE_TERMINATOR);
        context.flush();
    }

    /**
     * Send an Unsubscribe message
     * @param context The ChannelHandlerContext
     * @param unsubscribe The Unsubscribe object containing the message details
     */
    private void sendUnsubscribe(final ChannelHandlerContext context, final Unsubscribe unsubscribe) {
        context.write(unsubscribe.getCommandCode().getByteBuffer());
        context.write(Command.SPACE);
        context.write(unsubscribe.getSubscriptionId());

        if (unsubscribe.getMaxMessages().isPresent()) {
            ByteBuf queueGroupBuf = Unpooled.copiedBuffer(unsubscribe.getMaxMessages().get().toString(), StandardCharsets.US_ASCII);
            context.write(queueGroupBuf);
            context.write(Command.SPACE);
        }

        context.write(Command.LINE_TERMINATOR);
        context.flush();
    }

    /**
     * Send a command which requires no parameters.
     * @param context The ChannelHandlerContext
     * @param byteBuffer The ByteBuf to send
     */
    private void sendSimple(final ChannelHandlerContext context, final ByteBuf byteBuffer) {
        context.write(byteBuffer);
        context.write(Command.LINE_TERMINATOR);
        context.flush();
    }
}
