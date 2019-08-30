package com.gwg.demo.startup.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse {

    private static final int BUFFER_SIZE = 1024;
    Request request = null;
    OutputStream outputStream = null;
    PrintWriter printWriter = null;

    public Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    //此方法用于服务静态资源
    public void sendStaticResource() throws IOException{
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;

        try {
            File file = new File(Constants.WEB_ROOT+request.getUri());
            fileInputStream = new FileInputStream(file);
            int ch = fileInputStream.read(bytes);//注意： 在每次读之前并没先清空该数组
            //响应头必须，否则浏览器无法正确显示
            String sucRespHeader = "HTTP/1.1 200 OK\r\n"+
                    "Content-Type: text/html;charset=UTF-8\r\n"+
                    "\r\n";
            outputStream.write(sucRespHeader.getBytes());

            while(ch != -1){
                outputStream.write(bytes, 0, ch);//读取多少字节，就写多少个。
                ch = fileInputStream.read(bytes);
            }
        } catch (IOException e) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n"+
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File not Found</h1>";
            outputStream.write(errorMessage.getBytes());

        }finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
        }

    }

    /***********空实现 实现ServletResponse*************************/
    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        /*
          autoflush is true , println() will flush.
          but print() will not
         */
        //PrintWriter类的构造函数的第2个参数是一个布尔值，表示是否启用autoflush。对第2个参数传入true表示对println方法的任何调用都会自动刷新。但是对
        //print方法不会刷新输出。
        printWriter = new PrintWriter(outputStream, true);
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
