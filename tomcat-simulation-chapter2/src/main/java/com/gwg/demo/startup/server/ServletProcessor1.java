package com.gwg.demo.startup.server;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor1 {

    public void process(Request request, Response response){
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf('/')+1);
        URLClassLoader loader = null;

        //创建类载入器
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File file = new File(Constants.WEB_ROOT);
            //在Servlet容器中，类载入器查找servlet类的目录称为仓库
            String repository = (new URL("file", null, file.getCanonicalPath()+File.separator)).toString();//仓库
            /*
             在应用程序中，类载入器只需要查找一个位置，即工作目录下的webroot目录。因此，需要先创建只有一个URL的数组。URL类提供了一系列构造函数，
             在此我们使用到的构造函数有如下两个：
             public URL(URL context, java.lang.String spec, URLStreamHandler handler) throws MalformedURLException;
             public URL(java.lang.String protocol, java.lang.String host, java.lang.String file) throws MalformedURLException;
             */
            urls[0] = new URL(null, repository, streamHandler);
            //urls是一个java.net.URL对象数组，当载入一个类时每个URL对象都指明了类载入器要到哪里查找类。若一个URL以"/"结尾，则表明它指向的是一个目录。
            // 否则，URL默认指向一个jar文件，根据需要类加载器会下载并打开这个JAR文件。
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //调用loadClass()方法来载入servlet类
        Class myClass = null;
        try {
            //加载Servlet的class文件，需要事先将servlet编译好
            myClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Servlet  servlet = null;
        try {
            //
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}

