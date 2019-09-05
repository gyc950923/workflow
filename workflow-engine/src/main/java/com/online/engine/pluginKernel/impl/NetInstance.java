package com.online.engine.pluginKernel.impl;

import java.util.HashMap;
import java.util.List;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.Token;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginConditionResolver.IConditionResolver;
import com.online.engine.pluginKernel.INetInstance;
import com.online.engine.pluginKernel.INodeInstance;
import com.online.engine.pluginKernel.ITransitionInstance;
import com.online.engine.resource.TokenFrom;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.ConditionFork;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Synchronizer;
import com.online.workflow.process.net.Transition;

public class NetInstance implements INetInstance {

	private HashMap<String, Object> wfElementInstanceMap = new HashMap<String, Object>();
	public StartNodeInstance startNodeInstance;

	public NetInstance(WorkflowProcess process, IConditionResolver conditionResolver) {
		// 开始节点
		StartNode startNode = process.getStartNode();
		startNodeInstance = new StartNodeInstance(startNode);
		wfElementInstanceMap.put(startNode.getId(), startNodeInstance);

		// 活动节点
		List<Activity> activities = process.getActivities();
		for (int i = 0; i < activities.size(); i++) {
			Activity activity = (Activity) activities.get(i);
			ActivityInstance activityInstance = new ActivityInstance(activity, conditionResolver);
			wfElementInstanceMap.put(activity.getId(), activityInstance);
		}

		// 条件分支
		List<Synchronizer> synchronizers = process.getSynchronizers();
		for (int i = 0; i < synchronizers.size(); i++) {
		    if (synchronizers.get(i) instanceof ConditionFork) {
		        ConditionFork conditionFork = (ConditionFork) synchronizers.get(i);
		        ConditionForkIntance conditionForkIntance = new ConditionForkIntance(conditionFork, conditionResolver);
		        wfElementInstanceMap.put(conditionFork.getId(), conditionForkIntance);
            }else{
                Synchronizer synchronizer = (Synchronizer) synchronizers.get(i);
                SynchronizerInstance synchronizerInstance = new SynchronizerInstance(
                        synchronizer);
                wfElementInstanceMap
                        .put(synchronizer.getId(), synchronizerInstance);
            }
		}
		// 结束节点
		List<EndNode> endNodes = process.getEndNodes();
		for (int i = 0; i < endNodes.size(); i++) {
			EndNode endNode = (EndNode) endNodes.get(i);
			EndNodeInstance endNodeInstance = new EndNodeInstance(endNode);
			wfElementInstanceMap.put(endNode.getId(), endNodeInstance);
		}
		// 连接线
		List<Transition> transitions = process.getTransitions();
		for (int i = 0; i < transitions.size(); i++) {
			Transition transition = (Transition) transitions.get(i);
			ITransitionInstance transitionInstance = new TransitionInstance(
					transition);

			String fromNodeId = transition.getFromNode().getId();
			String toNodeId = transition.getToNode().getId();

			INodeInstance enteringNodeInstance = (INodeInstance) getWFElementInstance(fromNodeId);
			INodeInstance leavingNodeInstance = (INodeInstance) getWFElementInstance(toNodeId);

			transitionInstance.setEnteringNodeInstance(enteringNodeInstance);
			enteringNodeInstance
					.addLeavingTransitionInstances(transitionInstance);

			transitionInstance.setLeavingNodeInstance(leavingNodeInstance);
			leavingNodeInstance
					.addEnteringTransitionInstances(transitionInstance);

			wfElementInstanceMap.put(transition.getId(), transitionInstance);
		}

		// 开始节点的入口活动实例
		Activity entryctivity = process.getEntryActivity();
		if (entryctivity != null) {
			startNodeInstance.EntryNodeInstance = (INodeInstance) getWFElementInstance(entryctivity
					.getId());
		}

	}

	@Override
	public void run(IProcessInstance processInstance) throws EngineException {

		if (startNodeInstance == null) {
			// TODO 抛异常
		}

		IToken token = new Token();// 初始化token
		token.setIsAlive(true);// 活动的
		token.setProcessInstance(processInstance);// 对应流程实例
		token.setProcessInstanceId(processInstance.getId());
		token.setStepNumber(0);// 步骤号，开始节点的第一步默认为0
		token.setFromActivityId(TokenFrom.from_StartNode);// 从哪个节点来
															// "from_StartNode"
															// 规定的节点。

		startNodeInstance.fire(token);

	}

	@Override
	public Object getWFElementInstance(String wfElementId) {

		return wfElementInstanceMap.get(wfElementId);
	}

}
