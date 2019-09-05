package com.online.workflow.business.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.online.engine.common.BaseDaoSupport;
import com.online.engine.model.Org;
import com.online.engine.model.User;
import com.online.workflow.business.dao.IBusinessDao;
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

@Repository("businessDao")
@SuppressWarnings("unchecked")
public class BusinessDaoImpl extends BaseDaoSupport implements IBusinessDao{

    private String getPageSql(String sql, PageModel pageModel){
        int startIndexs = (pageModel.getPage() - 1) * pageModel.getRows() +1;
        int startIndex =startIndexs-1;
        int endIndex = pageModel.getPage() * pageModel.getRows();
        StringBuilder pageSql = new StringBuilder();
        pageSql.append(sql);
        pageSql.append("  limit "+startIndex+","+endIndex);
//        pageSql.append("select * from (");
//        pageSql.append("select tt.*,rownum as rowno from (");
//        pageSql.append(sql);
//        pageSql.append(") tt where rownum <= "+endIndex);
//        pageSql.append(") table_alias where table_alias.rowno>="+startIndex);
        return pageSql.toString(); 
    }
    
    private Integer getCount(String sql){
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) from (" + sql +") a");
        SQLQuery sqlQuery = this.createSqlQuery(countSql.toString());
       Object counts = sqlQuery.list().get(0);
       Integer count = Integer.parseInt(counts.toString());
        return count;
    }
    @Override
    public List<User> getAllUsersByPage(UserInfo userInfo, PageModel pageModel) {
        String sql = "select * from sys_user s where 1=1";
        //String sql = "select * from SF_XTYH_T where 1=1";
        if (StringUtils.isNotBlank(userInfo.getName())) {
            sql = sql + " and user_name like '%"+userInfo.getName()+"%'";
            //sql = sql + " and mc like '%"+userInfo.getName()+"%'";
        }
        Integer total = this.getCount(sql);
        pageModel.setTotal(total);
        sql = this.getPageSql(sql, pageModel);
        List<User> list = this.createSqlQuery(sql).addEntity(User.class).list();
        
        
        return list;
    }
    
    @Override
    public List<Org> getAllDeptsByPage(DeptInfo deptInfo, PageModel pageModel) {
        String sql = "select * from sys_org where 1=1";
        if (StringUtils.isNotBlank(deptInfo.getName())) {
            sql = sql + " and departname like '%"+deptInfo.getName()+"%'";
        }
        Integer total = this.getCount(sql);
        pageModel.setTotal(total);
        sql = this.getPageSql(sql, pageModel);
        SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(Org.class);
        return sqlQuery.list();
    }
    @Override
    public List<Role> getAllRolesByPage(RoleInfo roleInfo, PageModel pageModel) {
        String sql = "select * from sys_role where 1=1";
        if (StringUtils.isNotBlank(roleInfo.getName())) {
            sql = sql + " and rolename like '%"+roleInfo.getName()+"%'";
        }
        Integer total = this.getCount(sql);
        pageModel.setTotal(total);
        sql = this.getPageSql(sql, pageModel);
        SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(Role.class);
        return sqlQuery.list();
    }

    @Override
    public List<Org> getOrgTree(TreeNode treeNode) {
        String sql = "select * from sys_org where 1=1";
        SQLQuery sqlQuery = null;
        if (StringUtils.isEmpty(treeNode.getId())) {
            if (StringUtils.isNotEmpty(treeNode.getName())) {
                sql = sql + " and departname like '%" + treeNode.getName() + "%'";
            }else{
                sql = sql + " and parentDepartId is null";
            }
            sqlQuery = this.createSqlQuery(sql).addEntity(Org.class);
        }else{
            sql = sql + " and parentDepartId = :id";
            sqlQuery = this.createSqlQuery(sql).addEntity(Org.class);
            sqlQuery.setParameter("id", Long.parseLong(treeNode.getId()));
        }
        return sqlQuery.list();
    }

    @Override
    public List<Menu> getAllFromsByPage(Form form, PageModel pageModel) {
        String sql = "select * from sys_menu where 1=1";
        if (StringUtils.isNotBlank(form.getName())) {
            sql = sql + " and name like '%"+form.getName()+"%'";
        }
        Integer total = this.getCount(sql);
        pageModel.setTotal(total);
        sql = this.getPageSql(sql, pageModel);
        SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(Menu.class);
        return sqlQuery.list();
    }

    @Override
    public List<DbTable> getAllEntitysByPage(MetaData entity, PageModel pageModel) {
        String sql = "select * from sys_dbtable where 1=1";
        if (StringUtils.isNotBlank(entity.getName())) {
            sql = sql + " and content like '%"+entity.getName()+"%'";
        }
        Integer total = this.getCount(sql);
        pageModel.setTotal(total);
        sql = this.getPageSql(sql, pageModel);
        SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(DbTable.class);
        return sqlQuery.list();
    }

	@Override
	public List<UserInfo> getUsersByRoles(List<RoleInfo> roleInfos) {
		List<UserInfo> userlist = new ArrayList<UserInfo>();
//		List<User> userlist1 = new ArrayList<User>();
//		String roleids= null;
//		for(int i=0;i<roleInfos.size();i++){
//			if(i != roleInfos.size()-1){
//				roleids += roleInfos.get(i)+",";
//			}else{
//				roleids += roleInfos.get(i);
//			}
//		}
//		userlist1 = this.getAllUser(roleids);
//		for(User u:userlist1){
//			UserInfo uf = new UserInfo();
//			uf.setId(uf.getId());
//			uf.setName(u.getUserName());
//			userlist.add(uf);
//		}
		return userlist;
	}
	
//	public List<User> getAllUser(String roleid) {
//	    String sql = "select DISTINCT * from sys_user where id in ( select userid from sys_user_role where roleid in ("+roleid+") )";
//        SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(User.class);
//        return sqlQuery.list();
//    }
    
}
