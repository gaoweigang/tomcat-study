package com.gwg.demo.model.mbean.test;

import javax.management.*;
import javax.management.modelmbean.*;

public class ModelAgent {
    private String MANAGED_CLASS_NAME = "";
    private MBeanServer mBeanServer = null;

    public ModelAgent(){
        mBeanServer = MBeanServerFactory.createMBeanServer();
    }

    public MBeanServer getMBeanServer(){
        return mBeanServer;
    }

    private ObjectName createObjectName(String name){
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(name);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return objectName;
    }

    private ModelMBean createMBean(ObjectName objectName, String mbeanName){
        ModelMBeanInfo mBeanInfo = createModelM
    }

    private ModelMBeanInfo createModelMBeanInfo(ObjectName inMBeanObjectName, String inMBeanName){
        ModelMBeanInfo mBeanInfo = null;
        ModelMBeanAttributeInfo[] attributes = new ModelMBeanAttributeInfo[1];
        ModelMBeanOperationInfo[] operations = new ModelMBeanOperationInfo[3];

        try{
            attributes[0] = new ModelMBeanAttributeInfo("Color", "java.lang.String", "the Color.", true, true,  false, null);

            operations[0] = new ModelMBeanOperationInfo("drive", "the drive method", null, "void", MBeanOperationInfo.ACTION, null);

            operations[1] = new ModelMBeanOperationInfo("getColor", "get color attribute", null, "java.lang.String", MBeanOperationInfo.ACTION, null);

            Descriptor setColorDesc = new DescriptorSupport(new String[]{"name=setColor", "descriptorType=operation", "class="+MANAGED_CLASS_NAME, "role=operation"});

            MBeanParameterInfo[] setColorParams = new MBeanParameterInfo[]{
                    (new MBeanParameterInfo("new Color", "java.lang.String", "new Color value"))
            };

        } catch (Exception e){
            e.printStackTrace();
        }
        return mBeanInfo;


    }
}
