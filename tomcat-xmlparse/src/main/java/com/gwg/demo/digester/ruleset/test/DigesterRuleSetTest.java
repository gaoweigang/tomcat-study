package com.gwg.demo.digester.ruleset.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
