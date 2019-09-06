package com.gwg.demo.connector.http;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpConnector implements  Runnable{
    boolean stopped;
    private String scheme = "http";
    private HttpProcessor httpProcessor;

    public String getScheme() {
        return scheme;
    }

    @Override
    public void run() {

        int port = 8080;
        //1.等待HTTP请求
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!stopped){
            Socket socket = null;
            try {
                //从ServerSocket中获取接下来进来的连接
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //2. 连接器 为每个请求创建一个HttpProcessor实例
            httpProcessor = new HttpProcessor(this);

            //3.调用HttpProcessor对象的process()方法
            try {
                httpProcessor.process(socket);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }
}
