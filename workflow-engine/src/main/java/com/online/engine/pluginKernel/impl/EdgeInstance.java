package com.online.engine.pluginKernel.impl;

import com.online.engine.Instance.IToken;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IEdgeInstance;
import com.online.engine.pluginKernel.INodeInstance;

public class EdgeInstance implements IEdgeInstance {

	private INodeInstance LeavingNodeInstance;
	private INodeInstance enteringNodeInstance;

	@Override
	public INodeInstance getLeavingNodeInstance() {
		// TODO Auto-generated method stub
		return LeavingNodeInstance;
	}

	@Override
	public void setLeavingNodeInstance(INodeInstance leavingNodeInstance) {
		this.LeavingNodeInstance = leavingNodeInstance;

	}

	@Override
	public INodeInstance getEnteringNodeInstance() {

		return enteringNodeInstance;
	}

	@Override
	public void setEnteringNodeInstance(INodeInstance enteringNodeInstance) {
		this.enteringNodeInstance = enteringNodeInstance;

	}

	@Override
	public Boolean take(IToken token) throws EngineException {
		// 不做任何处理
		return false;
	}

}
