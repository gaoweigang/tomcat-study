package com.gwg.demo.startup;

import com.gwg.demo.connector.http.HttpConnector;

/**
 * 启动应用程序Bootstrap类的main()方法通过实例化HttpConnector类，并调用其start()方法就可以启动应用程序
 */
public class Bootstrap {

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        connector.start();
    }


}
