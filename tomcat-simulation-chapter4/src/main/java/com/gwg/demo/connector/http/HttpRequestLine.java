package com.gwg.demo.connector.http;

/**
 * HTTP请求行
 */
public class HttpRequestLine {

    //---------------------------------常量
    public static final int INITIAL_METHOD_SIZE = 8;
    public static final int INITIAL_URI_SIZE = 64;
    public static final int INITIAL_PROTOCOL_SIZE = 8;
    public static final int MAX_METHOD_SIZE = 1024;
    public static final int MAX_URI_SIZE = 32768;
    public static final int MAX_PROTOCOL_SIZE = 1024;

    //---------------------------------成员变量
    public char[] method;
    public int methodEnd;
    public char[] uri;
    public int uriEnd;
    public char[] protocol;
    public int protocolEnd;

    public HttpRequestLine(char[] method, int methodEnd,
                           char[] uri, int uriEnd,
                           char[] protocol, int protocolEnd){
        this.method = method;
        this.methodEnd = methodEnd;
        this.uri = uri;
        this.uriEnd = uriEnd;
        this.protocol = protocol;
        this.protocolEnd = protocolEnd;

    }

    public HttpRequestLine(){
        this(new char[INITIAL_METHOD_SIZE], 0,
                new char[INITIAL_URI_SIZE], 0,
                new char[INITIAL_PROTOCOL_SIZE], 0);
    }

    //释放所有对象引用，并初始化实例变量，以准备重用此对象。
    public void recycle(){
        this.methodEnd = 0;
        this.uriEnd = 0;
        this.protocolEnd = 0;

    }

    /**
     * 从uri中查询指定的字符串，返回该字符串的起始位置
     * buf :被查找的字符串
     * buf :被查找的字符串长度
     */
    public int indexOf(char[] buf, int end){
        char firstChar = buf[0];
        int pos = 0;
        while(pos < uriEnd){
            pos = indexOf(firstChar, pos);//递归
            if(pos == -1){
                return -1;
            }
            if((uriEnd - pos) < end){//如果原字符串中剩余字符串小于被查找字符串长度，则直接返回
                return -1;
            }
            for(int i = 0; i< end; i++){
                if(uri[pos+i] != buf[i]){
                    break;
                }
                if(i == (end-1)){
                    return pos;
                }
            }
            return -1;
        }
        return -1;
    }

    public int indexOf(char c, int start){
        for(int i = start; i< uriEnd; i++){
            if(uri[i] == c){
               return start;
            }
        }
        return -1;
    }

    //测试请求头的值是否包含指定字符串,并返回指定字符串的位置
    public int indexOf(String str){
        return indexOf(str.toCharArray(), str.length());

    }
}
