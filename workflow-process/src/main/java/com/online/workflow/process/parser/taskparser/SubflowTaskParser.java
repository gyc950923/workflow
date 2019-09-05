package com.online.workflow.process.parser.taskparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.resourcesparser.SubWorkflowProcessParser;
import com.online.workflow.process.resources.SubWorkflowProcess;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.tasks.SubflowTask;
import com.online.workflow.process.tasks.Task;

public class SubflowTaskParser extends TaskParser{

    /**
     * 
     * 功能:序列化SubflowTask<br>
     * 约束:与本函数相关的约束<br>
     * @param subflowTask
     * @param parentElement
     */
    public void doSerialize(SubflowTask subflowTask, Element parentElement) {
        if(ValidateUtil.isNull(subflowTask)){
            return ;
        }
        super.doSerialize((Task)subflowTask, parentElement);
        writeSubWorkflowProcess(subflowTask.getSubWorkflowProcess(), parentElement);
    }

    /**
     * 
     * 功能:序列化SubWorkflowProcess<br>
     * 约束:与本函数相关的约束<br>
     * @param subWorkflowProcess
     * @param parentElement
     */
    private void writeSubWorkflowProcess(SubWorkflowProcess subWorkflowProcess, Element parentElement) {
        if (ValidateUtil.isNull(subWorkflowProcess)) {
            return ;
        }
        Element element = parentElement.addElement(XPDLNames.XPDL_SubWorkflowProcess);
        SubWorkflowProcessParser parser = new SubWorkflowProcessParser();
        parser.doSerialize(subWorkflowProcess , element);
    }

    /**
     * 
     * 功能:反序列化SubflowTask<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param subflowTask
     */
    public void doParser(Element element, SubflowTask subflowTask,WorkflowProcess process) {
        super.doParser(element, (Task)subflowTask, process);
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_SubWorkflowProcess))) {
            if (ValidateUtil.isNull(subflowTask.getSubWorkflowProcess())) {
                subflowTask.setSubWorkflowProcess(new SubWorkflowProcess());
            }
            loadSubWorkflowProcess(element, subflowTask.getSubWorkflowProcess(), process);
        }
    }

    /**
     * 
     * 功能:反序列化SubWorkflowProcess<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param subWorkflowProcess
     */
    private void loadSubWorkflowProcess(Element element, SubWorkflowProcess subWorkflowProcess,WorkflowProcess process) {
        SubWorkflowProcessParser parser = new SubWorkflowProcessParser();
        parser.doParse(element, subWorkflowProcess, process);
    }

}
