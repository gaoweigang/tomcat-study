package com.gwg.demo.chapter2.server;

public class HttpServer1 {

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer1 server1 = new HttpServer1();
        server1.await();
    }

    public void await(){

        Integer.valueOf("1");

    }
}
