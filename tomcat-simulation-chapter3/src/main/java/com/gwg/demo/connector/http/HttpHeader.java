package com.gwg.demo.connector.http;

public class HttpHeader {

    //-----------------------常量
    public static final int INITIAL_NAME_SIZE = 32;
    public static final int INITIAL_VALUE_SIZE = 64;

    public static final int MAX_NAME_SIZE = 128;
    public static final int MAX_VALUE_SIZE = 4096;

    //----------------------成员变量
    public char[] name;
    public int nameEnd;
    public char[] value;
    public int valueEnd;
    protected int hashCode = 0;


    //--------------------------构造函数
    public HttpHeader(char[] name, int nameEnd, char[] value, int valueEnd){
        this.name = name;
        this.nameEnd = nameEnd;
        this.value = value;
        this.valueEnd = valueEnd;
    }

    public HttpHeader(){
        this(new char[INITIAL_NAME_SIZE], 0 , new char[INITIAL_VALUE_SIZE], 0);
    }

    public HttpHeader(String name, String value){
        this.name = name.toLowerCase().toCharArray();
        this.nameEnd = name.length();
        this.value = value.toCharArray();
        this.valueEnd = value.length();
    }


    //------------------------------公共方法
    public void recycle(){
        nameEnd = 0;
        valueEnd = 0;
        hashCode = 0;
    }

    public boolean equals(char[] buf){
        return equals(buf, buf.length);
    }

    public boolean equals(char[] buf, int end){
        if(end != nameEnd){
            return false;
        }
        for(int i =0; i< end; i++){
            if(buf[i] != name[i]){
                return false;
            }
        }
        return true;
    }

    public boolean equals(String str){
        return equals(str.toCharArray(), str.length());
    }


    public boolean valueEquals(char[] buf){
        return valueEquals(buf, buf.length);
    }

    public boolean valueEquals(char[] buf, int end){
        if(end != valueEnd){
            return false;
        }
        for(int i=0; i < end; i++){
            if(buf[i] != value[i]){
                return false;
            }

        }
        return true;
    }

    public boolean valueEquals(String str){
        return valueEquals(str.toCharArray(), str.length());
    }


}
