package com.online.workflow.flowlog.service;

import java.util.List;

import com.online.workflow.util.PageModel;

public interface IFlowLogService {

    List<Object> submitNotFinish(PageModel page, String userId);

    List<Object> submitAndFinish(PageModel page, String userId);

    List<Object> passAndFinish(PageModel page, String userId);

    List<Object> passNotFinish(PageModel page, String userId);

    List<Object> todoList(PageModel page, String userId);

    List<Object> detaiLog(PageModel page, String processinstanceId);

}
