package com.online.engine.kernelExtensions;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.events.INodeInstanceEvent;

public class StartNodeInstanceExtension implements INodeInstanceExtension,IRuntimeContextAware{

	private RuntimeContext ctx;

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		return ctx;
	}

	@Override
	public void onNodeInstanceEventFired(INodeInstanceEvent e) {
		 //开始节点，不需要做任何处理！
		
	}

	

}
