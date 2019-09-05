package com.online.workflow.process.parser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.ConditionFork;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Synchronizer;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.parser.formparser.FormParser;
import com.online.workflow.process.parser.netparser.ActivityParser;
import com.online.workflow.process.parser.netparser.ConditionForkParser;
import com.online.workflow.process.parser.netparser.EndNodeParser;
import com.online.workflow.process.parser.netparser.StartNodeParser;
import com.online.workflow.process.parser.netparser.SynchronizerParser;
import com.online.workflow.process.parser.netparser.TransitionParser;
import com.online.workflow.process.parser.resourcesparser.DataFieldParser;
import com.online.workflow.process.parser.rulesparser.StartRuleParser;
import com.online.workflow.process.parser.taskparser.FormTaskParser;
import com.online.workflow.process.parser.taskparser.SubflowTaskParser;
import com.online.workflow.process.parser.taskparser.ToolTaskParser;
import com.online.workflow.process.resources.DataField;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.rules.StartRule;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.SubflowTask;
import com.online.workflow.process.tasks.Task;
import com.online.workflow.process.tasks.ToolTask;

public class WorkflowProcessParser extends AbstractWFElementParser {

	public void DoSerialize(WorkflowProcess process, Element xElement) {
		if (null == process) {
			return;
		}
		super.doSerialize((AbstractWFElement) process, xElement);

		xElement.addAttribute("state", process.getState().toString());
		xElement.addAttribute("entityName", process.getEntityName());
		xElement.addAttribute("entityValue", process.getEntityValue());
		xElement.addAttribute("toDoDescription", process.getToDoDescription());
		xElement.addAttribute("processDescription", process.getProcessDescription());

		writeStartNode(process.getStartNode(), xElement);
		writeEndNodes(process.getEndNodes(), xElement);
		writeDataFields(process.getDataFields(), xElement);
		writeTasks(process.getTasks(), xElement);
		writeActivities(process.getActivitsPublicParam(),process.getActivities(), xElement);
		writeSynchronizers(process.getSynchronizers(), xElement);
		writeStartRule(process.getStartRule(), xElement);
		writeForm(process.getForm(), xElement);
		writeTransitions(process.getTransitions(), xElement);
	}

	/**
	 * 
	 * 功能:序列化Form<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param form
	 * @param parentElement
	 */
	private void writeForm(Form form, Element parentElement) {
		Element element = parentElement.addElement(XPDLNames.XPDL_Form);
		FormParser parser = new FormParser();
		parser.doSerialize(form, element);
	}

	/**
	 * 
	 * 功能:序列化StartRule<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param startRule
	 * @param parentElement
	 */
	private void writeStartRule(StartRule startRule, Element parentElement) {
		Element elementStartRule = parentElement
				.addElement(XPDLNames.XPDL_StartRule);
		StartRuleParser parser = new StartRuleParser();
		parser.doSerialize(startRule, elementStartRule);
	}

	/**
	 * 
	 * 功能:序列化Synchronizers<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param synchronizers
	 * @param xElement
	 */
	private void writeSynchronizers(List<Synchronizer> synchronizers,
			Element parentElement) {
		Element elementSynchronizers = parentElement
				.addElement(XPDLNames.XPDL_Synchronizers);
		if (null == synchronizers) {
			return;
		}
		for (Synchronizer synchronizer : synchronizers) {
		    if (synchronizer instanceof ConditionFork) {
		        Element elementConditionFork = elementSynchronizers
                        .addElement(XPDLNames.XPDL_ConditionFork);
		        ConditionForkParser parser = new ConditionForkParser();
		        parser.doSerialize((ConditionFork)synchronizer, elementConditionFork);
            }else {
                Element elementSynchronizer = elementSynchronizers
                        .addElement(XPDLNames.XPDL_Synchronizer);
                SynchronizerParser parser = new SynchronizerParser();
                parser.doSerialize(synchronizer, elementSynchronizer);
            }
		}
	}

