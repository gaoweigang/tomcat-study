package com.gwg.demo.digester;

import org.apache.commons.digester3.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 参考：https://www.jianshu.com/p/8d9a6ae40303
 * https://www.cnblogs.com/lay2017/p/9801690.html
 */
public class DigesterTest {
    // 属性和get/set方法，假设我们解析出来的School对象放在这儿
    private School school;
    public School getSchool() {
        return school;
    }
    public void setSchool(School s) {
        this.school = s;
    }

    private void digester() throws IOException, SAXException {
        System.out.println(System.getProperty("user.dir"));
        String fileLocation = System.getProperty("user.dir")+File.separator+"tomcat-xmlparse\\src\\main\\java\\com\\gwg\\demo\\digester\\school.xml";
        // 读取根据文件的路径，创建InputSource对象，digester解析的时候需要用到
        File file = new File(fileLocation);
        InputStream inputStream = new FileInputStream(file);
        InputSource inputSource = new InputSource(file.toURI().toURL().toString());
        inputSource.setByteStream(inputStream);

        // 创建Digester对象
        Digester digester = new Digester();
        // 是否需要用DTD验证XML文档的合法性。如果需要校验，则XML文件中需要指定dtd文件的路径，否则会报校验异常
        digester.setValidating(true);//如果为true，则必须为该XML定义DTD; 否则digester不知道如何校验，就会报错。
        // 将当前对象放到对象堆的最顶层，这也是这个类为什么要有school属性的原因！
        digester.push(this);

        /*
         * 下面开始为Digester创建匹配规则
         * Digester中的School、School/Grade、School/Grade/Class，分别对应School.xml的School、Grade、Class节点
         */

        // 为School创建规则

        /*
         * Digester.addObjectCreate(String pattern, String className, String attributeName)
         * pattern, 匹配的节点
         * className, 该节点对应的默认实体类
         * attributeName, 如果该节点有className属性, 用className的值替换默认实体类
         *
         * Digester匹配到School节点
         *
         * 1. 如果School节点没有className属性，将创建com.juconcurrent.learn.apache.digester.School对象；
         * 2. 如果School节点有className属性，将创建指定的(className属性的值)对象
         */
        digester.addObjectCreate("School", School.class.getName(), "className");
        // 将指定节点的属性映射到对象，即将School节点的name的属性映射到School.java
        digester.addSetProperties("School");

        /*
         * Digester.addSetNext(String pattern, String methodName, String paramType)
         * pattern, 匹配的节点
         * methodName, 调用父节点的方法
         * paramType, 父节点的方法接收的参数类型
         * Digester匹配到School节点，将调用DigesterTest(School的父节点)的setSchool方法，参数为School对象
         */
        digester.addSetNext("School", "setSchool", School.class.getName());

        // 为School/Grade创建规则
        digester.addObjectCreate("School/Grade", Grade.class.getName(), "className");
        digester.addSetProperties("School/Grade");

        // Grade的父节点为School
        digester.addSetNext("School/Grade", "addGrade", Grade.class.getName());

        // 为School/Grade/Class创建规则
        digester.addObjectCreate("School/Grade/Class", Class.class.getName(), "className");
        digester.addSetProperties("School/Grade/Class");
        digester.addSetNext("School/Grade/Class", "addClass", Class.class.getName());
        // 解析输入源
        digester.parse(inputSource);
    }

    // 只是将School对象进行控制台输出
    private void print(School s) {
        if (s != null) {
            System.out.println(s.getName() + "有" + s.getGrades().length + "个年级");
            for (int i = 0; i < s.getGrades().length; i++) {
                if (s.getGrades()[i] != null) {
                    Grade g = s.getGrades()[i];
                    System.out.println(g.getName() + "年级 有 " + g.getClasses().length + "个班：");
                    for (int j = 0; j < g.getClasses().length; j++) {
                        if (g.getClasses()[j] != null) {
                            Class c = g.getClasses()[j];
                            System.out.println(c.getName() + "班有" + c.getNumber() + "人");
                        }
                    }
                }
            }
        }
    }

    // 入口main()方法
    public static void main(String[] args) throws IOException, SAXException {
        DigesterTest digesterTest = new DigesterTest();
        digesterTest.digester();
        digesterTest.print(digesterTest.school);
    }
}