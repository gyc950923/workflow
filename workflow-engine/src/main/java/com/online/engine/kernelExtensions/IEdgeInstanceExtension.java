package com.online.engine.kernelExtensions;

import com.online.engine.events.IEdgeInstanceEvent;

public interface IEdgeInstanceExtension {
	 void onNodeInstanceEventFired(IEdgeInstanceEvent e);
}
