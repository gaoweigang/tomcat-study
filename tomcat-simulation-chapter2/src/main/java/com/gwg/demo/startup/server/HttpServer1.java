package com.gwg.demo.startup.server;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer1 {

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer1 server1 = new HttpServer1();
        server1.await();
    }

    public void await(){

        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port,1 , InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);//终止正在运行的java虚拟机。该参数作为状态码；按照惯例，非零状态码表示异常终止。
        }

        //循环等待请求
        while(!shutdown){
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                socket = serverSocket.accept();//阻塞， 等待连接
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                //创建Request对象并解析出来URL
                Request request = new Request(inputStream);
                request.parse();

                Response response = new Response(outputStream);
                response.setRequest(request);

                System.out.println("请求uri:"+request.getUri());
                if(request.getUri().startsWith("/servlet")){
                    ServletProcessor1 processor = new ServletProcessor1();
                    processor.process(request, response);
                }else{
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
