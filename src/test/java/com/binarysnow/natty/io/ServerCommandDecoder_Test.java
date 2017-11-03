package com.binarysnow.natty.io;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class ServerCommandDecoder_Test {
    @Test
    public void commandDecoder_test() {
        ServerCommandDecoder serverCommandDecoder = new ServerCommandDecoder();
        serverCommandDecoder.decodeCommand(null, null, "PON".getBytes(StandardCharsets.US_ASCII));
    }
}
