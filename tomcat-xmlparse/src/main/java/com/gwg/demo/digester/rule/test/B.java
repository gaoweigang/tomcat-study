package com.gwg.demo.digester.rule.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class B implements Serializable {

    private List<C> cList = new ArrayList<C>();

    public void addC(C c){
        cList.add(c);
    }

    public List<C> getcList() {
        return cList;
    }

    public void setcList(List<C> cList) {
        this.cList = cList;
    }
}
