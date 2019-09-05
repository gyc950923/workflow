package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.AbstractWFElementParser;
import com.online.workflow.process.resources.SubWorkflowProcess;

public class SubWorkflowProcessParser extends AbstractWFElementParser{

    /**
     * 
     * 功能:序列化SubWorkflowProcess<br>
     * 约束:与本函数相关的约束<br>
     * @param subWorkflowProcess
     * @param parentElement
     */
    public void doSerialize(SubWorkflowProcess subWorkflowProcess, Element parentElement) {
        super.doSerialize((AbstractWFElement)subWorkflowProcess, parentElement);
        parentElement.addAttribute("workflowProcessId", subWorkflowProcess.getWorkflowProcessId());
    }

    /**
     * 
     * 功能:反序列化SubWorkflowProcess<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param subWorkflowProcess
     */
    public void doParse(Element element, SubWorkflowProcess subWorkflowProcess,WorkflowProcess process) {
        super.doParse(element, (AbstractWFElement)subWorkflowProcess, process);
        subWorkflowProcess.setWorkflowProcessId(element.attributeValue("workflowProcessId"));
    }

}
