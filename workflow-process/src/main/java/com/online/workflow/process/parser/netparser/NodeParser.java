package com.online.workflow.process.parser.netparser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.parser.AbstractWFElementParser;
import com.online.workflow.process.resources.XPDLNames;

public class NodeParser extends AbstractWFElementParser {

	/**
	 * 
	 * 功能:序列化Node<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param node
	 * @param parentElement
	 */
	public void doSerialize(Node node, Element parentElement) {
		if (ValidateUtil.isNull(node)) {
			return;
		}
		super.doSerialize((AbstractWFElement) node, parentElement);
		parentElement.addAttribute("entityDocStatusName",node.getEntityDocStatusName());
		parentElement.addAttribute("entityDocStatus", node.getEntityDocStatus());

	}

	/**
	 * 
	 * 功能:反序列化Node<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param element
	 * @param node
	 */
	public void doParser(Element element, Node node, WorkflowProcess process) {
		super.doParse(element, (AbstractWFElement) node, process);
		node.setEntityDocStatusName(element.attributeValue("entityDocStatusName"));
		node.setEntityDocStatus(element.attributeValue("entityDocStatus"));
		if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_EnteringTransitions))) {
			if (ValidateUtil.isNullAndEmpty(node.getEnteringTransitions())) {
				node.setEnteringTransitions(new ArrayList<Transition>());
			}
			loadEnteringTransitions(
					element.element(XPDLNames.XPDL_EnteringTransitions),
					node.getEnteringTransitions(), process);
		}
		if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_LeavingTransitions))) {
			if (ValidateUtil.isNullAndEmpty(node.getLeavingTransitions())) {
				node.setLeavingTransitions(new ArrayList<Transition>());
			}
			loadLeavingTransitions(
					element.element(XPDLNames.XPDL_LeavingTransitions),
					node.getLeavingTransitions(), process);
		}
	}

	/**
	 * 
	 * 功能:反序列化leavingTransitions<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param leavingTransitions
	 */
	private void loadLeavingTransitions(Element parentElement,
			List<Transition> leavingTransitions, WorkflowProcess process) {
		List<Element> elements = parentElement.elements(XPDLNames.XPDL_Transition);
		for (Element element : elements) {
			Transition transition = new Transition();
			TransitionParser parser = new TransitionParser();
			parser.doParser(element, transition, process);
			leavingTransitions.add(transition);
		}
	}

	/**
	 * 
	 * 功能:反序列化EnteringTransitions<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param parentElement
	 * @param enteringTransitions
	 */
	private void loadEnteringTransitions(Element parentElement,
			List<Transition> enteringTransitions, WorkflowProcess process) {
		List<Element> elements = parentElement.elements(XPDLNames.XPDL_Transition);
		for (Element element : elements) {
			Transition transition = new Transition();
			TransitionParser parser = new TransitionParser();
			parser.doParser(element, transition, process);
			enteringTransitions.add(transition);
		}
	}

}
