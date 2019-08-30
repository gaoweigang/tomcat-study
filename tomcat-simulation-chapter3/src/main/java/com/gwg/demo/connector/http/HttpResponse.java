package com.gwg.demo.connector.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

public class HttpResponse implements HttpServletResponse {


    //默认的缓存大小
    private static final int BUFFER_SIZE = 1024;
    protected HttpRequest httpRequest;
    protected OutputStream outputStream;
    protected PrintWriter printWriter;
    protected byte[] buffer= new byte[BUFFER_SIZE];
    protected int bufferCount = 0;

    //这个响应已经提交了？
    protected boolean committed = false;

    //写入到这个响应的实际字节数
    protected int contentCount = 0;

    //这个响应相关的响应体长度
    protected int contentLength = -1;

    //这个响应相关的响应类型
    protected String contentType = null;

    //这个响应相关的字符编码
    protected String encoding = null;


    //和这个响应相关的Cookies集
    protected ArrayList cookies = new ArrayList();

    /**
     * HTTP头显式地通过addHeader()添加，
     * 但不包括那些要使用setContentLength()、setContentType()等添加的头。这个集合由header名作为键，元素是ArrayLists，其中包含已设置的关联值。
     */
    protected HashMap headers = new HashMap();


    protected final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MM yyyy HH:mm:ss zzz", Locale.US);


    protected String message = getStatusMessage(HttpServletResponse.SC_OK);


    protected int status = HttpServletResponse.SC_OK;

    public HttpResponse(OutputStream outputStream){
        this.outputStream = outputStream;
    }


    //调用此方法发送报头并响应输出
    public void finishResponse(){
        //sendHeaders
        //Flush and close the
        if(printWriter != null){
            printWriter.flush();
            printWriter.close();
        }
    }

    public int getContentLength() {
        return contentLength;
    }


    protected String getProcotol(){
        return httpRequest.getProtocol();
    }



    protected String getStatusMessage(int status){

        switch (status){
            case SC_OK:
                return "OK";
            case SC_BAD_GATEWAY:
                return "Bad Gateway";
            default:
                return "Http Response Status " + status;
        }


    }

    public OutputStream getStream(){
        return this.outputStream;
    }



    //--------------------------------------

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String s) {
        return false;
    }

    @Override
    public String encodeURL(String s) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return null;
    }

    @Override
    public String encodeUrl(String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return null;
    }

    @Override
    public void sendError(int i, String s) throws IOException {

    }

    @Override
    public void sendError(int i) throws IOException {

    }

    @Override
    public void sendRedirect(String s) throws IOException {

    }

    @Override
    public void setDateHeader(String s, long l) {

    }

    @Override
    public void addDateHeader(String s, long l) {

    }

    @Override
    public void setHeader(String s, String s1) {

    }

    @Override
    public void addHeader(String s, String s1) {

    }

    @Override
    public void setIntHeader(String s, int i) {

    }

    @Override
    public void addIntHeader(String s, int i) {

    }

    @Override
    public void setStatus(int i) {

    }

    @Override
    public void setStatus(int i, String s) {

    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
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
