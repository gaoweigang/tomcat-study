package com.gwg.demo.standard.mbean.test;

import javax.management.*;

/**
 * 标准MBean测试
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
            mBeanServer.createMBean(managedResourceClassName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //代理
        StandardAgent agent = new StandardAgent();
        MBeanServer mBeanServer = agent.getMBeanServer();//获取MBean服务器
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
