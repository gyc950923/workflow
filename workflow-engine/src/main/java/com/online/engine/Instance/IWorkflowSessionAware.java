package com.online.engine.Instance;

public interface IWorkflowSessionAware {
	 /**
     * 设置当前的IWorkflowSession
     * @return
     */
    public IWorkflowSession getCurrentWorkflowSession();
    
    /**
     * 返回当前的IWorkflowSession
     * @param session
     */
    public void setCurrentWorkflowSession(IWorkflowSession session);
}
