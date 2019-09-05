package com.online.workflow.process.parser.netparser;

import org.dom4j.Element;

import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.ConditionFork;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.Synchronizer;

public class ConditionForkParser extends SynchronizerParser{

    /**
     * 
     * 功能:序列化ConditionFork<br>
     * 约束:与本函数相关的约束<br>
     * @param conditionFork
     * @param parentElement
     */
    public void doSerialize(ConditionFork conditionFork, Element parentElement) {
        super.doSerialize((Synchronizer)conditionFork, parentElement);
        
    }

   /**
    * 功能:反序列化ConditionFork<br>
    * 约束:与本函数相关的约束<br>
    * @param element 参数
    * @param conditionFork 参数1
    * @param process 参数2
    */
    public void doParser(Element element, ConditionFork conditionFork, WorkflowProcess process) {
        super.doParser(element, (Synchronizer)conditionFork, process);
    }

}
