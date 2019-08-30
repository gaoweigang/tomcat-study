package com.gwg.demo.digester.rule.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class A implements Serializable{

    private List<B> bList = new ArrayList<B>();

    public void addB(B b){
        bList.add(b);
    }

    public List<B> getbList() {
        return bList;
    }

    public void setbList(List<B> bList) {
        this.bList = bList;
    }
}
