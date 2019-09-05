package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.resources.OrgRoleInfo;

public class OrgRoleInfoParser {

    /**
     * 
     * 功能:序列化DeptInfo<br>
     * 约束:与本函数相关的约束<br>
     */
    public void doSerialize(OrgRoleInfo orgRoleInfo, Element parentElement) {
        if(ValidateUtil.isNull(orgRoleInfo)){
            return ;
        }
        parentElement.addAttribute("id", orgRoleInfo.getId());
        parentElement.addAttribute("name", orgRoleInfo.getName());
    }

    /**
     * 
     * 功能:反序列化DeptInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     */
    public void doParser(Element element, OrgRoleInfo orgRoleInfo) {
    	orgRoleInfo.setId(element.attributeValue("id"));
    	orgRoleInfo.setName(element.attributeValue("name"));
    }

    

}
