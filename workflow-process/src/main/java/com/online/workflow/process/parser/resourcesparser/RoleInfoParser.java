package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.resources.RoleInfo;

public class RoleInfoParser {

    /**
     * 
     * 功能:序列化RoleInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param roleInfo
     * @param parentElement
     */
    public void doSerialize(RoleInfo roleInfo, Element parentElement) {
        if(ValidateUtil.isNull(roleInfo)){
            return ;
        }
        parentElement.addAttribute("id", roleInfo.getId());
        parentElement.addAttribute("name", roleInfo.getName());
    }

    /**
     * 
     * 功能:反序列化RoleInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param roleInfo
     */
    public void doParser(Element element, RoleInfo roleInfo) {
        roleInfo.setId(element.attributeValue("id"));
        roleInfo.setName(element.attributeValue("name"));
    }

    

}
