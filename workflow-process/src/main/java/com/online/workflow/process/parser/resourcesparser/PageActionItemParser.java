package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.process.resources.PageActionItem;

public class PageActionItemParser {

    /**
     * 
     * 功能:序列化PageActionItem<br>
     * 约束:与本函数相关的约束<br>
     * @param pageAction
     * @param parentElement
     */
    public void doSerialize(PageActionItem pageAction, Element parentElement) {
        parentElement.addAttribute("buttonName", pageAction.getButtonName());
        parentElement.addAttribute("methodName", pageAction.getMethodName());
    }

    /**
     * 
     * 功能:反序列化PageActionItem<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param pageActionItem
     */
    public void doParser(Element element, PageActionItem pageActionItem) {
        pageActionItem.setButtonName(element.attributeValue("buttonName"));
        pageActionItem.setMethodName(element.attributeValue("methodName"));
    }

}
