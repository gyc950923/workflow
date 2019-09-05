package com.online.workflow.process.parser.netparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Edge;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.parser.AbstractWFElementParser;

public class EdgeParser extends AbstractWFElementParser {

	/**
	 * 功能:序列化Edge
	 * 约束:与本函数相关的约束
	 * @param edge
	 * @param parentElement
	 */
	public void doSerialize(Edge edge, Element parentElement) {
		if (ValidateUtil.isNull(edge)) {
			return;
		}
		super.doSerialize((AbstractWFElement) edge, parentElement);

		parentElement.addAttribute("fromNodeId", edge.getFromNode().getId());
		parentElement.addAttribute("toNodeId", edge.getToNode().getId());

	}

	/**
	 * 
	 * 功能:反序列化Edge
	 * 约束:与本函数相关的约束
	 * 
	 * @param element
	 * @param edge
	 */
	public void doParse(Element element, Edge edge, WorkflowProcess process) {
		super.doParse(element, (AbstractWFElement) edge, process);

		String fromNodeId = element.attributeValue("fromNodeId");
		String toNodeId = element.attributeValue("toNodeId");

		Transition transition = (Transition) edge;
		Node fromNode = (Node) process.findWFElementById(fromNodeId);
		if (fromNode != null) {
			edge.setFromNode(fromNode);
			fromNode.getLeavingTransitions().add(transition);
		}

		Node toNode = (Node) process.findWFElementById(toNodeId);
		if (toNode != null) {
			edge.setToNode(toNode);
			toNode.getEnteringTransitions().add(transition);
		}

	}
}
