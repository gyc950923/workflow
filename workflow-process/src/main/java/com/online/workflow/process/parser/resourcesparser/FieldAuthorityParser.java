package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.process.resources.FieldAuthority;

public class FieldAuthorityParser {
    
    /**
     * 
     * 功能:序列化FieldAuthority<br>
     * 约束:与本函数相关的约束<br>
     * @param fieldAuthority
     * @param parentElement
     */
    public void doSerialize(FieldAuthority fieldAuthority, Element parentElement) {
        parentElement.addAttribute("code", fieldAuthority.getCode());
        parentElement.addAttribute("isVisible", String.valueOf(fieldAuthority.getIsVisible()));
        parentElement.addAttribute("isRead", String.valueOf(fieldAuthority.getIsRead()));
    }

    /**
     * 
     * 功能:反序列化FieldAuthority<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param fieldAuthority
     */
    public void doParser(Element element, FieldAuthority fieldAuthority) {
        fieldAuthority.setCode(element.attributeValue("code"));
        fieldAuthority.setIsVisible(Integer.parseInt(element.attributeValue("isVisible")));
        fieldAuthority.setIsRead(Integer.parseInt(element.attributeValue("isRead")));
    }

    

}
