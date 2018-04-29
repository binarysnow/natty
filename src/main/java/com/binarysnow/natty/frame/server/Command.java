package com.binarysnow.natty.frame.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public interface Command {

    int INF = 4804166;
    int _ER = 2966866;
    int O_ = 20256;
    int R_ = 21024;
    int G = 71;
    int MSG = 5067591;
    int OK = 2838347;
    int PIN = 5261646;
    int PON = 5263182;
    ByteBuf LINE_TERMINATOR = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(new byte[] { (byte) 0x0d, (byte) 0x0a}));
    ByteBuf SPACE = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(new byte[] { (byte) 0x20}));

    CommandCode getCommandCode();



    enum CommandCode {
            CONNECT("CONNECT"),
            SUBSCRIBE("SUB"),
            UNSUBSCRIBE("UNSUB"),
            PUBLISH("PUB"),
            PING("PING"),
            PONG("PONG"),
            OK("OK"),
            ERROR("-ERR"),
            INFO("INFO"),
            MESSAGE("MSG");

            private final ByteBuf byteBuffer;

            CommandCode(final String command) {
                final byte[] bytes = command.getBytes(StandardCharsets.US_ASCII);
                this.byteBuffer = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(bytes));
            }

            public ByteBuf getByteBuffer() {
                return byteBuffer;
            }
        }
}
