package com.gwg.demo.connector.http;

import com.gwg.demo.connector.RequestStream;
import com.gwg.demo.connector.util.RequestUtil;
import com.sun.deploy.util.ParameterUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Principal;
import java.util.*;

public class HttpRequest implements HttpServletRequest {

    private String contentType;
    private int contentLength;
    private InetAddress inetAddress;

    private InputStream input;
    private String method;
    private String protocol;
    private String queryString;

    private String requestURI;
    private String serverName;
    private int serverPort;
    private Socket socket;
    private boolean requestedSessionCookie;
    private String requestedSessionId;
    private boolean requestedSessionURL;



    protected HashMap attributes = new HashMap();
    protected HashMap headers = new HashMap();
    protected ArrayList cookies = new ArrayList();
    protected ParameterMap parameters = null;

    //标识请求的参数已经解析过
    protected boolean parsed = false;

    /**
     * reader通过调用getReader()方法返回
     */
    protected BufferedReader reader = null;

    /**
     * ServletInputStream通过调用getInputStream返回
     */
    protected ServletInputStream stream = null;

    public HttpRequest(InputStream input){
        this.input = input;
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value){

        name = name.toLowerCase();
        synchronized (headers){
            ArrayList values = (ArrayList) headers.get(name);
            if(value == null){
                values = new ArrayList();
                headers.put(name, values);
            }
            values.add(value);
        }
    }


    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    public void setRequestedSessionURL(boolean requestedSessionURL) {
        this.requestedSessionURL = requestedSessionURL;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    //设置URL
    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setRequestedSessionCookie(boolean requestedSessionCookie) {
        this.requestedSessionCookie = requestedSessionCookie;
    }

    //
    protected void parseParameters(){
        if(this.parsed){//如果请求参数已经解析过则直接返回
            return;
        }
        ParameterMap results = parameters;
        if(results == null){
            results = new ParameterMap();
        }

        //打开parameterMap对象的锁，使其可进行写操作
        results.setLocked(false);

        //检查字符串encoding,若encoding为null,则使用默认编码
        String encoding = this.getCharacterEncoding();
        if(encoding == null){
            encoding = "ISO-8859-1";
        }

        //待解析字符串
        String queryString = getQueryString();

        //1,解析URL路径中的参数
        RequestUtil.parseParameters(results, queryString, encoding);

        //2,解析HTTP请求体中的参数
        //1.检查HTTP请求头是否包含请求参数，如果用户使用POST方法提交请求时，请求体会包含参数，则请求头“content-length”的值会大于0,
        //“content-type”的值为“application/x-www-form-urlencoded”。下面的代码用于解析请求体
        String contentType = this.getContentType();
        if(contentType == null){
            contentType = "";
        }
        /**
         * Content-Type: application/x-www-form-urlencoded;charset=utf-8
         * 说明：服务端通常是根据请求头（headers）中的 Content-Type 字段来获知请求中的消息主体是用何种方式编码，再对请求体进行解析。
         * 所以说到 POST 提交数据方案，包含了 Content-Type 和消息主体编码方式两部分。
         *
         *  Content-Type: application/json
         *  说明：application/json 这个 Content-Type 作为响应头大家肯定不陌生。实际上，现在越来越多的人把它作为请求头，用来告诉服务端消息主体是序列化后的 JSON 字符串
         */
        int semicolon = contentType.indexOf(';');
        //获取content-type，已得知请求体的内容格式(json或者key=value等)以及编码方式
        if(semicolon > 0){//
            contentType = contentType.substring(0, semicolon).trim();
        }else{
            contentType = contentType.trim();
        }
        if("POST".equals(getMethod()) && getContentLength() > 0
                && "application/x-www-form-urlencoded".equals(contentType)){
            int max = this.getContentLength();//获取请求体长度
            int len = 0;
            byte buf[] = new byte[getContentLength()];
            try {
                ServletInputStream is = getInputStream();//获取RequestStream
                while(len < max){
                    int next = is.read(buf, len, max-len);
                    if(next < 0){
                        break;
                    }
                    len += next;
                }
                is.close();
                if(len < max){
                    throw new RuntimeException("Content Length mismatch");
                }
                //解析请求参数
                RequestUtil.parseParameters(results, buf,encoding);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //最后，parseParameters()方法会锁定ParameterMap对象，将布尔变量parsed设置为true,将变量results赋值给变量parameters
        results.setLocked(true);
        this.parsed = true;
        this.parameters = results;

    }


    public InputStream getStream(){
        return input;
    }

    //*******************实现HttpServletRequest*******************************************************
    /////////////////////////////获取参数////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////////
    @Override
    public BufferedReader getReader() throws IOException {
        return reader;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(reader != null){
            throw new IllegalStateException("getInpuptStream has been called");
        }
        if(stream == null){
            stream = createInputStream();
        }
        return stream;
    }

    public ServletInputStream createInputStream() throws IOException {
        return new RequestStream(this);
    }

    @Override
    public String getRequestURI() {
        return this.requestURI;
    }

    ///////////////////////////////////////////////
    //*********************************************************************

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }


    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

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
