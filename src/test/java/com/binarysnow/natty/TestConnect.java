package com.binarysnow.natty;

import java.nio.charset.StandardCharsets;

public class TestConnect {
    public static void main(String[] args) throws InterruptedException {
        NatsClient client = new NatsClient("localhost", 4222);

        System.out.println("connect");

        client.connect();

        client.publish("TEST_SUBJECT", "This is a test!".getBytes(StandardCharsets.UTF_8));

        System.out.println("fin");
    }
}
