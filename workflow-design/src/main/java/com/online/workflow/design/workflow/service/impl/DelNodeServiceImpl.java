package com.online.workflow.design.workflow.service.impl;

import java.util.List;

import com.online.workflow.design.workflow.service.IDelNodeService;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.ConditionFork;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.Synchronizer;
import com.online.workflow.process.net.Transition;

public class DelNodeServiceImpl implements IDelNodeService{

    @Override
    public void delStartNode(WorkflowProcess workflowProcess) {
        workflowProcess.setStartNode(null);   
    }

    @Override
    public void delActivity(WorkflowProcess workflowProcess, IWFElement iWFElement) {
        List<Activity> activitys = workflowProcess.getActivities();
        for (int i = 0; i < activitys.size(); i++) {
            Activity activity = activitys.get(i);
            if (iWFElement.getChartId().equals(activity.getChartId())) {
                activitys.remove(i);
                break;
            }
        }
    }

    @Override
    public void delTransition(WorkflowProcess workflowProcess, IWFElement iWFElement) {
        List<Transition> transitions = workflowProcess.getTransitions();
        for (int i = 0; i < transitions.size(); i++) {
            Transition transition = transitions.get(i);
            if (iWFElement.getChartId().equals(transition.getChartId())) {
                Node fromNode = transition.getFromNode();
                if (fromNode != null) {
                    fromNode.getLeavingTransitions().remove(transition);
                }
                Node toNode = transition.getToNode();
                if (toNode != null) {
                    toNode.getEnteringTransitions().remove(transition);
                }
                transitions.remove(i);
                break;
            }
        }
    }

    @Override
    public void delEndNode(WorkflowProcess workflowProcess, IWFElement iWFElement) {
        List<EndNode> endNodes = workflowProcess.getEndNodes();
        for (int i = 0; i < endNodes.size(); i++) {
            EndNode endNode = endNodes.get(i);
            if (iWFElement.getChartId().equals(endNode.getChartId())) {
                endNodes.remove(i);
                break;
            }
        }
    }

    @Override
    public void delConditionGatewayEvent(WorkflowProcess workflowProcess, IWFElement iWFElement) {
        List<Synchronizer> synchronizers = workflowProcess.getSynchronizers();
        for(int i = 0; i < synchronizers.size(); i++){
            Synchronizer synchronizer = synchronizers.get(i);
            if (iWFElement.getChartId().equals(synchronizer.getChartId())) {
                synchronizers.remove(i);
            }
        }
    } 

}
