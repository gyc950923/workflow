package com.online.workflow.flowdef.service.impl;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.online.workflow.flowdef.dao.IFlowDefDao;
import com.online.workflow.flowdef.service.IFlowDefService;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.util.PageModel;

@Service("flowDefService")
public class FlowDefServiceImpl implements IFlowDefService{

    @Resource(name="flowDefDao")
    private IFlowDefDao flowDefDao;

    @Override
    public List<Object> flowDefList(PageModel page) {
        return flowDefDao.flowDefList(page);
    }
    @Override
    public List<Object> getStudents(PageModel page) {
        return flowDefDao.getStudents(page);
    }
    public IFlowDefDao getFlowDefDao() {
        return flowDefDao;
    }

    public void setFlowDefDao(IFlowDefDao flowDefDao) {
        this.flowDefDao = flowDefDao;
    }
	@Override
	public String saveFrom(String name,String age,String days) {
		
	String	id = flowDefDao.saveFrom(name,age,days);
		 return id;
	}
	@Override
	public List<WorkflowDefinition>  saveWorkflow(String flowId) {
		// TODO Auto-generated method stub
		return flowDefDao.saveWorkflow(flowId);
	}
	@Override
	public void saveTodo(WorkflowDefinition workflowDefinition) {
		// TODO Auto-generated method stub
		flowDefDao.saveTodo(workflowDefinition);
		
	}

}
