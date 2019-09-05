package com.online.engine.kernelExtensions;

import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.exception.EngineException;

public interface INodeInstanceExtension {

	/*public IToken getToken();

	public void setToken(IToken token);

	public Integer getEventType();

	public void setEventType(Integer eventType);

	public Integer getNodeType();

	public void setNodeType(Integer nodeType);*/

	public void onNodeInstanceEventFired(INodeInstanceEvent e) throws EngineException;
}
