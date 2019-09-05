package com.online.workflow.process.parser.taskparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.AbstractWFElementParser;
import com.online.workflow.process.parser.resourcesparser.DurationParser;
import com.online.workflow.process.resources.Duration;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.tasks.Task;

public class TaskParser extends AbstractWFElementParser{

    /**
     * 
     * 功能:序列化Task<br>
     * 约束:与本函数相关的约束<br>
     * @param task
     * @param parentElement
     */
    public void doSerialize(Task task, Element parentElement) {
        if(ValidateUtil.isNull(task)){
            return ;
        }
        super.doSerialize((AbstractWFElement)task, parentElement);
        parentElement.addAttribute("taskType", String.valueOf(task.getTaskType()));
        
        writeDuration(task.getDuration(),parentElement);
        
        parentElement.addAttribute("priority", String.valueOf(task.getPriority()));   
        parentElement.addAttribute("loopStrategy", String.valueOf(task.getLoopStrategy()));
        parentElement.addAttribute("taskInstanceCreator", task.getTaskInstanceCreator());
        parentElement.addAttribute("taskInstanceRunner", task.getTaskInstanceRunner());
        parentElement.addAttribute("taskInstanceCompletionEvaluator", task.getTaskInstanceCompletionEvaluator());
    }

    /**
     * 
     * 功能:序列化Duration<br>
     * 约束:与本函数相关的约束<br>
     * @param duration
     * @param parentElement
     */
    private void writeDuration(Duration duration, Element parentElement) {
        if(ValidateUtil.isNull(duration)){
            return ;
        }
        Element elementDuration = parentElement.addElement(XPDLNames.XPDL_Duration);
        DurationParser parser = new DurationParser();
        parser.doSerialize(duration,elementDuration);  
    }

    /* ----------反 序 列 化 ----------*/
    
    /**
     * 
     * 功能:反序列化Task<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param task
     */
    public void doParser(Element element, Task task,WorkflowProcess process) {
        super.doParse(element, (AbstractWFElement)task, process);
        if (!ValidateUtil.isNull(element.attribute("taskType"))) {
            task.setTaskType(Integer.valueOf(element.attributeValue("taskType")));
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_Duration))) {
            if (ValidateUtil.isNull(task.getDuration())) {
                task.setDuration(new Duration());
            }
            loadDuration(element.element(XPDLNames.XPDL_Duration), task.getDuration());
        }
        if (!ValidateUtil.isNull(element.attributeValue("priority"))) {
            task.setPriority(Integer.valueOf(element.attributeValue("priority")));
        }
        if (!ValidateUtil.isNull(element.attributeValue("loopStrategy"))) {
            task.setLoopStrategy(Integer.valueOf(element.attributeValue("loopStrategy")));
        }
        task.setTaskInstanceCreator(element.attributeValue("taskInstanceCreator"));
        task.setTaskInstanceRunner(element.attributeValue("taskInstanceRunner"));
        task.setTaskInstanceCompletionEvaluator("taskInstanceCompletionEvaluator");
        
    }

    /**
     * 
     * 功能:反序列化Duration<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param duration
     */
    private void loadDuration(Element element, Duration duration) {
        DurationParser parser = new DurationParser();
        parser.doParser(element, duration);
    }

    

}
