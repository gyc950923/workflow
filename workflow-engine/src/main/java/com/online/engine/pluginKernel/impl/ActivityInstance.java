package com.online.engine.pluginKernel.impl;


import java.util.List;

import com.online.engine.Instance.IToken;
import com.online.engine.enums.NodeInstanceEnum;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.events.NodeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginConditionResolver.IConditionResolver;
import com.online.engine.pluginKernel.IActivityInstance;
import com.online.engine.pluginKernel.ITransitionInstance;
import com.online.workflow.process.net.Activity;

public class ActivityInstance extends NodeInstance implements IActivityInstance {

	private Activity activity;

	private IConditionResolver conditionResolver;
	@Override
	public String getId() {

		return activity.getId();
	}
	@Override
	public Activity getActivity() {
		return activity;
	}

	public ActivityInstance(Activity activity, IConditionResolver conditionResolver) {
		this.activity = activity;
		this.conditionResolver = conditionResolver;
	}

    @Override
	public void fire(IToken token) throws EngineException {

		token.setNodeId(this.getId());

		INodeInstanceEvent nodeInstanceEvent = new NodeInstanceEvent(this);
		nodeInstanceEvent.setToken(token);
		nodeInstanceEvent.setNodeType(NodeInstanceEnum.activity);

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_token_entered);
		nodeInstanceEvent.fireEvent();

		if (token.getIsAlive())// 如果当前token是活动的，那么就创建下一节点的token和taskinstance
		{
			nodeInstanceEvent
					.setEventType(NodeInstanceEventEnum.nodeinstance_fired);
			nodeInstanceEvent.fireEvent();
		} else// 如果token是dead状态，那么就直接结束当前节点。
		{
			try {
                this.complete(token, null);
            } catch (EngineException e) {
                e.printStackTrace();
            }
		}

	}

	@Override
	public void complete(IToken token, IActivityInstance targetActivityInstance) throws EngineException {

		NodeInstanceEvent nodeInstanceEvent = new NodeInstanceEvent(this);
		token.setStepNumber(token.getStepNumber() + 1);
		token.setFromActivityId(this.getId());

		nodeInstanceEvent.setToken(token);
		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_leaving);
		nodeInstanceEvent.setNodeType(NodeInstanceEnum.activity);
		nodeInstanceEvent.fireEvent();

		if (targetActivityInstance != null) {
			targetActivityInstance.fire(token.getNextToken());
		} else {
		    //分支流向过滤
		    List<ITransitionInstance> transitionInstances = conditionResolver.conditionFilter(token,getLeavingTransitionInstances());
		    if (transitionInstances.isEmpty()) {
		        //当流转的目标节点不存在时，手动报异常并结束操作
		        throw new EngineException("没有找到要流转的下一个节点，请检查流程配置!");
            }
			for (int i = 0; i < transitionInstances.size(); i++) {
				ITransitionInstance transInst = transitionInstances.get(i);
				transInst.take(token.getNextToken());
			}
		}

		if (token.getIsAlive()) {
			NodeInstanceEvent neevent = new NodeInstanceEvent(this);
			nodeInstanceEvent.setToken(token);
			nodeInstanceEvent
					.setEventType(NodeInstanceEventEnum.nodeinstance_completed);
			nodeInstanceEvent.fireEvent();
		}

	}

}
