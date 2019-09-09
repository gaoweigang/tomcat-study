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

    private Registry registry;
    private MBeanServer mBeanServer;

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    public ModelAgent(){
        registry = createRegistry();
        try{
            mBeanServer = Registry.getServer();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    public Registry createRegistry(){

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
        ManagedBean managedBean = registry.findManagedBean(mBeanName);
        if(managedBean == null){
            System.out.println("ManagedBean null");
            return null;
        }
        ModelMBean mbean = managedBean.createMBean();
        ObjectName objectName = createObjectName();
        return mbean;

    }

    public static void main(String[] args) throws MalformedObjectNameException {
        ModelAgent agent = new ModelAgent();
        MBeanServer mBeanServer = agent.getMBeanServer();
        Car car = new Car();
        System.out.println("Creating ObjectName");
        ObjectName objectName = agent.createObjectName();

        try {
            ModelMBean modelMBean = agent.createModelMBean("myMBean");
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
