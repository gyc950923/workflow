package com.online.workflow.process.parser.netparser;

import org.dom4j.Element;

import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.Synchronizer;

public class EndNodeParser extends SynchronizerParser{

    /**
     * 
     * 功能:序列化EndNode<br>
     * 约束:与本函数相关的约束<br>
     * @param endNode
     * @param parentElement
     */
    public void doSerialize(EndNode endNode, Element parentElement) {
        super.doSerialize((Synchronizer)endNode, parentElement); 
    }
    
    /**
     * 
     * 功能:反序列化EndNode<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param endNode
     */
    public void doParser(Element element, EndNode endNode,WorkflowProcess process) {
        super.doParser(element, (Synchronizer)endNode, process);
    }

    

}
