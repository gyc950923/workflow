package com.online.workflow.process.parser.netparser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.parser.taskparser.FormTaskParser;
import com.online.workflow.process.parser.taskparser.SubflowTaskParser;
import com.online.workflow.process.parser.taskparser.ToolTaskParser;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.SubflowTask;
import com.online.workflow.process.tasks.Task;
import com.online.workflow.process.tasks.ToolTask;

public class ActivityParser extends NodeParser{
	public void doSerialize(Activity activity, Element parentElement) {
	    if (ValidateUtil.isNull(activity)) {
            return ;
        }
	    super.doSerialize((Node)activity, parentElement);
	    parentElement.addAttribute("assignmentStrategy", activity.getAssignmentStrategyEnum().toString());
	    parentElement.addAttribute("callBackStatus", activity.getCallBackStatus().toString());
	    parentElement.addAttribute("localAddress", activity.getLocalAddress());
	    parentElement.addAttribute("restfulAddress", activity.getRestfulAddress());
	    parentElement.addAttribute("isSendMsg", String.valueOf(activity.getIsSendMsg()));
	    parentElement.addAttribute("msgtTemplate", activity.getMsgtTemplate());
		writeInlineTasks(activity.getInlineTasks(), parentElement);
	}

    private void writeInlineTasks(List<Task> inlineTasks, Element parentElement) {
	    if(ValidateUtil.isNullAndEmpty(inlineTasks)){
            return ;
        }
		Element elementTasks = parentElement.addElement(XPDLNames.XPDL_InlineTasks);
		for (Task task : inlineTasks) {
			Element elementTask;
			if (task instanceof FormTask) {
			    elementTask = elementTasks.addElement(XPDLNames.XPDL_FormTask);
                FormTaskParser parser = new FormTaskParser();
                parser.doSerialize((FormTask)task,elementTask);
			} else if (task instanceof ToolTask) {
			    elementTask = elementTasks.addElement(XPDLNames.XPDL_ToolTask);
                ToolTaskParser parser = new ToolTaskParser();
                parser.doSerialize((ToolTask)task,elementTask);
			} else if (task instanceof SubflowTask) {
			    elementTask = elementTasks.addElement(XPDLNames.XPDL_SubflowTask);
                SubflowTaskParser parser = new SubflowTaskParser();
                parser.doSerialize((SubflowTask)task,elementTask);
			}
		}
	}

	/**
	 * 
	 * 功能:反序列化Activity<br>
	 * 约束:与本函数相关的约束<br>
	 * @param element
	 * @param activity
	 */
	public void doParser(Element element, Activity activity,WorkflowProcess process) {
	    super.doParser(element, (Node)activity, process);
		if (!ValidateUtil.isNull(element.attribute("assignmentStrategyEnum"))) {
		    activity.setAssignmentStrategyEnum(Integer.valueOf(
		                    element.attributeValue("assignmentStrategyEnum")));
        }
		if (!ValidateUtil.isNull(element.attribute("callBackStatus"))) {
		    activity.setCallBackStatus(Integer.valueOf(
		            element.attributeValue("callBackStatus")));
		}
		activity.setLocalAddress(element.attributeValue("localAddress"));
		activity.setRestfulAddress(element.attributeValue("restfulAddress"));
		if(!ValidateUtil.isNull(element.attribute("isSendMsg"))) {
			activity.setIsSendMsg(Boolean.valueOf(element.attributeValue("isSendMsg")));
        }
		activity.setMsgtTemplate(element.attributeValue("msgtTemplate"));
		Element elementInlineTasks = element.element(XPDLNames.XPDL_InlineTasks);
		if (!ValidateUtil.isNull(elementInlineTasks)) {
		    if(ValidateUtil.isNull(activity.getInlineTasks())){
		        activity.setInlineTasks(new ArrayList<Task>());
		    }
		    loadInlineTasks(elementInlineTasks, activity.getInlineTasks(), process);
           
        }
	}

	/**
	 * 
	 * 功能:反序列化InlineTasks<br>
	 * 约束:与本函数相关的约束<br>
	 * @param parentElement
	 * @param inlineTasks
	 */
    private void loadInlineTasks(Element parentElement, List<Task> inlineTasks,WorkflowProcess process) {
        List<Element> elements = parentElement.elements();
        if (ValidateUtil.isNullAndEmpty(elements)) {
            return ;
        }
        for(Element element : elements){
            if (XPDLNames.XPDL_FormTask.equals(element.getName())) {
                FormTask formTask = new FormTask();
                FormTaskParser parser = new FormTaskParser();
                parser.doParser(element, formTask, process);
                inlineTasks.add(formTask);
            }else if (XPDLNames.XPDL_ToolTask.equals(element.getName())) {
                ToolTask toolTask = new ToolTask();
                ToolTaskParser parser = new ToolTaskParser();
                parser.doParser(element, toolTask, process);
                inlineTasks.add(toolTask);
            }else if(XPDLNames.XPDL_SubflowTask.equals(element.getName())){
                SubflowTask subflowTask = new SubflowTask();
                SubflowTaskParser parser = new SubflowTaskParser();
                parser.doParser(element, subflowTask, process);
                inlineTasks.add(subflowTask);
            }
        }
    }
}
