package com.gwg.demo.connector.util;

import com.gwg.demo.connector.http.ParameterMap;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 解析cookies
 * 1.Cookie是由浏览器作为HTTP请求头的一部分发送的。这样的请求头的名称是"cookie",其对应值是一些名/值对。下面是一个Cookie请求头的例子，其中包含两个Cookie:
 * Cookie:username=root; JSESSIONID=28D77A7B33EDC8E8FDF010FE3AD4E128;
 *
 * 2.在tomcat中对Cookie的解析是通过org.apache.catalina.util.RequestUtil类的parseCookieHeader()方法完成的。该方法接受Cookie请求头，返回javax.servlet.http.Cookie类
 * 型的一个数组。数组中元素的个数与Cookie请求头中名/值对的数目相同。
 */
public class RequestUtil {


    /**
     *
     * @param header Cookie请求头的值
     * @return
     */
    public static Cookie[] parseCookieHeader(String header){
        if(header == null || header.length() < 1){
            return new Cookie[0];//返回空Cookie数组
        }
        ArrayList cookies = new ArrayList();
        while(header.length() > 0){
            int semicolon = header.indexOf(';');

            String token = header.substring(0, semicolon);
            if(semicolon < header.length()){
                header = header.substring(semicolon+1);
            }else{
                header = "";
            }
            int equals = token.indexOf(';');
            if(equals > 0){
                String name = token.substring(0,equals).trim();
                String value = token.substring(equals+1).trim();
                cookies.add(new Cookie(name, value));
            }
        }
        return (Cookie[]) cookies.toArray();
    }

    /**
     * 解析请求参数
     * @param results
     * @param buf
     * @param encoding
     * @throws UnsupportedEncodingException
     */
    public static void parseParameters(ParameterMap results, byte[] buf, String encoding) throws UnsupportedEncodingException {
        String queryString = new String(buf, encoding);
        parseParameters(results, queryString, encoding);
    }

    /**
     * 解析请求参数
     */
    public static void parseParameters(ParameterMap results, String queryString, String encoding){






    }


}
