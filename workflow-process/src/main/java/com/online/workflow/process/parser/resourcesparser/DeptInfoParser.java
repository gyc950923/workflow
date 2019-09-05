package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.resources.DeptInfo;

public class DeptInfoParser {

    /**
     * 
     * 功能:序列化DeptInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param deptInfo
     * @param parentElement
     */
    public void doSerialize(DeptInfo deptInfo, Element parentElement) {
        if(ValidateUtil.isNull(deptInfo)){
            return ;
        }
        parentElement.addAttribute("id", deptInfo.getId());
        parentElement.addAttribute("name", deptInfo.getName());
    }

    /**
     * 
     * 功能:反序列化DeptInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param deptInfo
     */
    public void doParser(Element element, DeptInfo deptInfo) {
        deptInfo.setId(element.attributeValue("id"));
        deptInfo.setName(element.attributeValue("name"));
    }

    

}
