package com.online.engine.pluginKernel;

import com.online.workflow.process.net.Transition;

public interface ITransitionInstance extends IEdgeInstance {
	
	Transition getTransition();
	void setTransition(Transition transition);

}
