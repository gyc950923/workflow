package com.online.engine.pluginKernel;

import com.online.engine.Instance.IRuntimeContextAware;

public interface IKernelManager extends IRuntimeContextAware{
	  INetInstance getNetInstance(String processId, int version);
      void clearNetInstanceMap();
}
