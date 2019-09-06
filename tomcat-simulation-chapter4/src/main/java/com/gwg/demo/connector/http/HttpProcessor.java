package com.gwg.demo.connector.http;

import com.gwg.demo.ServletProcessor;
import com.gwg.demo.StaticResourceProcessor;
import com.gwg.demo.connector.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor {

    private HttpConnector httpConnector;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private HttpRequestLine requestLine = new HttpRequestLine();

    private ServletProcessor servletProcessor;
    private StaticResourceProcessor staticResourceProcessor;


    protected String method;
    protected String queryString;

    /**
     * 对于这个包的StringManager
     */
    protected StringManager sm = StringManager.getManager("com.gwg.demo.connector.http");

    public HttpProcessor(HttpConnector httpConnector){
        this.httpConnector = httpConnector;
    }

    public void process(Socket socket) throws ServletException, IOException {
        SocketInputStream inputStream = null;
        OutputStream outputStream = null;


        inputStream = new SocketInputStream(socket.getInputStream(), 2048);
        outputStream = socket.getOutputStream();

        //1.创建一个HttpRequest对象
        httpRequest = new HttpRequest(inputStream);


        //2.创建一个HttpResponse对象
        httpResponse = new HttpResponse(outputStream);
        httpResponse.setHttpRequest(httpRequest);

        httpResponse.setHeader("Server", "Pyrmont Servlet Container");
        //3.解析HTTP请求的第1行内容和请求头信息，填充HttpRequest对象
        parseRequest(inputStream, outputStream);//解析请求URL
        parseHeaders(inputStream);//解析请求头

        //4.将HttpRequest对象和HttpResponse对象传递给ServletProcessor或StaticResourceProcessor的process()方法
        if(httpRequest.getRequestURI().startsWith("/servlet")){
            servletProcessor = new ServletProcessor();
            servletProcessor.process(httpRequest, httpResponse);
        }else{
            staticResourceProcessor = new StaticResourceProcessor();
            staticResourceProcessor.process(httpRequest, httpResponse);
        }

        //5.对于这个应用没有shutdown
        socket.close();
    }


    /**
     * 假设请求的url为http://www.baidu.com/index.html?name=gaoweigang
     * @param inputStream
     * @param outputStream
     */
    public void parseRequest(SocketInputStream inputStream, OutputStream outputStream) throws ServletException, IOException {

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

        //检查uri是相对路径还是绝对路径,如果是绝对路径，则处理成相对路径
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
        if(semicolon >= 0){//判断uri中是否包含jsessionid,若在查询字符串中包含jsessionid，则获取jsessionid的值，并调用HttpRequest类的setRequestSessionId()方法填充HttpRequest实例
            String rest = uri.substring(semicolon+match.length());//截取match后面的字符串，即";jsessionid="之后的字符串
            int semicolon2 = rest.indexOf(';');
            if(semicolon2 >= 0){
                //rest.substring(0, semicolon2) 就是获取jsessionid的值
                httpRequest.setRequestedSessionId(rest.substring(0, semicolon2));
                rest = rest.substring(semicolon2);//截取jsessionid后面的子字符串
            }else{
                httpRequest.setRequestedSessionId(rest);
                rest = "";
            }
            //标识sessionid是否在URL中
            httpRequest.setRequestedSessionURL(true);
            uri = uri.substring(0, semicolon) + rest;//从uri中删除jsessionid之后的uri
        }else{
            httpRequest.setRequestedSessionId(null);
            //标识URL中不包含会话标识符sessionid
            httpRequest.setRequestedSessionURL(false);
        }

        //对非正常的URI进行修正。例如：出现"\"的地方会被替换成"/"。如果URI本身是正常的，或不正常的地方可以修正，则normalize()方法会返回相同的URI或修正过的URI
        String normalizedUri = normalize(uri);
        if(normalizedUri == null){
            throw new ServletException("Invalid URI: "+uri);
        }


        //最后，设置HttpRequest对象的属性
        httpRequest.setMethod(method);
        httpRequest.setProtocol(protocol);
        if(normalizedUri != null){
            httpRequest.setRequestURI(normalizedUri);
        }else{//这里不可能走到，因为如果normalizedUri为空的话 就会抛出异常
            httpRequest.setRequestURI(uri);
        }


    }
    /**
     * 对非正常的URI进行修正。例如：出现"\"的地方会被替换成"/"。如果URI本身是正常的，或不正常的地方可以修正，则normalize()方法会返回相同的URI或修正过的URI。
     */
    public String normalize(String uri){//在这里先简单返回
        return uri;
    }

    /**
     * 解析请求头：
     * 1.可以通过HttpHeader类的无参构造函数来创建一个HttpHeader实例
     * 2.创建了HttpHeader实例后，可以将其传给SocketInputStream类的readHeader()方法。若有请求头信息可以读取，readHeader()方法会相应地填充HttpHeader对象。若没有请求头
     * 信息可以去取，则HttpHeader实例的nameEnd和valueEnd字段都会是0
     * 3.要获取请求头的名字和值，可以使用如下方法：
     * String name = new String(header.name, 0, header.nameEnd);
     * String value = new String(header.value, 0, header.value);
     */
    public void parseHeaders(SocketInputStream inputStream) throws ServletException, IOException {
        /**
         * while循环会不断地从SocketInputStream中读取请求头信息，直到全部读完。
         * 在循环开始时，会先创建一个HttpHeader实例，然后将其传给SocketInputStream类的readHeader()方法
         */
        while(true){
            HttpHeader header = new HttpHeader();
            //读取并解析请求头中的下一行，并将请求头中的每行的解析结果封装在一个HttpHeader之中。
            //请求头中有多少行，就会封装成多少个HttpHeader
            inputStream.readHeader(header);
            //检查HttpHeader实例的nameEnd和valueEnd字段来判断是否已经从输入流中读取了所有的请求头信息
            if(header.nameEnd == 0){
                if(header.valueEnd == 0){
                    return;
                }else{
                    throw new ServletException(sm.getString(""));
                }
            }

            String name = new String(header.name, 0 , header.nameEnd);
            String value = new String(header.value, 0, header.valueEnd);

            //当获取了请求头的名称和值之后，就可以调用HttpRequest对象的addHeader()方法，将其添加到HttpRequest对象的HashMap请求头中
            httpRequest.addHeader(name, value);

            /**
             * 某些请求头包含一些属性设置信息
             */
            if(name.equals("cookies")){
                //在这里处理cookies
                Cookie[] cookies = RequestUtil.parseCookieHeader(value);//解析Cookie
                for(int i = 0; i< cookies.length; i++){
                    if(cookies[i].getName().equals("jsessionid")){
                        //检查请求的会话ID是否仍然有效.
                        if(!httpRequest.isRequestedSessionIdFromCookie()){//如果请求的会话ID无效
                            httpRequest.setRequestedSessionId(cookies[i].getValue());
                            //标识sessionID来自于Cookie而非URL中
                            httpRequest.setRequestedSessionCookie(true);
                            httpRequest.setRequestedSessionURL(false);
                        }
                    }
                }
            }else if(name.equals("content-length")){//注意：请求头中的name已经被转成小写了
                int n = -1;
                try {
                    n = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.contentLength"));

                }
                httpRequest.setContentLength(n);
            }else if(name.equals("content-type")){
                httpRequest.setContentType(name);
            }

        }

    }
}
