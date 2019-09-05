package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.resources.BackNode;

public class BackNodeParser {

    /**
     * 
     * 功能:序列化BackNode<br>
     * 约束:与本函数相关的约束<br>
     * @param backNode
     * @param parentElement
     */
    public void doSerialize(BackNode backNode, Element parentElement) {
        if (ValidateUtil.isNull(backNode)) {
            return ;
        }
        parentElement.addAttribute("backRangeType", String.valueOf(backNode.getBackRangeType()));
        parentElement.addAttribute("nodeId", backNode.getNodeId());
        parentElement.addAttribute("name", backNode.getName());        
    }

    /**
     * 
     * 功能:反序列化BackNode<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param backNode
     */
    public void doParser(Element element, BackNode backNode) {
        if (!"null".equals(element.attributeValue("backRangeType"))) {
            backNode.setBackRangeType(Integer.valueOf(element.attributeValue("backRangeType")));
        }
        backNode.setNodeId(element.attributeValue("nodeId"));
        backNode.setName(element.attributeValue("name"));
    }

    

}
