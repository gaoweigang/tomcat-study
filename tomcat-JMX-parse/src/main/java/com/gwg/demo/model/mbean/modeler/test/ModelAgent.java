package com.gwg.demo.model.mbean.modeler.test;


import org.apache.commons.modeler.ManagedBean;
import org.apache.commons.modeler.Registry;

import javax.management.*;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBean;
import java.io.InputStream;
import java.net.URL;

public class ModelAgent {

    private Registry registry;
    private MBeanServer mBeanServer;

    public ModelAgent(){
        registry = createRegistry();
        try{
            mBeanServer = Registry.getServer();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    public Registry createRegistry(){
        Registry registry = null;


        try {
            URL url = ModelAgent.class.getResource("mbeans-descriptors.xml");
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

}