	/**
	 * 
	 * 功能:序列化Transitions<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param transitions
	 * @param parentElement
	 */
	private void writeTransitions(List<Transition> transitions,
			Element parentElement) {
		Element elementTransitions = parentElement
				.addElement(XPDLNames.XPDL_Transitions);
		if (null == transitions) {
			return;
		}
		for (Transition transition : transitions) {
			Element elementTransition = elementTransitions
					.addElement(XPDLNames.XPDL_Transition);
			TransitionParser parser = new TransitionParser();
			parser.doSerialize(transition, elementTransition);
		}
	}

	/**
	 * 
	 * 功能:序列化Tasks<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param tasks
	 * @param parentElement
	 */
	private void writeTasks(List<Task> tasks, Element parentElement) {
		Element elementTasks = parentElement.addElement(XPDLNames.XPDL_Tasks);
		for (Task task : tasks) {
			Element elementTask;
			if (task instanceof FormTask) {
				elementTask = elementTasks.addElement(XPDLNames.XPDL_FormTask);
				FormTaskParser parser = new FormTaskParser();
				parser.doSerialize((FormTask) task, elementTask);
			} else if (task instanceof ToolTask) {
				elementTask = elementTasks.addElement(XPDLNames.XPDL_ToolTask);
				ToolTaskParser parser = new ToolTaskParser();
				parser.doSerialize((ToolTask) task, elementTask);
			} else if (task instanceof SubflowTask) {
				elementTask = elementTasks
						.addElement(XPDLNames.XPDL_SubflowTask);
				SubflowTaskParser parser = new SubflowTaskParser();
				parser.doSerialize((SubflowTask) task, elementTask);
			}
		}
	}

	/**
	 * 
	 * 功能:序列化EndNodes<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param endNodes
	 * @param parentElement
	 */
	private void writeEndNodes(List<EndNode> endNodes, Element parentElement) {
		if (ValidateUtil.isNullAndEmpty(endNodes)) {
			return;
		}
		Element elementEndNodes = parentElement
				.addElement(XPDLNames.XPDL_EndNodes);
		for (EndNode endNode : endNodes) {
			Element elementEndNode = elementEndNodes
					.addElement(XPDLNames.XPDL_EndNode);
			EndNodeParser parser = new EndNodeParser();
			parser.doSerialize(endNode, elementEndNode);
		}
	}

	private void writeActivities(OrgRoleInfo ori, List<Activity> activities, Element parentElement) {
		Element elementActivities = parentElement.addElement(XPDLNames.XPDL_Activitys);
		setActivitysAttribute(ori, elementActivities);
		
		if (null == activities) {
			return;
		}
		for (Activity activity : activities) {
			Element element = elementActivities
					.addElement(XPDLNames.XPDL_Activity);
			ActivityParser parser = new ActivityParser();
			parser.doSerialize(activity, element);
		}
		
	}

	private void setActivitysAttribute(OrgRoleInfo ori, Element elementActivities) {
		elementActivities.addAttribute("departmentSrc", ori.getDepartmentSrc());
        elementActivities.addAttribute("allNode", ori.getAllNode());
        elementActivities.addAttribute("conditionId", ori.getConditionId());
        elementActivities.addAttribute("conditionName", ori.getConditionName());
	}

	private void writeDataFields(List<DataField> dataFields,
			Element parentElement) {

		Element elementFields = parentElement
				.addElement(XPDLNames.XPDL_DataFields);

		for (DataField dataField : dataFields) {
			Element elementDataField = elementFields
					.addElement(XPDLNames.XPDL_DataField);
			DataFieldParser parser = new DataFieldParser();
			parser.doSerialize(dataField, elementDataField);
		}
	}

	private void writeStartNode(StartNode startNode, Element parentElement) {
		if (ValidateUtil.isNull(startNode)) {
			return;
		}
		Element element = parentElement.addElement(XPDLNames.XPDL_StartNode);
		StartNodeParser parser = new StartNodeParser();
		parser.doSerialize(startNode, element);
	}

	/* ---------反 序 列 化 --------- */

