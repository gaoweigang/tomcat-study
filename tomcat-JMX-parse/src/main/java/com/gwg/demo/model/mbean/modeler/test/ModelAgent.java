package com.gwg.demo.model.mbean.modeler.test;


import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import org.apache.commons.modeler.ManagedBean;
import org.apache.commons.modeler.Registry;

import javax.management.*;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBean;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ModelAgent {

    /**
     *Registry类定义了很多方法，下面是可以使用该类做的事情：
     * 1.使用loadRegistry()方法读取MBean的描述符文件
     * 2.创建一个ManagedMBean对象，用于创建模型MBean的实例
     * 3.获取MBeanServer类的一个实例，所以不再需要调用MBeanServerFactory类的createMBeanServer()方法了。
     */
    private Registry registry;
    private MBeanServer mBeanServer;

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    public ModelAgent(){
        registry = createRegistry();
        try{
            //3.获取MBeanServer类的一个实例，所以不再需要调用MBeanServerFactory类的createMBeanServer()方法了。
            mBeanServer = Registry.getServer();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    //1.使用loadRegistry()方法读取MBean的描述符文件
    public Registry createRegistry(){

        //MBean的描述符文件路径
        String path = "tomcat-JMX-parse\\src\\main\\java\\com\\gwg\\demo\\model\\mbean\\modeler\\test\\mbeans-descriptors.xml";
        Registry registry = null;
        try {
            URL url = new URL("file", null, System.getProperty("user.dir")+ File.separator+path);
            System.out.println(url.toString());
            InputStream stream = url.openStream();
            Registry.loadRegistry(stream);
            stream.close();
            registry = Registry.getRegistry();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return registry;
    }

    public ObjectName createObjectName() throws MalformedObjectNameException {
        ObjectName objectName = null;
        String domain = mBeanServer.getDefaultDomain();
        objectName = new ObjectName(domain+":type=MyCar");
        return objectName;
    }


    public ModelMBean createModelMBean(String mBeanName) throws MalformedObjectNameException, InvalidTargetObjectTypeException, MBeanException, InstanceNotFoundException {
        //创建一个ManagedMBean对象，用于创建模型MBean的实例。
        //ManagedMBean对象描述了一个模型MBean,该类用于取代javax.management.MBeanInfo对象。
        ManagedBean managedBean = registry.findManagedBean(mBeanName);
        if(managedBean == null){
            System.out.println("ManagedBean null");
            return null;
        }
        //创建模型MBean实例
        ModelMBean mbean = managedBean.createMBean();
        return mbean;

    }

    public static void main(String[] args) throws MalformedObjectNameException {
        ModelAgent agent = new ModelAgent();
        MBeanServer mBeanServer = agent.getMBeanServer();
        Car car = new Car();
        System.out.println("Creating ObjectName");
        //创建一个ObjectName,在将模型MBean注册到MBean服务器的时候使用
        ObjectName objectName = agent.createObjectName();

        try {
            //模型MBean
            ModelMBean modelMBean = agent.createModelMBean("myMBean");
            //设置模型MBean管理的资源，car被托管的资源
            modelMBean.setManagedResource(car, "ObjectReference");
            //将MBean注册到MBean服务器上
            mBeanServer.registerMBean(modelMBean, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //管理bean
        Attribute attribute = new Attribute("Color", "green");
        try {
            mBeanServer.setAttribute(objectName, attribute);
            String color = (String) mBeanServer.getAttribute(objectName, "Color");
            System.out.println("Color :"+ color);
            System.out.println("car Color :"+ car.getColor());
            attribute = new Attribute("Color", "red");
            mBeanServer.setAttribute(objectName, attribute);
            color = (String) mBeanServer.getAttribute(objectName, "Color");
            System.out.println("Color : "+color);
            mBeanServer.invoke(objectName, "drive", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
