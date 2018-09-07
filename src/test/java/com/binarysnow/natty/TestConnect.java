package com.binarysnow.natty;

import com.binarysnow.natty.exception.CommunicationException;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

public class TestConnect {
    public static void main(String[] args) throws CommunicationException, InterruptedException {
        NatsClient client = new NatsClient("localhost", 4222);

        System.out.println("connect");

        client.connect();

        Thread.sleep(2000);

        client.publish("TEST_SUBJECT", "This is a test!".getBytes(StandardCharsets.UTF_8));

        Thread.sleep(2000);

        client.ping();

        Thread.sleep(2000);

        Subscription subscription1 = client.subscribe("TEST_SUBJECT", TestConnect::receiver1);

        Thread.sleep(2000);

        Subscription subscription2 = client.subscribe("TEST_SUBJECT", TestConnect::receiver2);

        Thread.sleep(2000);

        client.publish("TEST_SUBJECT", "This is a test!".getBytes(StandardCharsets.UTF_8));

        Thread.sleep(2000);

        client.unsubscribe(subscription1);

        System.out.println(System.currentTimeMillis());
        byte[] b = new byte[300];
        new Random().nextBytes(b);

        for (int i = 0; i < 100000; i++) {
            client.publish("NO_SUBJECT", b);
        }
        System.out.println(System.currentTimeMillis());

        System.out.println("fin");
    }

    private static void receiver1(final byte[] data) {
        System.out.println("RECEIVER 1");
    }

    private static void receiver2(final byte[] data) {
        System.out.println("RECEIVER 2");
    }
}
