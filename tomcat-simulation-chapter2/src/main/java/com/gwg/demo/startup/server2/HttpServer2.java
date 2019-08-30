package com.gwg.demo.startup.server2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer2 {
    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer2 httpServer = new HttpServer2();
        httpServer.await();

    }
    public void await(){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port, 1 , InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!shutdown){
            try {
                Socket socket = serverSocket.accept();//监听端口
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Request request = new Request(inputStream);
            request.parse();

            Response response = new Response(outputStream);
            response.setRequest(request);

            System.out.println("请求uri:"+request.getUri());
            if(request.getUri().startsWith("/servlet")){

                ServletProcessor2 processor = new ServletProcessor2();
                processor.process(request, response);
            }else{//静态资源
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request, response);

            }
        }

    }

}
