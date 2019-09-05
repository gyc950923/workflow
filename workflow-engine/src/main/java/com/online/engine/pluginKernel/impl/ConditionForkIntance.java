package com.online.engine.pluginKernel.impl;

import java.util.List;

import com.online.engine.Instance.IConditionForkIntance;
import com.online.engine.Instance.IToken;
import com.online.engine.enums.NodeInstanceEnum;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.events.NodeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginConditionResolver.IConditionResolver;
import com.online.engine.pluginKernel.ITransitionInstance;
import com.online.workflow.process.net.ConditionFork;

public class ConditionForkIntance extends SynchronizerInstance implements
		IConditionForkIntance {

	private ConditionFork conditionFork;
	private IConditionResolver conditionResolver;
	
	public ConditionForkIntance(ConditionFork conditionFork, IConditionResolver conditionResolver){
	    this.conditionFork = conditionFork;
	    this.conditionResolver = conditionResolver;
	}
	
	@Override
	public String getId() {

		return conditionFork.getId();
	}

	@Override
	public void fire(IToken token) throws EngineException  {
		INodeInstanceEvent nodeInstanceEvent = new NodeInstanceEvent(this);
		nodeInstanceEvent.setToken(token);
		nodeInstanceEvent.setNodeType(NodeInstanceEnum.conditionfork);

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_token_entered);
		nodeInstanceEvent.fireEvent();
		
		/*token.setStepNumber(token.getStepNumber() + 1);
		token.setFromActivityId(this.getId());*/

		List<ITransitionInstance> transitionInstances = conditionResolver
				.conditionFilter(token, getLeavingTransitionInstances());
		if (transitionInstances.isEmpty()) {
			// 当流转的目标节点不存在时，手动报异常并结束操作
		    throw new EngineException("没有找到要流转的下一个节点，请检查流程配置!");
		}
		for (int i = 0; i < transitionInstances.size(); i++) {
			ITransitionInstance transInst = transitionInstances.get(i);
			transInst.take(token.getNextToken());
		}

	}

}
