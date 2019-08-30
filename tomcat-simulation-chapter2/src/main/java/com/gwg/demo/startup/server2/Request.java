package com.gwg.demo.startup.server2;

import javax.servlet.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class Request implements ServletRequest{

    private String uri;
    private InputStream inputStream;

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public String getUri() {
        return uri;
    }

    public void parse(){
        StringBuffer request = new StringBuffer();
        byte[] bytes = new byte[2048];
        try {
            //Input.read(b): 从输入流读取一些字节数，并将它们存储到缓冲区b 。 实际读取的字节数作为整数返回。 该方法阻塞直到输入数据可用，检测到文件结束或抛出异常。
            int ch;
            //这里不能这么写？流始终无法结束，会一直阻塞
            /*while((ch = inputStream.read(bytes)) != -1){
                System.out.println("ch: "+ch);
                for(int j = 0; j < ch ; j++){
                    System.out.println("j:"+j);
                    request.append((char)bytes[j]);
                }
            }*/
            ch = inputStream.read(bytes);
            for(int j = 0; j < ch ; j++){
                request.append((char)bytes[j]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HTTP请求："+ request.toString());
        uri = parseUri(request.toString());

    }
    public String parseUri(String requestString){
        int index1, index2;
        index1 = requestString.indexOf(' ');//空格符
        if(index1 != -1){
            index2 = requestString.indexOf(' ', index1+1);
            if(index2 > index1){
                return requestString.substring(index1+1, index2);
            }
        }
        return null;
    }


    /*******实现ServletRequest******************/
    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
