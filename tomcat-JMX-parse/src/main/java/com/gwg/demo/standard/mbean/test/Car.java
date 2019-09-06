package com.gwg.demo.standard.mbean.test;

/**
 * 使用标准MBean管理资源时，需要定义一个接口(在这里是CarMBean)，然后让托管资源实现该接口
 */
public class Car implements CarMBean{

    private String color = "red";

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void drive(){
        System.out.println("Baby you can drive my car.");
    }
}
