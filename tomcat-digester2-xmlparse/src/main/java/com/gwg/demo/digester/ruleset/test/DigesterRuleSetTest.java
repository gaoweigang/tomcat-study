package com.gwg.demo.digester.ruleset.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则集 RuleSet 测试
 */
public class DigesterRuleSetTest {

    private A a;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public void digester() throws Exception{

        System.out.println(System.getProperty("user.dir"));
        String fileLocation = System.getProperty("user.dir")+File.separator+"tomcat-xmlparse\\src\\main\\java\\com\\gwg\\demo\\digester\\ruleset\\test\\demo.xml";
        // 读取根据文件的路径，创建InputSource对象，digester解析的时候需要用到
        File file = new File(fileLocation);
        InputStream inputStream = new FileInputStream(file);
        InputSource inputSource = new InputSource(file.toURI().toURL().toString());
        inputSource.setByteStream(inputStream);

        Digester digester = new Digester();
        digester.setValidating(false);//如果为true，则必须为该XML定义DTD; 否则digester不知道如何校验，就会报错。

        /*Map<Class<?>, List<String>> fakeAttributes = new HashMap<>();
        List<String> objectAttrs = new ArrayList<>();
        objectAttrs.add("className");
        fakeAttributes.put(Object.class, objectAttrs);
        List<String> contextAttrs = new ArrayList<>();
        contextAttrs.add("name");
        fakeAttributes.put(B.class, contextAttrs);*/
        digester.setUseContextClassLoader(true);//使用线程上下文类加载器

        digester.push(this);

        digester.addObjectCreate("a", "com.gwg.demo.digester.ruleset.test.A");
        digester.addSetNext("a","setA", "com.gwg.demo.digester.ruleset.test.A");
        digester.addRuleSet(new TestRuleSet("a/"));


        digester.parse(inputSource);



    }

    public static void main(String[] args) throws Exception{

        DigesterRuleSetTest digester = new DigesterRuleSetTest();
        digester.digester();
        A object = digester.getA();
        System.out.println(JSON.toJSONString(object));
    }
}
