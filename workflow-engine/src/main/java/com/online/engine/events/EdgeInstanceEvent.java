package com.online.engine.events;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.enums.EdgeInstanceEventEnum;
import com.online.engine.kernelExtensions.IEdgeInstanceExtension;
import com.online.engine.kernelExtensions.TransitionInstanceExtension;

public class EdgeInstanceEvent implements IEdgeInstanceEvent {

	private Object source;

	private IToken token;

	private Integer eventType;

	public EdgeInstanceEvent(Object source) {
		this.source = source;
		setEventType(EdgeInstanceEventEnum.none);
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
	public Object getSource() {
		return source;
	}

	@Override
	public void fireEvent() {
		IEdgeInstanceExtension extension = new TransitionInstanceExtension();

		if (extension != null) {
			((IRuntimeContextAware) extension)
					.setRuntimeContext(((ProcessInstance) this.token
							.getProcessInstance()).getRuntimeContext());

			extension.onNodeInstanceEventFired(this);
		}

	}

}
