package com.binarysnow.natty;

public class TestConnect {
    public static void main(String[] args) throws InterruptedException {
        NatsClient client = new NatsClient("localhost", 4222);

        System.out.println("connect");

        client.connect();

        System.out.println("done");

        Thread.sleep(5000);

        System.out.println("fin");
    }
}
