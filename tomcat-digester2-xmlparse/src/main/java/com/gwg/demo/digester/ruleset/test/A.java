package com.gwg.demo.digester.ruleset.test;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class A implements Serializable{

    private String name = "abc";

    private List<B> bList = new ArrayList<B>();

    public void addB(B b){
        bList.add(b);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<B> getbList() {
        return bList;
    }

    public void setbList(List<B> bList) {
        this.bList = bList;
    }
}
