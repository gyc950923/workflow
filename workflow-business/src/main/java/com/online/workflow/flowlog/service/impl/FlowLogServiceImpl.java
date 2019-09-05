package com.online.workflow.flowlog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.online.workflow.flowlog.dao.IFlowLogDao;
import com.online.workflow.flowlog.service.IFlowLogService;
import com.online.workflow.util.PageModel;

@Service("flowLogService")
public class FlowLogServiceImpl implements IFlowLogService{

    @Resource(name = "flowLogDao")
    private IFlowLogDao flowLogDao;
    @Override
    public List<Object> submitNotFinish(PageModel page, String userId) {
        return flowLogDao.submitNotFinish(page, userId);
    }
    
    @Override
    public List<Object> submitAndFinish(PageModel page, String userId) {
        return flowLogDao.submitAndFinish(page, userId);
    }

    @Override
    public List<Object> passAndFinish(PageModel page, String userId) {
        return flowLogDao.passAndFinish(page, userId);
    }

    @Override
    public List<Object> passNotFinish(PageModel page, String userId) {
        return flowLogDao.passNotFinish(page, userId);
    }

    @Override
    public List<Object> todoList(PageModel page, String userId) {
        return flowLogDao.todoList(page, userId);
    }

    @Override
    public List<Object> detaiLog(PageModel page, String processinstanceId) {
        return flowLogDao.detaiLog(page,processinstanceId);
    }
    
}
