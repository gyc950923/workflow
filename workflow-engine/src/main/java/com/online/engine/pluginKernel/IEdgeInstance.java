package com.online.engine.pluginKernel;

import com.online.engine.Instance.IToken;
import com.online.engine.exception.EngineException;

public interface IEdgeInstance {

	public INodeInstance getLeavingNodeInstance();

	public void setLeavingNodeInstance(INodeInstance leavingNodeInstance);

	public INodeInstance getEnteringNodeInstance();

	public void setEnteringNodeInstance(INodeInstance enteringNodeInstance);

	// / <summary>
	// / 接受一个token，并移交给下一个节点
	// / </summary>
	// / <param name="token"></param>
	// / <returns>返回值是该transition计算出的token的alive值</returns>
	public Boolean take(IToken token) throws EngineException;
}
