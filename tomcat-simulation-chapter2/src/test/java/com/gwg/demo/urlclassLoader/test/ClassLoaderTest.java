package com.gwg.demo.urlclassLoader.test;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * URLClassLoader测试
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception{

        File filpath=new File("");
        String path = "file:"+filpath.getAbsoluteFile()+File.separator+"webroot";
        System.out.println(path);
        URL newurl=new URL(path);
        URLClassLoader classLoader=new URLClassLoader(new URL[]{newurl});
        Class<?> methtClass = classLoader.loadClass("PrimitiveServlet");
        Object obj = methtClass.newInstance();


    }

    @Test
    public void testOne() throws Exception{
        byte[] bytes = new byte[1024];
        ByteInputStream inputStream = new ByteInputStream();
        int ch = inputStream.read(bytes);
        System.out.println(ch);
        int ch2 = inputStream.read(bytes);
        System.out.println(ch2);


    }
}
