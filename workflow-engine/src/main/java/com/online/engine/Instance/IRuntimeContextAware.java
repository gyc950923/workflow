package com.online.engine.Instance;

import com.online.engine.Instance.impl.RuntimeContext;


public interface IRuntimeContextAware {

	/**
	 * @param ctx
	 */
	public void setRuntimeContext(RuntimeContext ctx);

	/**
	 * @return
	 */
	public RuntimeContext getRuntimeContext();
}
