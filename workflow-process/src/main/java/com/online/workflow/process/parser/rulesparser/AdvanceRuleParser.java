package com.online.workflow.process.parser.rulesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.rules.AdvanceRule;

public class AdvanceRuleParser {

    /**
     * 
     * 功能:序列化AdvanceRule<br>
     * 约束:与本函数相关的约束<br>
     * @param advanceRule
     * @param parentElement
     */
    public void doSerialize(AdvanceRule advanceRule, Element parentElement) {
        if(ValidateUtil.isNull(advanceRule)){
            return ;
        }
        parentElement.addAttribute("isAdvanceConfirm", String.valueOf(advanceRule.isAdvanceConfirm()));
        parentElement.addAttribute("isAdjustStaff", String.valueOf(advanceRule.isAdjustStaff()));
        parentElement.addAttribute("isToBeRead", String.valueOf(advanceRule.isToBeRead()));
    }

    /**
     * 
     * 功能:反序列化AdvanceRule<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param advanceRule
     */
    public void doParser(Element element, AdvanceRule advanceRule) {
        if(!ValidateUtil.isNull(element.attribute("isAdvanceConfirm"))) {
            advanceRule.setAdvanceConfirm(Boolean.valueOf(element.attributeValue("isAdvanceConfirm")));
        }
        if (!ValidateUtil.isNull(element.attribute("isAdjustStaff"))) {
            advanceRule.setAdjustStaff(Boolean.valueOf(element.attributeValue("isAdjustStaff")));
        }
        if (!ValidateUtil.isNull(element.attribute("isToBeRead"))) {
        	advanceRule.setIsToBeRead(Boolean.valueOf(element.attributeValue("isToBeRead")));
        }
    }

    

}
