package com.online.engine.pluginKernel;

import com.online.engine.Instance.IToken;
import com.online.engine.exception.EngineException;

public interface INodeInstance {

	public String getId();

	public void addEnteringTransitionInstances(
			ITransitionInstance transitionInstance);

	public void addLeavingTransitionInstances(
			ITransitionInstance transitionInstance);

	public void fire(IToken token) throws EngineException ;
}
