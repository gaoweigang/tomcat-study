package com.gwg.demo.chapter1.server;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static final String WEB_ROOT = System.getProperty("user.dir")+ File.separator+"webroot";

    //shutdown command
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    //the shutdown command received
    private boolean shutdown = false;


    public static void main(String[] args) throws Exception{
        HttpServer server = new HttpServer();
        server.await();
    }

    public void await(){
        ServerSocket serverSocket = null;
        int port = 8080;

        try{
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        //Loop waiting for a request
        while(!shutdown){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try{
                //Socket本身不是协议，它只是提供了一个针对TCP或者UDP编程的接口
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                //create  Request object and parse
                Request request = new Request(input);
                request.parse();

                //create response object
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();

            } catch(Exception e){
                e.printStackTrace();
            }
        }


    }
}