	public void DoParse(Element root, WorkflowProcess process) {
		super.doParse(root, (AbstractWFElement) process, process);
		process.setState(Boolean.parseBoolean(root.attributeValue("state")));
		process.setEntityName(root.attributeValue("entityName"));
		process.setEntityValue(root.attributeValue("entityValue"));
		process.setToDoDescription(root.attributeValue("toDoDescription"));
		process.setProcessDescription(root.attributeValue("processDescription"));

		if (null != root.element(XPDLNames.XPDL_StartNode)) {
			if (null == process.getStartNode()) {
				process.setStartNode(new StartNode());
			}
			loadStartNode(root.element(XPDLNames.XPDL_StartNode),
					process.getStartNode(), process);
		}
		if (null != root.element(XPDLNames.XPDL_EndNodes)) {
			if (null == process.getEndNodes()) {
				process.setEndNodes(new ArrayList<EndNode>());
			}
			loadEndNodes(root.element(XPDLNames.XPDL_EndNodes),
					process.getEndNodes(), process);
		}
		if (null != root.element(XPDLNames.XPDL_DataFields)) {
			if (null == process.getDataFields()) {
				process.setDataFields(new ArrayList<DataField>());
			}
			loadDataFields(root.element(XPDLNames.XPDL_DataFields),
					process.getDataFields(), process);
		}
		if (null != root.element(XPDLNames.XPDL_Tasks)) {
			if (null == process.getTasks()) {
				process.setTasks(new ArrayList<Task>());
			}
			loadTasks(root.element(XPDLNames.XPDL_Tasks), process.getTasks(),
					process);
		}
		
		
		if (null != root.element(XPDLNames.XPDL_Activitys)) {
			if (null == process.getActivities()) {
				process.setActivities(new ArrayList<Activity>());
			}
			loadActivities(root.element(XPDLNames.XPDL_Activitys), process.getActivities(), process);
		}
	
		if (null != root.element(XPDLNames.XPDL_Synchronizers)) {
			if (null == process.getSynchronizers()) {
				process.setSynchronizers(new ArrayList<Synchronizer>());
			}
			loadSynchronizers(root.element(XPDLNames.XPDL_Synchronizers),
					process.getSynchronizers(), process);
		}
		if (null != root.element(XPDLNames.XPDL_StartRule)) {
			if (null == process.getStartRule()) {
				process.setStartRule(new StartRule());
			}
			loadStartRule(root.element(XPDLNames.XPDL_StartRule),
					process.getStartRule(), process);
		}
		if (null != root.element(XPDLNames.XPDL_Form)) {
			if (null == process.getForm()) {
				process.setForm(new Form());
			}
			loadForm(root.element(XPDLNames.XPDL_Form), process.getForm(),
					process);
		}
		
		if (null != root.element(XPDLNames.XPDL_Transitions)) {
			if (null == process.getTransitions()) {
				process.setTransitions(new ArrayList<Transition>());
			}
			loadTransitions(root.element(XPDLNames.XPDL_Transitions),
					process.getTransitions(), process);
		}
	}

	/**
	 * 
	 * 功能:反序列化Form<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param element
	 * @param form
	 */
	private void loadForm(Element element, Form form,WorkflowProcess process) {
		FormParser parser = new FormParser();
		parser.doParser(element, form, process);
	}

	/**
	 * 
	 * 功能:反序列化StartRule<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param element
	 * @param startRule
	 */
	private void loadStartRule(Element element, StartRule startRule,WorkflowProcess process) {
		StartRuleParser parser = new StartRuleParser();
		parser.doParser(element, startRule);
	}

	/**
	 * 
	 * 功能:反序列化Synchronizers<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param synchronizers
	 */
	private void loadSynchronizers(Element parentElement,
			List<Synchronizer> synchronizers,WorkflowProcess process) {
		List<Element> elements = parentElement.elements();
		for (Element element : elements) {
		    if (XPDLNames.XPDL_ConditionFork.equals(element.getName())) {
                ConditionFork conditionFork = new ConditionFork();
                ConditionForkParser parser = new ConditionForkParser();
                parser.doParser(element, conditionFork, process);
                synchronizers.add(conditionFork);
            }else{
                Synchronizer synchronizer = new Synchronizer();
                SynchronizerParser parser = new SynchronizerParser();
                parser.doParser(element, synchronizer, process);
                synchronizers.add(synchronizer);
            }
		}
	}

