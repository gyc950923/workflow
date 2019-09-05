package com.online.workflow.process.parser.netparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.Synchronizer;

public class SynchronizerParser extends NodeParser{

    /**
     * 
     * 功能:序列化Synchronizer
     * 约束:与本函数相关的约束
     * @param synchronizer
     * @param parentElement
     */
    public void doSerialize(Synchronizer synchronizer, Element parentElement) {
        if (ValidateUtil.isNull(synchronizer)) {
            return ;
        }
        super.doSerialize((Node) synchronizer, parentElement);
    }

    /**
     * 功能:反序列化Synchronizer
     * 约束:与本函数相关的约束
     * @param element 参数1
     * @param synchronizer 参数2
     * @param process 参数3
     */
    public void doParser(Element element, Synchronizer synchronizer,WorkflowProcess process) {
        super.doParser(element, (Node)synchronizer, process);
    }

    

}
