package com.gwg.demo.standard.mbean.test;

import javax.management.*;

/**
 * 标准MBean是最简单的MBean类型。要想通过标准MBean来管理一个Java对象，需要执行以下步骤：
 * 1.创建一个接口，该接口命名规范为：Java类名+MBean后缀。例如，如果想要管理的Java类名是Car,则需要创建的接口命名为CarMBean
 * 2.修改Java类，让其实现刚刚创建的CarMBean接口。
 * 3.创建一个代理，该代理类必须包含一个MBeanServer实例
 * 4.为MBean创建ObjectName实例。
 * 5.实例化MBeanServer
 * 6.将MBean注册到MBeanServer。
 *
 */
public class StandardAgent {

    //MBean服务器
    private MBeanServer mBeanServer = null;

    public StandardAgent(){
        mBeanServer = MBeanServerFactory.createMBeanServer();
    }

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    //
    public ObjectName createObjectName(String name){
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(name);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return objectName;
    }

    //
    private void createStandardBean(ObjectName objectName, String managedResourceClassName){
        try {
            //createMBean()方法接收托管资源的类名和一个ObjectName实例，该ObjectName实例唯一地标识了为托管资源创建的MBean实例。
            //createMBean()方法也会将创建的MBean实例注册到MBeanServer中。由于标准的MBean实例遵守了特定的命名规则，因此不需要为createMBean()方法提供MBean的类名。如果托管的
            //资源是Car,则创建的MBean的类名是CarMBean
            mBeanServer.createMBean(managedResourceClassName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //代理
        StandardAgent agent = new StandardAgent();
        MBeanServer mBeanServer = agent.getMBeanServer();//获取MBean服务器
        //MBeanServer实例的默认域会作为ObjectName实例的域使用。一个名为type的键会被添加到域后面。键type的值是托管资源的完全限定名
        String domain = mBeanServer.getDefaultDomain();
        System.out.println("domain:"+ domain);
        String managedResourceClassName = "com.gwg.demo.standard.mbean.test.Car";
        ObjectName objectName = agent.createObjectName(domain+":type="+managedResourceClassName);
        agent.createStandardBean(objectName, managedResourceClassName);

        //管理MBean
        try {
            //给属性赋值
            Attribute colorAttribute = new Attribute("Color", "blue");
            mBeanServer.setAttribute(objectName, colorAttribute);
            //获取属性的值
            System.out.println(mBeanServer.getAttribute(objectName, "Color"));

            //调用方法
            mBeanServer.invoke(objectName, "drive", null , null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
