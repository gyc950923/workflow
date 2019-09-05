package com.online.workflow.process.parser.rulesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.parser.resourcesparser.BackNodeParser;
import com.online.workflow.process.resources.BackNode;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.rules.BackRule;
import com.online.workflow.process.rules.Rule;

public class BackRuleParser extends RuleParser{

    public void doSerialize(BackRule backRule, Element parentElement) {
        if (ValidateUtil.isNull(backRule)) {
            return ;
        }
        super.doSerialize((Rule)backRule, parentElement);
        parentElement.addAttribute("isBackConfirm", String.valueOf(backRule.isBackConfirm()));
        parentElement.addAttribute("returnMode", String.valueOf(backRule.getReturnMode()));
        writeBackNode(backRule.getBackRange(),parentElement);
    }

    /**
     * 
     * 功能:序列化BackNode<br>
     * 约束:与本函数相关的约束<br>
     * @param backNode
     * @param parentElement
     */
    private void writeBackNode(BackNode backNode, Element parentElement) {
        if (ValidateUtil.isNull(backNode)) {
            return ;
        }
        Element element = parentElement.addElement(XPDLNames.XPDL_BackNode);
        BackNodeParser parser = new BackNodeParser();
        parser.doSerialize(backNode,element);
    }

    /**
     * 
     * 功能:反序列化BackRule<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param backRule
     */
    public void doParser(Element element, BackRule backRule) {
        super.doParser(element, (Rule)backRule);
        if(!ValidateUtil.isNull(element.attribute("isBackConfirm"))) {
            backRule.setBackConfirm(Boolean.valueOf(element.attributeValue("isBackConfirm")));
        }
        if (!ValidateUtil.isNull(element.attribute("returnMode"))) {
            backRule.setReturnMode(Integer.valueOf(element.attributeValue("returnMode")));
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_BackNode))) {
            if (ValidateUtil.isNull(backRule.getBackRange())) {
                backRule.setBackRange(new BackNode());
            }
            loadBackRange(element.element(XPDLNames.XPDL_BackNode), backRule.getBackRange());
        }
    }

    /**
     * 
     * 功能:反序列化BackRange<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param backRange
     */
    private void loadBackRange(Element element, BackNode backRange) {
        BackNodeParser parser = new BackNodeParser();
        parser.doParser(element, backRange);
    }

}
