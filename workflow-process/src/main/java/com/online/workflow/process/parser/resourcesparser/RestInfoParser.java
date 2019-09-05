package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.resources.RestInfo;
import com.online.workflow.process.resources.SqlInfo;

public class RestInfoParser {

    /**
     * 
     * 功能:序列化UserInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     */
    public void doSerialize(RestInfo restInfo, Element parentElement) {
        if(ValidateUtil.isNull(restInfo)){
            return ;
        }
        parentElement.addAttribute("restKey", restInfo.getRestKey());
    }

    /**
     * 
     * 功能:反序列化UserInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     */
    public void doParser(Element element, RestInfo restInfo) {
    	if(null != element){
    		restInfo.setRestKey(element.attributeValue("restKey"));
    	}
    }
}
