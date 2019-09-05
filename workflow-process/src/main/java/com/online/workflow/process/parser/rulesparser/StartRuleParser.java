package com.online.workflow.process.parser.rulesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.rules.StartRule;

public class StartRuleParser {

    /**
     * 
     * 功能:序列化StartRule
     * 约束:与本函数相关的约束
     * @param startRule
     * @param parentElement
     */
    public void doSerialize(StartRule startRule, Element parentElement) {
        if (ValidateUtil.isNull(startRule)) {
            return ;
        }
        parentElement.addAttribute("taskName", startRule.getTaskName());
        parentElement.addAttribute("taskInstanceCreator", startRule.getTaskInstanceCreator());
        parentElement.addAttribute("formTaskInstanceRunner", startRule.getFormTaskInstanceRunner());
        parentElement.addAttribute("toolTaskInstanceRunner", startRule.getToolTaskInstanceRunner());
        parentElement.addAttribute("subflowTaskInstanceRunner", startRule.getSubflowTaskInstanceRunner());
        parentElement.addAttribute("formTaskInstanceCompletionEvaluator", startRule.getFormTaskInstanceCompletionEvaluator());
        parentElement.addAttribute("toolTaskInstanceCompletionEvaluator", startRule.getToolTaskInstanceCompletionEvaluator());
        parentElement.addAttribute("subflowTaskInstanceCompletionEvaluator", startRule.getSubflowTaskInstanceCompletionEvaluator());
    }

    /**
     * 
     * 功能:反序列化StartRule
     * 约束:与本函数相关的约束
     * @param element
     * @param startRule
     */
    public void doParser(Element element, StartRule startRule) {
        startRule.setTaskName(element.attributeValue("taskName"));
        startRule.setTaskInstanceCreator(element.attributeValue("taskInstanceCreator"));
        startRule.setFormTaskInstanceRunner(element.attributeValue("formTaskInstanceRunner"));
        startRule.setToolTaskInstanceRunner(element.attributeValue("toolTaskInstanceRunner"));
        startRule.setSubflowTaskInstanceRunner(element.attributeValue("subflowTaskInstanceRunner"));
        startRule.setFormTaskInstanceCompletionEvaluator(element.attributeValue("formTaskInstanceCompletionEvaluator"));
        startRule.setToolTaskInstanceCompletionEvaluator(element.attributeValue("toolTaskInstanceCompletionEvaluator"));
        startRule.setSubflowTaskInstanceCompletionEvaluator(element.attributeValue("subflowTaskInstanceCompletionEvaluator"));
    } 

}
