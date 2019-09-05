package com.online.engine.events;

import com.online.engine.Instance.IToken;

public interface IEdgeInstanceEvent {
	public IToken getToken();

	public void setToken(IToken token);

	public Integer getEventType();

	public void setEventType(Integer eventType);

	public Object getSource();

	public void fireEvent();
}
