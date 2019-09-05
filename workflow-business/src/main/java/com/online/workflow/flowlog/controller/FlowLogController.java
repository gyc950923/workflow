package com.online.workflow.flowlog.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.workflow.flowlog.service.IFlowLogService;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.rules.UserRule;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.util.DataGridModel;
import com.online.workflow.util.PageModel;

@Controller
@RequestMapping("/flowLog")
public class FlowLogController {

    @Resource(name = "flowLogService")
    private IFlowLogService flowLogService;
    
    @RequestMapping(value = "/todoList")
    @ResponseBody
    public DataGridModel todoList(PageModel page,String userId){
        List<Object> list =flowLogService.todoList(page,userId);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
    
    /**
     * 我提交未结束的审批流程
     */
    @RequestMapping(value = "/submitNotFinish")
    @ResponseBody
    public DataGridModel submitNotFinish(PageModel page,String userId){
        List<Object> list =flowLogService.submitNotFinish(page,userId);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
    
    /**
     * 我提交已结束的审批流程
     */
    @RequestMapping(value = "/submitAndFinish")
    @ResponseBody
    public DataGridModel submitAndFinish(PageModel page,String userId){
        List<Object> list =flowLogService.submitAndFinish(page,userId);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
    
    /**
     * 经过我未结束的审批流程
     */
    @RequestMapping(value = "/passNotFinish")
    @ResponseBody
    public DataGridModel passNotFinish(PageModel page,String userId){
        List<Object> list =flowLogService.passNotFinish(page,userId);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
    /**
     * 经过我已结束的审批流程
     */
    @RequestMapping(value = "/passAndFinish")
    @ResponseBody
    public DataGridModel passAndFinish(PageModel page,String userId){
        List<Object> list =flowLogService.passAndFinish(page,userId);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
    /**
     * 查看流程详细
     */
    @RequestMapping(value = "/detaiLog")
    @ResponseBody
    public DataGridModel detaiLog(PageModel page,String processinstanceId){
        List<Object> list =flowLogService.detaiLog(page,processinstanceId);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
}
