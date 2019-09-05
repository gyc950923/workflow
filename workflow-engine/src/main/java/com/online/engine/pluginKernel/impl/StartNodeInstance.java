package com.online.engine.pluginKernel.impl;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.Token;
import com.online.engine.enums.NodeInstanceEnum;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.events.NodeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.INodeInstance;
import com.online.engine.pluginKernel.ITransitionInstance;
import com.online.workflow.process.net.StartNode;

public class StartNodeInstance extends SynchronizerInstance {

	private StartNode startNode;

	public INodeInstance EntryNodeInstance;

	public StartNodeInstance(StartNode startNode) {

		this.startNode = startNode;
	}

	@Override
	public String getId() {

		return startNode.getId();
	}

	@Override
	public void fire(IToken token) throws EngineException {

		if (!token.getIsAlive())// 如果token是false，那么直接返回
		{
			return;
		}

		IProcessInstance processInstance = token.getProcessInstance();

		INodeInstanceEvent nodeInstanceEvent = new NodeInstanceEvent(this);
		nodeInstanceEvent.setToken(token);
		nodeInstanceEvent.setNodeType(NodeInstanceEnum.start);

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_token_entered);
		nodeInstanceEvent.fireEvent();

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_fired);
		nodeInstanceEvent.fireEvent();

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_leaving);
		nodeInstanceEvent.fireEvent();

		IToken newToken = new Token(); // 产生新的token
		newToken.setIsAlive(true);
		newToken.setProcessInstance(processInstance);
		newToken.setProcessInstanceId(processInstance.getId());
		newToken.setFromActivityId(token.getFromActivityId());
		newToken.setStepNumber(token.getStepNumber() + 1);// 步骤号+1

		if (EntryNodeInstance != null) {
			ITransitionInstance transInst = getOutTransition();
			if (transInst != null) {
				transInst.take(token);
			} else {

				EntryNodeInstance.fire(newToken);
			}
		} else {

			for (int i = 0; getLeavingTransitionInstances() != null
					&& i < getLeavingTransitionInstances().size(); i++) {
				ITransitionInstance transitionInstance = getLeavingTransitionInstances()
						.get(i);
				transitionInstance.take(token);
			}

		}

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_completed);
		nodeInstanceEvent.fireEvent();

	}

	private ITransitionInstance getOutTransition() {

		ITransitionInstance reslut = null;
		if (EntryNodeInstance != null) {
			for (ITransitionInstance modelITransitionInstance : getLeavingTransitionInstances()) {
				TransitionInstance transitionInstance = (TransitionInstance) modelITransitionInstance;
				if (modelITransitionInstance.getLeavingNodeInstance().getId() == EntryNodeInstance
						.getId()) {
					reslut = modelITransitionInstance;
					break;
				}
			}
		}
		return reslut;

	}
}
