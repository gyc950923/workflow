package com.online.engine.pluginKernel;

import com.online.workflow.process.net.EndNode;

public interface IEndNodeInstance extends ISynchronizerInstance {
	public EndNode getEndNode();

	public void setEndNode(EndNode endNode);
}
