package com.online.workflow.design.workflow.service;

import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowProcess;

public interface IDelNodeService {

    /**
     * 
     * 功能:删除开始节点<br>
     * 约束:与本函数相关的约束<br>
     * @param workflowProcess
     */
    void delStartNode(WorkflowProcess workflowProcess);

    /**
     * 
     * 功能:删除用户活动节点<br>
     * 约束:与本函数相关的约束<br>
     * @param workflowProcess
     * @param iWFElement
     */
    void delActivity(WorkflowProcess workflowProcess, IWFElement iWFElement);

    /**
     * 
     * 功能:删除连接线<br>
     * 约束:与本函数相关的约束<br>
     * @param workflowProcess
     * @param iWFElement
     */
    void delTransition(WorkflowProcess workflowProcess, IWFElement iWFElement);

    void delEndNode(WorkflowProcess workflowProcess, IWFElement iWFElement);

    void delConditionGatewayEvent(WorkflowProcess workflowProcess, IWFElement iWFElement);

    

}
