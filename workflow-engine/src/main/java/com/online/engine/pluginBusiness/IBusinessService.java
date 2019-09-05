package com.online.engine.pluginBusiness;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.ITodo;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.Instance.impl.Todo;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.EntityDocStatus;
import com.online.workflow.process.resources.MetaData;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.PagerNumber;
import com.online.workflow.process.resources.RestInfo;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.SqlInfo;
import com.online.workflow.process.resources.UserInfo;

public interface IBusinessService extends IRuntimeContextAware {

	List<MetaData> getAllEntityInfos();

	List<MetaData> getRefenceEntityById(String idOrName);

	List<Form> getAllFroms();

	List<EntityDocStatus> getEntityDocStatusValues(String entityId);

	List<UserInfo> getUsersByDepts(List<DeptInfo> deptInfos, String orgId);

	List<UserInfo> getUsersByRoles(List<RoleInfo> roleInfos);

	List<UserInfo> getDeptUsers(String deprtId, String orgId);

	List<DeptInfo> getDeptOrg(UUID orgId);

	List<RoleInfo> getRoles(String name);

	/* List<PostInfo> GetPosts(String name); */

	List<String> getValueByEnttyExpression(HashMap<String, Object> vars,
			String entityColumn);

	HashMap<String, String> ListFieldValue(String orgId, String tableId,
			String dataId, List<String> fieldCodes);

	Boolean updateDocStatus(String entityType, String entityId,
			Integer docStatus);
	
	public List<Todo> queryTodoList(Todo todo);
	public ProcessInstance getProcessInstanceEntity(String id);
	// / <summary>
	// / 获取待办信息
	// / </summary>
	// / <param name="actorId"></param>
	// / <param name="entityType"></param>
	// / <param name="orgId"></param>
	// / <returns></returns>
	List<ITodo> getTodoInstanceByActorId(String actorId, String entityType,
			String orgId);

	// / <summary>
	// / 已完成流程实例
	// / </summary>
	// / <param name="actorId"></param>
	// / <param name="entityType"></param>
	// / <param name="orgId"></param>
	// / <returns></returns>
	List<ITodo> getPassEndProcessInstByActorId(String actorId, String orgId);

	// / <summary>
	// / 获取历史记录
	// / </summary>
	// / <param name="actorId"></param>
	// / <param name="processInstId"></param>
	// / <param name="orgId"></param>
	// / <returns></returns>
	List<ITodo> getWorkflowHistory(String entityType, String entityId,
			String orgId, PagerNumber page);

	List<UserInfo> getSqlConditionUserInfos(SqlInfo sqlInfos);

	List<UserInfo> getRestConditionUserInfos(RestInfo restInfo);

	List<UserInfo> getConditionOrgOnRoleUserInfos(List<OrgRoleInfo> orgRoleInfos, IProcessInstance processInstance);
}
