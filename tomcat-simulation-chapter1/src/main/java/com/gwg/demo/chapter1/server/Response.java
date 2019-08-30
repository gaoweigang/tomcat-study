package com.gwg.demo.chapter1.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

public class Response {

    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output){
        this.output = output;
    }

    public void setRequest(Request request){
        this.request = request;
    }

    public void sendStaticResource() throws IOException{
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;

        try {
            System.out.println("静态资源路径："+HttpServer.WEB_ROOT+"    -- "+request.getUri());
            File file = new File(HttpServer.WEB_ROOT+request.getUri());
            if(file.exists()){
                //响应头必须，否则浏览器无法正常显示
                String successMessage = "HTTP/1.1 200\n"+
                        "Content-Type: text/html;charset=UTF-8\n"+
                        "\r\n";

                //响应实体正文
                output.write(successMessage.getBytes());
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0 , BUFFER_SIZE);
                if(ch != -1){
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            }else {
                //file not found
                //响应头
                String errorMessage = "HTTP/1.1 404 File Not Found\n" +
                        "Content-Type: text/html \r\n" +
                        "Content-Length: 23 \r\n" +
                        "\r\n" +
                        //响应实体正文
                        "<h1>File Not Found</h1>";
                output.write(errorMessage.getBytes());
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                fis.close();
            }
        }



    }

}
