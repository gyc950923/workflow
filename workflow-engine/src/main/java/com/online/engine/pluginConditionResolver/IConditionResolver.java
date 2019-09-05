package com.online.engine.pluginConditionResolver;

import java.util.List;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.pluginKernel.ITransitionInstance;

public interface IConditionResolver extends IRuntimeContextAware{
	Boolean conditionCalc(Object param,String operator,Object result);

    List<ITransitionInstance> conditionFilter(IToken token, List<ITransitionInstance> leavingTransitionInstances);
}
