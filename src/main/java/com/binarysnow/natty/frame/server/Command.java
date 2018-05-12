package com.binarysnow.natty.frame.server;

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

    byte[] LINE_TERMINATOR = new byte[] { (byte) 0x0d, (byte) 0x0a};
    char SPACE = ' ';

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

            private final byte[] bytes;

            CommandCode(final String command) {
                bytes = command.getBytes(StandardCharsets.US_ASCII);
            }

            public byte[] getBytes() {
                return bytes;
            }
        }
}
