package com.online.workflow.design.workflow.service;

import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.ConditionFork;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Transition;

public interface IInitNodeService {

    void startNoneEvent(StartNode iWFElement, WorkflowProcess workflowProcess, String resourceId);

    void userTaskNoneEvent(Activity iWFElement, WorkflowProcess workflowProcess, String resourceId);

    void sequenceFlowEvent(Transition iWFElement, WorkflowProcess workflowProcess, String resourceId,
            String fromResourceId, String toResourceId);

    void endNoneEvent(EndNode iWFElement, WorkflowProcess workflowProcess, String resourceId);

    void conditionGatewayEvent(ConditionFork iWFElement, WorkflowProcess workflowProcess, String resourceId);

}
