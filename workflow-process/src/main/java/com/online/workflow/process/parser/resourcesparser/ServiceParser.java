package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.AbstractWFElementParser;
import com.online.workflow.process.resources.Service;

public class ServiceParser extends AbstractWFElementParser{

    /**
     * 
     * 功能:序列化Service<br>
     * 约束:与本函数相关的约束<br>
     * @param service
     * @param parentElement
     */
    public void doSerialize(Service service, Element parentElement) {
        if(ValidateUtil.isNull(service)){
            return ;
        }
        super.doSerialize((AbstractWFElement)service, parentElement);
        parentElement.addAttribute("Handler", service.getHandler());
    }
    
    /**
     * 
     * 功能:反序列化Service<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param service
     */
    public void doParse(Element element, Service service,WorkflowProcess process) {
        super.doParse(element, (AbstractWFElement)service, process);
        service.setHandler(element.attributeValue("Handler"));
    }

    
}
