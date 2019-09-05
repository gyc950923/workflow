package com.online.workflow.business.dao;

import java.util.List;

import com.online.engine.model.Org;
import com.online.engine.model.User;
import com.online.workflow.business.model.DbTable;
import com.online.workflow.business.model.Menu;
import com.online.workflow.business.model.Role;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.MetaData;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.util.PageModel;
import com.online.workflow.util.TreeNode;

public interface IBusinessDao {

    List<Menu> getAllFromsByPage(Form form, PageModel pageModel);
    
    List<User> getAllUsersByPage(UserInfo userInfo, PageModel pageModel);

    List<Org> getAllDeptsByPage(DeptInfo deptInfo, PageModel pageModel);

    List<Role> getAllRolesByPage(RoleInfo roleInfo, PageModel pageModel);

    List<Org> getOrgTree(TreeNode treeNode);

    List<DbTable> getAllEntitysByPage(MetaData entity, PageModel pageModel);
    
    List<UserInfo> getUsersByRoles(List<RoleInfo> roleInfos);

}
