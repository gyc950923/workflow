package com.online.workflow.process.parser.rulesparser;

import org.dom4j.Element;

import com.online.workflow.process.rules.Rule;

public class RuleParser {

    /**
     * 
     * 功能:序列化Rule
     * 约束:与本函数相关的约束
     * @param rule
     * @param parentElement
     */
    public void doSerialize(Rule rule, Element parentElement) {
        parentElement.addAttribute("id", rule.getId());
        parentElement.addAttribute("name", rule.getName());
    }

    /**
     * 
     * 功能:反序列化Rule
     * 约束:与本函数相关的约束
     * @param element
     * @param rule
     */
    public void doParser(Element element, Rule rule) {
        rule.setId(element.attributeValue("id"));
        rule.setName(element.attributeValue("name"));
    }

    

    

}
