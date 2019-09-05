package com.online.workflow.flowdef.dao;

import java.util.List;

import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.util.PageModel;

public interface IFlowDefDao {

    List<Object> flowDefList(PageModel page);

	List<Object> getStudents(PageModel page);

	String saveFrom(String name, String age, String days);

	List<WorkflowDefinition>  saveWorkflow(String flowId);

	void saveTodo(WorkflowDefinition workflowDefinition);


}
