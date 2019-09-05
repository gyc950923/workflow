package com.online.engine.pluginTaskInstanceManger.formTaskInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.common.FreemarkerUtil;
import com.online.engine.common.HttpClientUtil;
import com.online.engine.common.SysConfigUtil;
import com.online.engine.pluginBusiness.IBusinessService;
//import com.online.workflow.business.service.IBusinessService;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceRunner;
import com.online.engine.resource.PromptControlItem;
import com.online.workflow.process.enums.ActorAssignTypeEnum;
import com.online.workflow.process.enums.LoopStrategyEnum;
import com.online.workflow.process.enums.PageActionEnum;
import com.online.workflow.process.enums.TaskTypeEnum;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.Performer;
import com.online.workflow.process.resources.RestInfo;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.SqlInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.process.rules.UserRule;
import com.online.workflow.process.tasks.FormTask;

public class FormTaskInstanceRunner implements ITaskInstanceRunner {

	private IWorkflowSession currentSession;
	private RuntimeContext runtimeContext;
	private IProcessInstance processInstance;
	private ITaskInstance taskInstance;
	private IBusinessService businessService;

	@Override
	public void run(IWorkflowSession workflowSession,
			RuntimeContext runtimeContext, IProcessInstance processInstance,
			ITaskInstance taskInstance) {
		if (taskInstance.getTaskTypeEnum() != TaskTypeEnum.form.intValue()) {
			// TODO 异常
		}
		this.setCurrentSession(currentSession);
		this.runtimeContext = runtimeContext;
		this.processInstance = processInstance;
		this.taskInstance = taskInstance;
		this.businessService = runtimeContext.getBusinessService();

		FormTask formTask = (FormTask) taskInstance.getTask();
		assign(formTask);
	}

