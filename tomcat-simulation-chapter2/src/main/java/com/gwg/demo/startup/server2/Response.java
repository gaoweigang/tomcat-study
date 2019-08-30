package com.gwg.demo.startup.server2;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse{

    private Request request;
    private OutputStream outputStream;
    private PrintWriter printWriter;

    public Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResouce() throws Exception{
        //1.定位静态资源并输出静态资源文件
        System.out.println("静态资源文件位置："+Constants.WEB_ROOT+request.getUri());
        File file = new File(Constants.WEB_ROOT+request.getUri());
        byte[] bytes = new byte[2048];
        try {
            FileInputStream input = new FileInputStream(file);
            String sucRespHeader = "HTTP/1.1 200 \n" +
                    "Content-Type: text/html\n" +
                    "\r\n";
            //响应头
            outputStream.write(sucRespHeader.getBytes());

            //响应体正文
            int ch = input.read(bytes);
            while(ch != -1){
                outputStream.write(bytes, 0, ch);
                ch = input.read(bytes);

            }
        } catch (IOException e) {
            String failRespMessage = "HTTP/1.1 404 File Not Found\n" +
                    "Content-Type: text/html\n" +
                    "\r\n" +
                    "<h1>File not Found, please check!</h1>";
            outputStream.write(failRespMessage.getBytes());
        }
    }

    /*******实现ServletResponse***************************/
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
        return new PrintWriter(outputStream, true);
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
