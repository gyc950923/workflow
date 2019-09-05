package com.online.engine.pluginKernel;


import com.online.engine.Instance.IToken;
import com.online.engine.exception.EngineException;
import com.online.workflow.process.net.Activity;

public interface IActivityInstance extends INodeInstance{
	// / <summary>结束活动</summary>
		// / <param name="token"></param>
		// / <param name="targetActivityInstance"></param>
		void complete(IToken token, IActivityInstance targetActivityInstance) throws EngineException;
		Activity getActivity();
}
