package com.online.workflow.design.workflow.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.online.workflow.design.workflow.service.IInitNodeService;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.ConditionFork;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Synchronizer;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.tasks.FormTask;

/**
 * 流程节点初始化service
 * @author Administrator
 *
 */
public class InitNodeServiceImpl implements IInitNodeService{

    /**
     * 
     * 功能:初始化开始节点<br>
     * 约束:与本函数相关的约束<br>
     * @param iWFElement
     * @param workflow
     * @param resourceId 
     */
    public void startNoneEvent(StartNode startNode, WorkflowProcess workflow, String resourceId) {
        if(null==startNode){
            startNode=new StartNode();
            startNode.setId(resourceId);
            startNode.setChartId(resourceId);
            startNode.setName("开始节点");
            startNode.setDescription("默认描述");
            workflow.setStartNode(startNode);
        } 
    }

    /**
     * 
     * 功能:初始化用户活动节点<br>
     * 约束:与本函数相关的约束<br>
     * @param iWFElement
     * @param workflow
     * @param resourceId
     */
    public void userTaskNoneEvent(Activity activity, WorkflowProcess workflow, String resourceId) {
        if(null==activity){
            activity=new Activity();
            activity.setId(resourceId);
            activity.setChartId(resourceId);
            activity.setName("");
            activity.setDescription("");
            
            FormTask formTask = new FormTask();
            String id = "sid-"+UUID.randomUUID().toString();//带上sid-前缀，保持与其他节点的id格式一致
            formTask.setId(id);
            formTask.setChartId(id);
            
            activity.getInlineTasks().add(formTask);   
            List<Activity> activityList=workflow.getActivities();
            activityList.add(activity);
            workflow.setActivities(activityList);
        }
    }

    /**
     * 
     * 功能:初始化连接线<br>
     * 约束:与本函数相关的约束<br>
     * @param iWFElement
     * @param workflow
     * @param resourceId
     * @param toResourceId 
     * @param fromResourceId 
     */
    public void sequenceFlowEvent(Transition transition, WorkflowProcess workflow, String resourceId, String fromResourceId, String toResourceId) {
        if(null==transition){
            transition=new Transition();
            transition.setId(resourceId);
            transition.setChartId(resourceId);
            transition.setName("");
            Node fromNode = (Node) workflow.findWFElementByChartId(fromResourceId);
            transition.setFromNode(fromNode);
            Node toNode = (Node) workflow.findWFElementByChartId(toResourceId);
            transition.setToNode(toNode);
            fromNode.getLeavingTransitions().add(transition);
            toNode.getEnteringTransitions().add(transition);
            List<Transition> transitionList=workflow.getTransitions();
            transitionList.add(transition);
            workflow.setTransitions(transitionList);
        }else{
            /*移除旧的连接关系*/
            Node oldFromNode = transition.getFromNode();
            if (oldFromNode != null) {
                removeUnuseTransition(transition,oldFromNode.getLeavingTransitions());
                transition.setFromNode(null);
            } 
            Node oldToNode = transition.getToNode();
            if (oldToNode != null) {
                removeUnuseTransition(transition,oldToNode.getEnteringTransitions());
                transition.setToNode(null);
            }
            /*设置新的连接关系*/
            if (StringUtils.isNotBlank(fromResourceId)) {
                Node fromNode = (Node) workflow.findWFElementByChartId(fromResourceId);
                transition.setFromNode(fromNode);
                fromNode.getLeavingTransitions().add(transition);
            }
            if (StringUtils.isNotBlank(toResourceId)) {
                Node toNode = (Node) workflow.findWFElementByChartId(toResourceId);
                transition.setToNode(toNode);
                toNode.getEnteringTransitions().add(transition);
            }
        }
    }

    public void endNoneEvent(EndNode endNode, WorkflowProcess workflowProcess, String resourceId) {
        if(null==endNode){
            endNode=new EndNode();
            endNode.setId(resourceId);
            endNode.setChartId(resourceId);
            endNode.setName("结束节点");
            endNode.setDescription("默认描述");
            List<EndNode> endNodesList=workflowProcess.getEndNodes();
            endNodesList.add(endNode);
            workflowProcess.setEndNodes(endNodesList);
        }
    }
    private void removeUnuseTransition(Transition transition , List<Transition> transitions){
        for(int i = 0 ; i < transitions.size(); i++){
            if (transitions.get(i).getChartId().equals(transition.getChartId())) {
                transitions.remove(i);
            }
        }
    }

    @Override
    public void conditionGatewayEvent(ConditionFork conditionFork, WorkflowProcess workflowProcess, String resourceId) {
        if (conditionFork == null) {
            conditionFork = new ConditionFork();
            conditionFork.setId(resourceId);
            conditionFork.setChartId(resourceId);
            List<Synchronizer> synchronizerList=workflowProcess.getSynchronizers();
            synchronizerList.add(conditionFork);
            workflowProcess.setSynchronizers(synchronizerList);
        }
    }

}
