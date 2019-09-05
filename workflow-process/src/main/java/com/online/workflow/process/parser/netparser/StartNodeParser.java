package com.online.workflow.process.parser.netparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Synchronizer;
import com.online.workflow.process.resources.XPDLNames;

public class StartNodeParser  extends SynchronizerParser{

    /**
     * 功能:序列化开始节点startNode
     * 约束:与本函数相关的约束
     * @param startNode
     * @param parentElement
     */
	public void doSerialize(StartNode startNode, Element parentElement) {
	    if(ValidateUtil.isNull(startNode)){
	        return ;
	    }
	    super.doSerialize((Synchronizer)startNode, parentElement);
	    parentElement.addAttribute("name", startNode.getName());
	    writeEntryNode(startNode.getEntryNode(),parentElement);
	}

	/**
	 * 
	 * 功能:序列化EntryNode
	 * 约束:与本函数相关的约束
	 * @param entryNode
	 * @param parentElement
	 */
	private void writeEntryNode(Node entryNode, Element parentElement) {
	    if (ValidateUtil.isNull(entryNode)) {
            return ;
        }
	    Element elementNode=parentElement.addElement(XPDLNames.XPDL_EntryNode);
        NodeParser parser=new NodeParser();
        parser.doSerialize(entryNode,elementNode);
    }

    /**
	 * 
	 * 功能:反序列化开始节点StartNode
	 * 约束:与本函数相关的约束
	 * @param element
	 * @param startNode
	 */
    public void doParser(Element element, StartNode startNode,WorkflowProcess process) {
        super.doParser(element,(Synchronizer)startNode, process);
        startNode.setName(element.attributeValue("name"));
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_EntryNode))) {
            if (ValidateUtil.isNull(startNode.getEntryNode())) {
                startNode.setEntryNode(new Node());
            }
            loadEntryNode(element.element(XPDLNames.XPDL_EntryNode), startNode.getEntryNode(), process);
        }
    }

    /**
     * 
     * 功能:反序列化EntryNode
     * 约束:与本函数相关的约束
     * @param element
     * @param entryNode
     */
    private void loadEntryNode(Element element, Node entryNode,WorkflowProcess process) {
        NodeParser parser =new NodeParser();
        parser.doParser(element, entryNode, process);
    }

}
