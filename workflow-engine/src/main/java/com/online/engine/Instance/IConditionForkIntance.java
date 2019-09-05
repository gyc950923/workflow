package com.online.engine.Instance;

import com.online.engine.exception.EngineException;

public interface IConditionForkIntance {
	 void fire(IToken token) throws EngineException;
}
