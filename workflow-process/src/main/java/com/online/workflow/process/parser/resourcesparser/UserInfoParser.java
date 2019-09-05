package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.resources.UserInfo;

public class UserInfoParser {

    /**
     * 
     * 功能:序列化UserInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param userInfo
     * @param parentElement
     */
    public void doSerialize(UserInfo userInfo, Element parentElement) {
        if(ValidateUtil.isNull(userInfo)){
            return ;
        }
        parentElement.addAttribute("id", userInfo.getId());
        parentElement.addAttribute("name", userInfo.getName());
        parentElement.addAttribute("orderNum", userInfo.getOrderNum());
        parentElement.addAttribute("orgId", userInfo.getOrgId());
        parentElement.addAttribute("orgName", userInfo.getOrgName());
        parentElement.addAttribute("orgFullName", userInfo.getOrgFullName());
        parentElement.addAttribute("deptId", userInfo.getDeptId());
    }

    /**
     * 
     * 功能:反序列化UserInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param userInfo
     */
    public void doParser(Element element, UserInfo userInfo) {
        userInfo.setId(element.attributeValue("id"));
        userInfo.setName(element.attributeValue("name"));
        userInfo.setOrderNum(element.attributeValue("orderNum"));
        userInfo.setOrgId(element.attributeValue("orgId"));
        userInfo.setOrgName(element.attributeValue("orgName"));
        userInfo.setOrgFullName(element.attributeValue("orgFullName"));
        userInfo.setDeptId(element.attributeValue("deptId"));
    }

}
