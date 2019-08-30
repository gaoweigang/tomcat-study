package com.gwg.demo.startup.server2;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ServletProcessor2 {


    public void process(Request request, Response response){

        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf('/')+1);
        //1.servlet仓库
        URL[] urls = new URL[1];
        //指定仓库目录
        try {
            String repository = (new URL("file", null, Constants.WEB_ROOT+File.separator)).toString();
            urls[0] = new URL(repository);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLClassLoader loader = new URLClassLoader(urls);

        //2.加载指定Servlet

        try {
            Class myClass = loader.loadClass(servletName);
            Servlet servlet = (Servlet)myClass.newInstance();
            RequestFacade requestFacade = new RequestFacade(request);
            ResponseFacade responseFacade = new ResponseFacade(response);
            //3.执行servlet的service方法
            servlet.service((ServletRequest) requestFacade, (ServletResponse)responseFacade);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
