package com.online.workflow.process.parser.netparser;

import org.dom4j.Element;

import com.online.workflow.process.EventListener;
import com.online.workflow.process.WorkflowProcess;

public class EventListenerParser {

    /**
     * 
     * 功能:序列化EventListener<br>
     * 约束:与本函数相关的约束<br>
     * @param eventListener
     * @param parentElement
     */
    public void doSerialize(EventListener eventListener, Element parentElement) {
        parentElement.addAttribute("className", eventListener.getClassName()); 
    }

    /**
     * 
     * 功能:反序列化EventListener<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param eventListener
     */
    public void doParse(Element element, EventListener eventListener,WorkflowProcess process) {
        eventListener.setClassName(element.attributeValue("className"));
    }
    
}
