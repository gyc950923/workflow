package com.online.workflow.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.online.engine.Instance.ITodo;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.model.Org;
import com.online.engine.model.User;
import com.online.workflow.business.dao.IBusinessDao;
import com.online.workflow.business.model.DbTable;
import com.online.workflow.business.model.Menu;
import com.online.workflow.business.model.Role;
import com.online.workflow.business.service.IBusinessService;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.EntityDocStatus;
import com.online.workflow.process.resources.MetaData;
import com.online.workflow.process.resources.PagerNumber;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.util.PageModel;

@Service("busiService")
public class BusinessServiceImpl implements IBusinessService{

	private RuntimeContext runtimeContext;
	@Resource(name="businessDao")
	private IBusinessDao businessDao;
	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		runtimeContext=ctx;
		
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		// TODO Auto-generated method stub
		return runtimeContext;
	}

	@Override
	public List<MetaData> getAllEntityInfos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaData> getRefenceEntityById(String idOrName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityDocStatus> getEntityDocStatusValues(String entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> getUsersByDepts(List<DeptInfo> deptInfos, String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> getUsersByRoles(List<RoleInfo> roleInfos) {
		// TODO Auto-generated method stub
		return businessDao.getUsersByRoles(roleInfos);
	}

	@Override
	public List<UserInfo> getDeptUsers(String deprtId, String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeptInfo> getDeptOrg(UUID orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleInfo> getRoles(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getValueByEnttyExpression(HashMap<String, Object> vars,
			String entityColumn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> ListFieldValue(String orgId, String tableId,
			String dataId, List<String> fieldCodes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateDocStatus(String entityType, String entityId,
			Integer docStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ITodo> getTodoInstanceByActorId(String actorId,
			String entityType, String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ITodo> getPassEndProcessInstByActorId(String actorId,
			String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ITodo> getWorkflowHistory(String entityType, String entityId,
			String orgId, PagerNumber page) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public List<DeptInfo> getAllDeptsByPage(DeptInfo deptInfo, PageModel pageModel) {
        List<Org> orgs = businessDao.getAllDeptsByPage(deptInfo, pageModel);
        List<DeptInfo> deptInfos = new ArrayList<DeptInfo>();
        for (Org org : orgs) {
            DeptInfo deptInfoTemp = new DeptInfo();
            deptInfoTemp.setId(String.valueOf(org.getId()));
            deptInfoTemp.setName(org.getDepartName());
            deptInfos.add(deptInfoTemp);
        }
        return deptInfos;
    }

    @Override
    public List<RoleInfo> getAllRolesByPage(RoleInfo roleInfo, PageModel pageModel) {
        List<Role> roles = businessDao.getAllRolesByPage(roleInfo, pageModel);
        List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
        for (Role role : roles) {
            RoleInfo roleInfoTemp = new RoleInfo();
            roleInfoTemp.setId(String.valueOf(role.getId()));
            roleInfoTemp.setName(role.getRoleName());
            roleInfos.add(roleInfoTemp);
        }
        return roleInfos;
    }

    @Override
    public List<UserInfo> getAllUsersByPage(UserInfo userInfo, PageModel pageModel) {
      List<User> users = businessDao.getAllUsersByPage(userInfo, pageModel);
    	
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        for (User user : users) {
            UserInfo userInfoTemp = new UserInfo();
            userInfoTemp.setId(String.valueOf(user.getId()));
            userInfoTemp.setName(user.getUserName());
            userInfos.add(userInfoTemp);
        }
        return userInfos;
    }

    @Override
    public List<Form> getAllFromsByPage(Form form, PageModel pageModel) {
        List<Menu> menus = businessDao.getAllFromsByPage(form,pageModel);
        List<Form> forms = new ArrayList<Form>();
        for(Menu menu : menus){
            Form formTemp = new Form();
            formTemp.setName(menu.getName());
            formTemp.setUrl(menu.getUrl());
            forms.add(formTemp);
        }
        return forms;
    }

    @Override
    public List<MetaData> getAllEntitysByPage(MetaData entity, PageModel pageModel) {
        List<DbTable> tables = businessDao.getAllEntitysByPage(entity,pageModel);
        List<MetaData> entitys = new ArrayList<MetaData>();
        for(DbTable table : tables){
            MetaData entityTemp = new MetaData();
            entityTemp.setName(table.getContent());
            entityTemp.setCode(table.getTableName());
            entitys.add(entityTemp);
        }
        return entitys;
    }

    

}
