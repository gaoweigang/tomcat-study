package com.gwg.demo.digester.ruleset.test;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class B implements Serializable {

    private String name;

    private List<C> cList = new ArrayList<C>();

    public void addC(C c){
        cList.add(c);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<C> getcList() {
        return cList;
    }

    public void setcList(List<C> cList) {
        this.cList = cList;
    }
}