	private void assign(FormTask formTask) {
		IPersistenceService persistenceService = runtimeContext.getPersistenceService();
		ITaskInstance theLastCompletedTaskInstance = persistenceService.getLastCompletedTaskInstance(processInstance.getId(), formTask.getId());

		Boolean IsQuery = runtimeContext.getTaskControl().isQuery();
		Boolean IsStartFlow = runtimeContext.getTaskControl().isStartFlow();
		Boolean IsConfirm = false;
		
		HashMap<String, String> userList = new HashMap<String, String>();

		if (theLastCompletedTaskInstance != null
				&& (LoopStrategyEnum.redo.intValue() == formTask.getUserRule()
						.getLoopStrategy())) {
			List<IWorkItemInstance> workItemList = persistenceService
					.getCompletedWorkItemsForTaskInstance(theLastCompletedTaskInstance
							.getId());

			for (IWorkItemInstance item : workItemList) {
				if (!userList.values().contains(item.getActorId())) {
					userList.put(item.getActorId(), item.getActorName());
				}
			}

		} else {
			HashMap<String, String> actors = runtimeContext.getActors();
			if (IsStartFlow) {//首次启动流程
				userList.put(runtimeContext.getUserInfo().getId(),
						runtimeContext.getUserInfo().getName());
			} else if(actors != null && actors.size() > 0){//动态指定了处理人
				userList = actors;
			}else {//按节点上配置的处理人
			    UserRule userRule = formTask.getUserRule();
			    Integer actorAssignType =userRule.getActorAssignType();//人员分配方式
			    if (actorAssignType == ActorAssignTypeEnum.appoint.intValue()) {
			        //按照流程指定的人员分配处理人
			        Performer performer = userRule.getPerformer();

	                getUsersByUserInfos(performer.getUserInfos(), userList);
	                getUsersByDeptInfos(performer.getDeptInfos(), userList);
	                getUsersByRoleInfos(performer.getRoleInfos(), userList);
	                getUsersByOrgRoleInfos(performer.getOrgRoleInfos(), userList, formTask.getUserRule().getPerformer().getOrgRoleInfo());
			    }else if (actorAssignType == ActorAssignTypeEnum.expression.intValue()) {
                    //按照表达式从流程变量中取处理人
			        /*String actorId = String.valueOf(processInstance.getProcessInstanceVariablesByName(userRule.getActorIdKey()));
			        String actorName = String.valueOf(processInstance.getProcessInstanceVariablesByName(userRule.getActorNameKey()));
			        UserInfo userInfo = new UserInfo();
			        userInfo.setId(actorId);
			        userInfo.setName(actorName);
			        addUserList(userList, userInfo);*/
                }else if(actorAssignType == ActorAssignTypeEnum.sqlexpression.intValue()){//按照sql
                	SqlInfo sqlInfo = userRule.getPerformer().getSqlInfos();
                	Long entityId = Long.parseLong(taskInstance.getAliveProcessInstance().getEntityId());
                	sqlInfo.setEntityId(entityId);
                	getSqlConditionUserInfos(sqlInfo, userList);
                }else if(actorAssignType == ActorAssignTypeEnum.restfulexpression.intValue()){//按照远程接口
					UserInfo userInfo = new UserInfo();
					RestInfo restInfo = userRule.getPerformer().getRestInfos();
					Long entityId = Long.parseLong(taskInstance.getAliveProcessInstance().getEntityId());
					restInfo.setEntityId(entityId);
					getRestConditionUserInfos(restInfo, userList);
					addUserList(userList, userInfo);
				}
			    
			}

		}

		if (userList.size() <= 0) {
			// TODO 抛异常
		} else {

			if (runtimeContext.getTaskControl().getPageAction() == PageActionEnum.Advance.intValue()) {
				IsConfirm = formTask.getAdvanceRule().isAdvanceConfirm();
			} else if (runtimeContext.getTaskControl().getPageAction() == PageActionEnum.Back.intValue()) {
				IsConfirm = formTask.getBackRule().isBackConfirm();
			}

			if (runtimeContext.getTaskControl().isStartFlow()) {
				runtimeContext.getTaskInstanceManager()
						.createWorkItem(getCurrentSession(), processInstance,
								taskInstance, formTask, userList);
				
				
				
			} else {
				if (IsQuery && IsConfirm) {
					PromptControlItem item = new PromptControlItem();
					item.setActivityId(taskInstance.getActivityId());
					item.setActivityName(taskInstance.getActivity().getName());
					runtimeContext.getPromptControl().getItems().add(item);
					return;
				} else {
					// TODO 执行
					runtimeContext.getTaskInstanceManager().createWorkItem(
							getCurrentSession(), processInstance, taskInstance,
							formTask, userList);

				}

			}
			
			//代办任务生成前调用短信接口，发送短信通知处理人处理
			if (!IsStartFlow) {
				Activity activity = this.taskInstance.getActivity();
				if (activity.getIsSendMsg()) {
					String msgTemplate = activity.getMsgtTemplate();
					FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
					String msg = freemarkerUtil.getContent(StringUtils.defaultString(msgTemplate), processInstance.getProcessInstanceVariables());
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair(SysConfigUtil.getString("messageParamName"), msg));
					params.add(new BasicNameValuePair(SysConfigUtil.getString("targetParamName"), StringUtils.join(userList.keySet().iterator(), ",")));
					HttpClientUtil.executeCallBack(SysConfigUtil.getString("sendMsgUrl"), params);
				}
			}
		}

	}

	/**
	 * 机构角色
	 * @param orgRoleInfos
	 * @param userList
	 */
	private void getUsersByOrgRoleInfos(List<OrgRoleInfo> orgRoleInfos, HashMap<String, String> userList, OrgRoleInfo orgRoleInfo) {
		
		if(StringUtils.isNotBlank(orgRoleInfo.getAllNode())) {//如果本节点有指定部门，以本节点为准， 本节点没有以公用节点为准
			this.runtimeContext.getWorkflowProcess().setActivitsPublicParam(orgRoleInfo);
		}
		if(StringUtils.isNotBlank(processInstance.getEntityId())) {
			List<UserInfo> users = businessService.getConditionOrgOnRoleUserInfos(orgRoleInfos, processInstance);
			getUsersByUserInfos(users, userList);
		}
	}

	private void getRestConditionUserInfos(RestInfo restInfo,
			HashMap<String, String> userList) {
		List<UserInfo> users = businessService.getRestConditionUserInfos(restInfo);
		getUsersByUserInfos(users, userList);
		
	}

	/**
	 * 
	 * @param sqlInfos
	 * @param userList
	 */
	private void getSqlConditionUserInfos(SqlInfo sqlInfos, HashMap<String, String> userList) {
		List<UserInfo> users = businessService.getSqlConditionUserInfos(sqlInfos);
		getUsersByUserInfos(users, userList);
	}

	private void getUsersByUserInfos(List<UserInfo> userInfos,
			HashMap<String, String> userList) {

		if (userInfos != null) {
			for (UserInfo item : userInfos) {
				addUserList(userList, item);
			}
		}

	}

	private void getUsersByDeptInfos(List<DeptInfo> deptInfos,
			HashMap<String, String> userList) {
		List<UserInfo> users = businessService.getUsersByDepts(deptInfos,
				runtimeContext.getUserInfo().getOrgId());
		getUsersByUserInfos(users, userList);
	}

	private void getUsersByRoleInfos(List<RoleInfo> roleInfos,
			HashMap<String, String> userList) {
		List<UserInfo> users = businessService.getUsersByRoles(roleInfos);
		getUsersByUserInfos(users, userList);
	}

	private void addUserList(HashMap<String, String> userList, UserInfo userInfo) {
		if (!userList.keySet().contains(userInfo.getId())) {
			userList.put(userInfo.getId(), userInfo.getName());
		}
	}

	public IWorkflowSession getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(IWorkflowSession currentSession) {
		this.currentSession = currentSession;
	}

}
