package com.online.workflow.process.parser.taskparser;

import org.apache.commons.lang.Validate;
import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.resourcesparser.ServiceParser;
import com.online.workflow.process.resources.Service;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.tasks.Task;
import com.online.workflow.process.tasks.ToolTask;

public class ToolTaskParser extends TaskParser{

    /**
     * 
     * 功能:序列化ToolTask<br>
     * 约束:与本函数相关的约束<br>
     * @param toolTask
     * @param parentElement
     */
    public void doSerialize(ToolTask toolTask, Element parentElement) {
        if(ValidateUtil.isNull(toolTask)){
            return ;
        }
        super.doSerialize((Task)toolTask, parentElement);
        writeService(toolTask.getService(), parentElement);   
    }

    /**
     * 
     * 功能:序列化Service<br>
     * 约束:与本函数相关的约束<br>
     * @param service
     * @param parentElement
     */
    private void writeService(Service service, Element parentElement) {
        if (ValidateUtil.isNull(service)) {
            return ;
        }
        Element element = parentElement.addElement(XPDLNames.XPDL_Service);
        ServiceParser parser = new ServiceParser();
        parser.doSerialize(service, parentElement);
    }
    
    public void doParser(Element element, ToolTask toolTask,WorkflowProcess process) {
        super.doParser(element, (Task)toolTask, process);
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_Service))) {
            if (ValidateUtil.isNull(toolTask.getService())) {
                toolTask.setService(new Service());
            }
            loadService(element.element(XPDLNames.XPDL_Service), toolTask.getService(), process);
        }
    }

    /**
     * 
     * 功能:反序列化Service<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param service
     */
    private void loadService(Element element, Service service,WorkflowProcess process) {
        ServiceParser parser = new ServiceParser();
        parser.doParse(element, service, process);
    }


}
