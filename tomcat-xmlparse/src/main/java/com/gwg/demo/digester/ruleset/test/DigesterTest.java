package com.gwg.demo.digester.ruleset.test;


import org.apache.commons.digester3.Digester;

public class DigesterTest {

    public static void main(String[] args) {
        Digester digester = new Digester();
        digester.addRuleSet(new MyRuleSet("baz/"));

    }
}
