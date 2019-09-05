package com.online.engine.pluginKernel.impl;

import com.online.engine.Instance.IToken;
import com.online.engine.enums.NodeInstanceEnum;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.events.NodeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IEndNodeInstance;
import com.online.workflow.process.net.EndNode;

public class EndNodeInstance extends SynchronizerInstance implements
		IEndNodeInstance {

	private EndNode endNode;

	@Override
	public EndNode getEndNode() {
		return endNode;
	}

	@Override
	public void setEndNode(EndNode endNode) {
		this.endNode = endNode;
	}

	@Override
	public String getId() {

		return endNode.getId();
	}

	public EndNodeInstance(EndNode endNode) {
		this.endNode = endNode;
	}

	@Override
	public void fire(IToken tk) throws EngineException { // tk.NodeId = this.Id;

		INodeInstanceEvent nodeInstanceEvent = new NodeInstanceEvent(this);
		nodeInstanceEvent.setToken(tk);
		nodeInstanceEvent.setNodeType(NodeInstanceEnum.end);

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_token_entered);
		;
		nodeInstanceEvent.fireEvent();

		if (tk.getIsAlive())// 如果token是活动的
		{
			nodeInstanceEvent
					.setEventType(NodeInstanceEventEnum.nodeinstance_fired);
			nodeInstanceEvent.fireEvent();
		}

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_leaving);
		nodeInstanceEvent.fireEvent();

		nodeInstanceEvent
				.setEventType(NodeInstanceEventEnum.nodeinstance_completed);
		nodeInstanceEvent.fireEvent();

	}
}
