package com.online.engine.events;

import com.online.engine.Instance.IToken;
import com.online.engine.enums.NodeInstanceEnum;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.exception.EngineException;

public interface INodeInstanceEvent {

	public IToken getToken();

	public void setToken(IToken token);

	public Integer getEventType();

	public void setEventType(Integer eventType);

	public Integer getNodeType();

	public void setNodeType(Integer nodeType);

	Object getSource();

	void fireEvent() throws EngineException;
}
