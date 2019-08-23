package com.gwg.demo.digester.ruleset.test;


import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.RuleSetBase;

public class MyRuleSet extends RuleSetBase {

    public MyRuleSet()
    {
        this("");
    }

    public MyRuleSet(String prefix)
    {
        super();
        this.prefix = prefix;
    }

    protected String prefix = null;

    public void addRuleInstances(Digester digester)
    {
        digester.addObjectCreate( prefix + "foo/bar",
                "com.mycompany.MyFoo" );
        digester.addSetProperties( prefix + "foo/bar" );
    }

}