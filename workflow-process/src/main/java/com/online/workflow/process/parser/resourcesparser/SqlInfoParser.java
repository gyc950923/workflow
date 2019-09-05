package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.resources.SqlInfo;

public class SqlInfoParser {

    /**
     * 
     * 功能:序列化UserInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     */
    public void doSerialize(SqlInfo slqInfo, Element parentElement) {
        if(ValidateUtil.isNull(slqInfo)){
            return ;
        }
        parentElement.addAttribute("sqlKey", slqInfo.getSqlKey());
    }

    /**
     * 
     * 功能:反序列化UserInfo<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     */
    public void doParser(Element element, SqlInfo sqlInfo) {
    	if(null != element){
    		sqlInfo.setSqlKey(element.attributeValue("sqlKey"));
    	}
    }
}
