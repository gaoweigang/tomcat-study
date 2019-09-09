package com.gwg.demo.model.mbean.test;

import javax.management.*;
import javax.management.modelmbean.*;

/**
 * 模型MBean
 * 相对于标准MBean，模型MBean更具有灵活性。在编程上，模型MBean难度更大一些，但也不在需要为可管理的对象修改Java类了。如果不能修改已有的Java类，那么模型
 * MBean是不错的选择。
 */
public class ModelAgent {

    //被托管的资源
    private String MANAGED_CLASS_NAME = "com.gwg.demo.model.mbean.test.Car";


    private MBeanServer mBeanServer = null;

    public ModelAgent(){
        mBeanServer = MBeanServerFactory.createMBeanServer();
    }

    public MBeanServer getMBeanServer() {
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

    /**
     * 声明托管资源所要暴露的所有属性和方法
     * ModelMBeanInfo接口描述了要通过ModelMBean暴露给代理层的构造函数，属性，方法和监听器。其中，构造函数是ModelMBeanConstructorInfo类的实例，属性是ModelMBeanAttributeInfo类的实例。
     * 方法是ModelMBeanOperationInfo类的实例，监听器是ModelMBeanNotificationInfo类的实例。
     *
     */
    private ModelMBeanInfo createModelMBeanInfo(){
        ModelMBeanInfo mBeanInfo = null;
        ModelMBeanAttributeInfo[] attributes = new ModelMBeanAttributeInfo[1];
        ModelMBeanOperationInfo[] operations = new ModelMBeanOperationInfo[3];

        attributes[0] = new ModelMBeanAttributeInfo("Color", "java.lang.String", "the Color.", true, true, false, null);
        operations[0] = new ModelMBeanOperationInfo("drive", "the drive method", null, "void", MBeanOperationInfo.ACTION, null);
        operations[1] = new ModelMBeanOperationInfo("getColor", "get color attribute", null, "java.lang.String", MBeanOperationInfo.ACTION, null);

        //注意首字母大写
        Descriptor setColorDesc = new DescriptorSupport(new String[]{
                "Name=setColor", "DescriptorType=operation",
                "Class="+MANAGED_CLASS_NAME, "Role=operation"
        });
        MBeanParameterInfo[] setColorParams = new MBeanParameterInfo[]{
                (new MBeanParameterInfo("new Color", "java.lang.String", "new Color value"))
        };

        operations[2] = new ModelMBeanOperationInfo("setColor", "set Color attribute",  setColorParams, "void", MBeanOperationInfo.ACTION, setColorDesc );


        mBeanInfo = new ModelMBeanInfoSupport(MANAGED_CLASS_NAME, null, attributes, null, operations, null);
        return mBeanInfo;
    }

    private ModelMBean createModelMBean(){

        ModelMBeanInfo mBeanInfo = createModelMBeanInfo();
        RequiredModelMBean modelMBean = null;
        try {
            modelMBean = new RequiredModelMBean(mBeanInfo);
        } catch (MBeanException e) {
            e.printStackTrace();
        }
        return modelMBean;

    }

    public static void main(String[] args) throws MBeanException, InstanceNotFoundException, InvalidTargetObjectTypeException, InstanceAlreadyExistsException, NotCompliantMBeanException, InvalidAttributeValueException, AttributeNotFoundException, ReflectionException {
        ModelAgent agent = new ModelAgent();

        MBeanServer mBeanServer = agent.getMBeanServer();
        //托管的资源
        Car car = new Car();
        String  domain = mBeanServer.getDefaultDomain();

        //创建一个ObjectName示例，在将MBean实例注册到MBean服务器时使用
        ObjectName objectName = agent.createObjectName(domain+":type=MyCar");
        //实例化一个模型MBean
        ModelMBean modelMBean = agent.createModelMBean();

        //使用模型MBean管理car，在创建了ModelMBean对象后，需要调用ModelMBean接口的setManagedResouce()方法其与托管资源相关联。
        modelMBean.setManagedResource(car, "ObjectReference");
        //将模型MBean注册MBean服务器
        mBeanServer.registerMBean(modelMBean, objectName);

        //设置托管资源的属性Color的值
        Attribute attribute = new Attribute("Color", "green");
        mBeanServer.setAttribute(objectName, attribute);
        String color = (String) mBeanServer.getAttribute(objectName, "Color");
        System.out.println("Color :" + color);

        attribute = new Attribute("Color", "blue");
        mBeanServer.setAttribute(objectName, attribute);
        color = (String) mBeanServer.getAttribute(objectName, "Color");
        System.out.println("Color :"+ color);

        mBeanServer.invoke(objectName, "drive", null, null);


    }
}