	/**
	 * 
	 * 功能:反序列化Transitions<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param transitions
	 */
	private void loadTransitions(Element parentElement,
			List<Transition> transitions,WorkflowProcess process) {
		List<Element> elements = parentElement
				.elements(XPDLNames.XPDL_Transition);
		for (Element element : elements) {
			Transition transition = new Transition();
			TransitionParser parser = new TransitionParser();
			parser.doParser(element, transition, process);
			transitions.add(transition);
		}
	}

	/**
	 * 
	 * 功能:反序列化Tasks<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param tasks
	 */
	private void loadTasks(Element parentElement, List<Task> tasks,WorkflowProcess process) {
		List<Element> elements = parentElement.elements();
		for (Element element : elements) {
			if (XPDLNames.XPDL_FormTask.equals(element.getName())) {
				FormTask formTask = new FormTask();
				FormTaskParser parser = new FormTaskParser();
				parser.doParser(element, formTask, process);
				tasks.add(formTask);
			} else if (XPDLNames.XPDL_ToolTask.equals(element.getName())) {
				ToolTask toolTask = new ToolTask();
				ToolTaskParser parser = new ToolTaskParser();
				parser.doParser(element, toolTask, process);
				tasks.add(toolTask);
			} else if (XPDLNames.XPDL_SubflowTask.equals(element.getName())) {
				SubflowTask subflowTask = new SubflowTask();
				SubflowTaskParser parser = new SubflowTaskParser();
				parser.doParser(element, subflowTask, process);
				tasks.add(subflowTask);
			}
		}

	}

	/**
	 * 
	 * 功能:反序列化DataFields<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param dataFields
	 */
	private void loadDataFields(Element parentElement,
			List<DataField> dataFields,WorkflowProcess process) {
		List<Element> elements = parentElement
				.elements(XPDLNames.XPDL_DataField);
		for (Element element : elements) {
			DataField dataField = new DataField();
			DataFieldParser parser = new DataFieldParser();
			parser.doParse(element, dataField, process);
		}

	}

	/**
	 * 
	 * 功能:反序列化EndNodes<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param endNodes
	 */
	private void loadEndNodes(Element parentElement, List<EndNode> endNodes,WorkflowProcess process) {
		List<Element> elements = parentElement.elements(XPDLNames.XPDL_EndNode);
		for (Element element : elements) {
			EndNode endNode = new EndNode();
			EndNodeParser parser = new EndNodeParser();
			parser.doParser(element, endNode, process);
			endNodes.add(endNode);
		}
	}

	/**
	 * 
	 * 功能:反序列化StartNode<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param element
	 * @param startNode
	 */
	private void loadStartNode(Element element, StartNode startNode,
			WorkflowProcess process) {
		StartNodeParser parser = new StartNodeParser();
		parser.doParser(element, startNode, process);
	}

	/**
	 * 
	 * 功能:反序列化Activities<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param activities
	 */
	@SuppressWarnings("unchecked")
	private void loadActivities(Element parentElement, List<Activity> activities, WorkflowProcess process) {
		
		setActivitysEntityValue(parentElement, process);
		
		List<Element> elements = parentElement.elements(XPDLNames.XPDL_Activity);
		for (Element element : elements) {
			Activity activity = new Activity();
			ActivityParser parser = new ActivityParser();
			parser.doParser(element, activity, process);
			activities.add(activity);
		}
	}

	private void setActivitysEntityValue(Element parentElement, WorkflowProcess process) {
		process.getActivitsPublicParam().setAllNode(parentElement.attributeValue("allNode"));
		process.getActivitsPublicParam().setConditionId(parentElement.attributeValue("conditionId"));
		process.getActivitsPublicParam().setConditionName(parentElement.attributeValue("conditionName"));
		process.getActivitsPublicParam().setDepartmentSrc(parentElement.attributeValue("departmentSrc"));
	}

}
