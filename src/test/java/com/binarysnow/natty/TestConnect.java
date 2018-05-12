package com.binarysnow.natty;

import com.binarysnow.natty.exception.CommunicationException;

import java.nio.charset.StandardCharsets;

public class TestConnect {
    public static void main(String[] args) throws CommunicationException, InterruptedException {
        NatsClient client = new NatsClient("localhost", 4222);

        System.out.println("connect");

        client.connect();

        Thread.sleep(500);

        client.publish("TEST_SUBJECT", "This is a test!".getBytes(StandardCharsets.UTF_8));

        Thread.sleep(500);

        client.ping();

        Thread.sleep(500);

        client.subscribe("TEST_SUBJECT", TestConnect::receiver1);

        Thread.sleep(500);

        client.subscribe("TEST_SUBJECT", TestConnect::receiver2);

        Thread.sleep(500);

        client.publish("TEST_SUBJECT", "This is a test!".getBytes(StandardCharsets.UTF_8));

        Thread.sleep(500);

        System.out.println("fin");
    }

    private static void receiver1(final byte[] data) {
        System.out.println("RECEIVER 1");
    }

    private static void receiver2(final byte[] data) {
        System.out.println("RECEIVER 2");
    }
}
