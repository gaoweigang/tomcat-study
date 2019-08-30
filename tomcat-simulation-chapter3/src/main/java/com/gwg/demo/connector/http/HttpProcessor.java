package com.gwg.demo.connector.http;

import javax.servlet.ServletException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor {

    private HttpConnector httpConnector;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private HttpRequestLine requestLine = new HttpRequestLine();

    protected String method;
    protected String queryString;

    /**
     * 对于这个包的StringManager
     */
    protected StringManager sm = StringManager.getManager("com.gwg.demo.connector.http");

    public HttpProcessor(HttpConnector httpConnector){
        this.httpConnector = httpConnector;
    }

    public void process(Socket socket){
        SocketInputStream inputStream = null;
        OutputStream outputStream = null;


        inputStream = new SocketInputStream(socket.getInputStream(), 2048);
        outputStream = socket.getOutputStream();

        //1.创建一个HttpRequest对象


        httpRequest = new HttpRequest(inputStream);


        //2.创建一个HttpResponse对象

        httpResponse = new HttpResponse(outputStream);
        httpResponse.setRequest(request);

        httpResponse.setHeader("Server", "Pyrmont Servlet Container");
        //3.解析HTTP请求的第1行内容和请求头信息，填充HttpRequest对象
        parseRequest(inputStream, outputStream);
        parseHeaders(inputStream);

        //4.将HttpRequest对象和HttpResponse对象传递给ServletProcessor或StaticResourceProcessor的process()方法



        //5.对于这个应用没有shutdown
        socket.close();
    }


    /**
     * 假设请求的url为http://www.baidu.com/index.html?name=gaoweigang
     * @param inputStream
     * @param outputStream
     */
    public void parseRequest(SocketInputStream inputStream, OutputStream outputStream){

        //使用SocketInputStream对象中的信息填充HttpRequestLine实例。
        inputStream.readRequestLine(requestLine);
        //接着，从请求行RequestLine中获取请求方法，URI和请求协议的版本信息
        String method = new String(requestLine.method, 0, requestLine.methodEnd);
        String uri = null;
        String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);
        //但是URI后面可能会有一个查询字符串。若有，则查询字符串与URI是用一个问号分割的。
        // 因此，parseRequest()方法会首先调用HttpRequest类的setQueryString方法来获取查询字符串，并填充HttpRequest对象。
        int question = requestLine.indexOf("?");//查找字符'?'在uri中的起始地址
        if(question > 0){//如果请求uri(http://www.baidu.com/index.html?name=gaoweigang)带有请求参数
            //设置查询条件,比如name=gaoweigang
            httpRequest.setQueryString(new String(requestLine.uri, question+1, requestLine.uriEnd -(question+1)));
            //请求uri,不包含url后面的请求参数,比如 http://www.baidu.com/index.html
            uri = new String(requestLine.uri, 0, question);
        }else{//请求uri中不带请求参数
            httpRequest.setQueryString(null);
            uri = String.valueOf(requestLine.uri);
        }

        //检查uri是相对路径还是绝对路径
        if(!uri.startsWith("/")){//uri是绝对路径
            //如果不是以"/"开头，则是绝对路径
            int pos = uri.indexOf("://");
            //解析出protocol和host name
            if(pos != -1){
                pos = uri.indexOf('/', pos+3);
                if(pos == -1){//eg：url全路径为http://www.baidu.com
                    uri = null;
                }else{
                    uri = uri.substring(pos); //eg: index.html
                }
            }

        }

        //然后，查询字符串可能也会包含一个回话标识符，参数名为 jsessionid。因此，parseRequest()方法还要检查是否包含回话标识符。若在查询字符串中包含jsessionid,则parseRequest()方法要获取
        //jsessionid的值，并调用HttpRequest类setRequestedSessionid方法填充HttpRequest实例
        String match = ";jsessionid=";
        int semicolon = uri.indexOf(match);

        if(semicolon >= 0){

        }










    }

    public void parseHeaders(SocketInputStream inputStream){
        while(true){
            HttpHeader header = new HttpHeader();
            //读下一个header
            inputStream.readHeader(header);
            if(header.nameEnd == 0){
                if(header.valueEnd == 0){
                    return;
                }else{
                    throw new ServletException(sm.getString(""));
                }
            }

            String name = new String(header.name, 0 , header.nameEnd);
            String value = new String(header.value, 0, header.valueEnd);

            httpRequest.add

        }



    }
}
