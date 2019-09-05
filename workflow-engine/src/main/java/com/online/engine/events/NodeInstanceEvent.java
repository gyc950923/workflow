package com.online.engine.events;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.enums.NodeInstanceEnum;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.exception.EngineException;
import com.online.engine.kernelExtensions.ActivityInstanceExtension;
import com.online.engine.kernelExtensions.ConditionForkInstanceExtension;
import com.online.engine.kernelExtensions.EndNodeInstanceExtension;
import com.online.engine.kernelExtensions.INodeInstanceExtension;
import com.online.engine.kernelExtensions.StartNodeInstanceExtension;

public class NodeInstanceEvent implements INodeInstanceEvent {

	private Object source;

	private IToken token;

	private Integer eventType;
	private Integer nodeType;

	public NodeInstanceEvent(Object source) {
		this.source = source;
		eventType = NodeInstanceEventEnum.none;
	}

	public Object getSource() {
		return source;
	}

	@Override
	public void fireEvent() throws EngineException {
		INodeInstanceExtension extension = null;

		if (nodeType == NodeInstanceEnum.start.intValue()) {
			extension = new StartNodeInstanceExtension();
		} else if (nodeType == NodeInstanceEnum.end.intValue()) {
			extension = new EndNodeInstanceExtension();
		} else if (nodeType == NodeInstanceEnum.activity.intValue()) {
			extension = new ActivityInstanceExtension();
		} else if (nodeType == NodeInstanceEnum.conditionfork.intValue()) {
			extension = new ConditionForkInstanceExtension();
		}

		if (extension != null) {
			((IRuntimeContextAware) extension)
					.setRuntimeContext(((ProcessInstance) this.token
							.getProcessInstance()).getRuntimeContext());

			extension.onNodeInstanceEventFired(this);
		}

	}

	@Override
	public IToken getToken() {
		return token;
	}

	@Override
	public void setToken(IToken token) {
		this.token = token;

	}

	@Override
	public Integer getEventType() {

		return eventType;
	}

	@Override
	public void setEventType(Integer eventType) {
		this.eventType = eventType;

	}

	@Override
	public Integer getNodeType() {

		return nodeType;
	}

	@Override
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;

	}
}
