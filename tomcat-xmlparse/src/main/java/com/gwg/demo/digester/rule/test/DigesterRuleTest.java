package com.gwg.demo.digester.rule.test;

import com.alibaba.fastjson.JSON;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.digester.SetNextRule;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DigesterRuleTest {

    private A a;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public void digester() throws Exception{

        System.out.println(System.getProperty("user.dir"));
        String fileLocation = System.getProperty("user.dir")+File.separator+"tomcat-xmlparse\\src\\main\\java\\com\\gwg\\demo\\digester\\rule\\test\\demo.xml";
        // 读取根据文件的路径，创建InputSource对象，digester解析的时候需要用到
        File file = new File(fileLocation);
        InputStream inputStream = new FileInputStream(file);
        InputSource inputSource = new InputSource(file.toURI().toURL().toString());
        inputSource.setByteStream(inputStream);

        Digester digester = new Digester();
        digester.push(this);

        ObjectCreateRule ruleA1 = new ObjectCreateRule("a", A.class);
        SetNextRule ruleA2= new SetNextRule("setA","com.gwg.demo.digester.rule.test.A");
        //ruleA2.setFireOnBegin(true);//digetster3中需要配置
        ObjectCreateRule ruleB1 = new ObjectCreateRule("a/b", B.class);
        SetNextRule ruleB2= new SetNextRule("addB", "com.gwg.demo.digester.rule.test.B");//调用A.addB设置a对象中bList的值
        //ruleB2.setFireOnBegin(true); //digester3中需要配置
        ObjectCreateRule ruleC1 = new ObjectCreateRule("a/b/c", C.class);
        SetNextRule ruleC2= new SetNextRule("addC", "com.gwg.demo.digester.rule.test.C");
        //ruleC2.setFireOnBegin(true); //digester3中必须，否则模式匹配了，也不会执行begin方法


        digester.addRule("a", ruleA1);//按设置的顺序匹配调用
        digester.addRule("a", ruleA2);
        digester.addRule("a/b", ruleB1);
        digester.addRule("a/b", ruleB2);
        digester.addRule("a/b/c", ruleC1);
        digester.addRule("a/b/c", ruleC2);

        digester.parse(inputSource);



    }

    public static void main(String[] args) throws Exception{
        DigesterRuleTest digester = new DigesterRuleTest();
        digester.digester();
        System.out.println(JSON.toJSONString(digester.getA()));
    }
}
