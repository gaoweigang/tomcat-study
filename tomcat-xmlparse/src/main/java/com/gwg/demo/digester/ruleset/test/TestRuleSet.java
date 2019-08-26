package com.gwg.demo.digester.ruleset.test;

import com.gwg.demo.digester.rule.test.A;
import org.apache.commons.digester.*;

/**
 * 规则集中可以写很多规则，匹配就处理，不匹配就不处理
 * tomcat8.5中使用的是digester2.x
 */
public class TestRuleSet extends RuleSetBase {


    //用于识别元素的匹配模式前缀
    protected final String prefix;

    public TestRuleSet(){
        this("");
    }

    public TestRuleSet(String prefix){
        this.prefix = prefix;
    }



    @Override
    public void addRuleInstances(Digester digester) {
        /*digester.addObjectCreate("a", "com.gwg.demo.digester.ruleset.test.A");
        digester.addSetNext("a","setA", "com.gwg.demo.digester.ruleset.test.A");*/

        digester.addObjectCreate(prefix+"b", "com.gwg.demo.digester.ruleset.test.B", "className");
        digester.addRule(prefix+"b", new SetNextRule("addB", "com.gwg.demo.digester.ruleset.test.B"));
        digester.addSetProperties(prefix+"b");//从object stack对象栈取出顶不对象，然后设置对象的值，在这里取出的顶部对象是b
        //模式匹配上了就处理，否则不处理，可以写n多规则
        digester.addObjectCreate(prefix+"b/c", "com.gwg.demo.digester.ruleset.test.C", "className");
        digester.addRule(prefix+"b/c", new SetNextRule("addC", "com.gwg.demo.digester.ruleset.test.C"));

    }
}
