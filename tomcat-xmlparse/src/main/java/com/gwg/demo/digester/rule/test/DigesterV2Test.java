package com.gwg.demo.digester.rule.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.ObjectCreateRule;
import org.apache.commons.digester3.SetNextRule;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 第二种写法 与第一种写法等效
 */
public class DigesterV2Test {

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

     /*   ObjectCreateRule ruleA1 = new ObjectCreateRule("a", A.class);
        SetNextRule ruleA2= new SetNextRule("setA",A.class);
        ruleA2.setFireOnBegin(true);
        ObjectCreateRule ruleB1 = new ObjectCreateRule("a/b", B.class);
        SetNextRule ruleB2= new SetNextRule("addB", B.class);//调用A.addB设置a对象中bList的值
        ruleB2.setFireOnBegin(true);
        ObjectCreateRule ruleC1 = new ObjectCreateRule("a/b/c", C.class);
        SetNextRule ruleC2= new SetNextRule("addC", C.class);
        ruleC2.setFireOnBegin(true); //必须，否则模式匹配了，也不会执行begin方法


        digester.addRule("a", ruleA1);
        digester.addRule("a", ruleA2);
        digester.addRule("a/b", ruleB1);
        digester.addRule("a/b", ruleB2);
        digester.addRule("a/b/c", ruleC1);
        digester.addRule("a/b/c", ruleC2);*/
        //如果className值为空，则使用A.class.getName()
        digester.addObjectCreate("a", A.class.getName(), "className");
        digester.addSetNext("a", "setA", A.class.getName());
        digester.addObjectCreate("a/b", B.class.getName(), "className");
        digester.addSetNext("a/b", "addB", B.class.getName());
        digester.addObjectCreate("a/b/c", C.class.getName(), "className");
        digester.addSetNext("a/b/c", "addC", C.class.getName());//从object stack上取出最顶上的对象,即b,然后调用对象b的addC方法


        digester.parse(inputSource);



    }

    public static void main(String[] args) throws Exception{
        DigesterV2Test digester = new DigesterV2Test();
        digester.digester();
        System.out.println(JSON.toJSONString(digester.getA()));
        System.out.println( JSON.toJSONString(digester.getA(), SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullStringAsEmpty));

    }
}
