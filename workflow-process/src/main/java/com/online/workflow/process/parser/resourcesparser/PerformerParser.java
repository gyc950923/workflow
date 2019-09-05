package com.online.workflow.process.parser.resourcesparser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.Performer;
import com.online.workflow.process.resources.RestInfo;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.SqlInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.process.resources.XPDLNames;

public class PerformerParser {

    /**
     * 
     * 功能:函数功能的说明<br>
     * 约束:与本函数相关的约束<br>
     * @param performer
     * @param parentElement
     */
    public void doSerialize(Performer performer, Element parentElement) {
        if(ValidateUtil.isNull(performer)){
            return ;
        }
        writeUserInfos(performer.getUserInfos(), parentElement);//按人员
        writeDeptInfos(performer.getDeptInfos(), parentElement);//按部门
        writeRoleInfos(performer.getRoleInfos(), parentElement);//按角色
        writeSqlInfos(performer.getSqlInfos(), parentElement);//按SQL
        writeRestInfos(performer.getRestInfos(), parentElement);//按restful接口
        writeOrgRoleInfos(performer.getOrgRoleInfo(),performer.getOrgRoleInfos(), parentElement);//按restful接口
    }
    
    private void writeOrgRoleInfos(OrgRoleInfo ori, List<OrgRoleInfo> orgRoleInfos,
			Element parentElement) {
        if (ValidateUtil.isNullAndEmpty(orgRoleInfos)) {
            return ;
        }
        
        Element elementRoleInfos = parentElement.addElement(XPDLNames.XPDL_OrgRoleInfos);
        setOrgRoleNodeAttribute(ori, elementRoleInfos);
        
        for(OrgRoleInfo orgRoleInfo : orgRoleInfos){
            Element elementRoleInfo = elementRoleInfos.addElement(XPDLNames.XPDL_OrgRoleInfo);
            OrgRoleInfoParser parser=new OrgRoleInfoParser();
            parser.doSerialize(orgRoleInfo,elementRoleInfo);
        }
        
        
        
        
	}

	private void setOrgRoleNodeAttribute(OrgRoleInfo ori,
			Element elementRoleInfos) {
		elementRoleInfos.addAttribute("departmentSrc", ori.getDepartmentSrc());
        elementRoleInfos.addAttribute("allNode", ori.getAllNode());
        elementRoleInfos.addAttribute("conditionId", ori.getConditionId());
        elementRoleInfos.addAttribute("conditionName", ori.getConditionName());
	}

	private void writeRestInfos(RestInfo restInfo, Element parentElement) {
    	Element elementSqlInfos = parentElement.addElement(XPDLNames.XPDL_RestInfos);
        if (StringUtils.isEmpty(restInfo.getRestKey())) {
            return ;
        }
        Element elementRestInfo = elementSqlInfos.addElement(XPDLNames.XPDL_RestInfo);
        RestInfoParser parser=new RestInfoParser();
        parser.doSerialize(restInfo,elementRestInfo);
	}

	private void writeSqlInfos(SqlInfo sqlInfo, Element parentElement) {
    	 Element elementSqlInfos = parentElement.addElement(XPDLNames.XPDL_SqlInfos);
         if (StringUtils.isEmpty(sqlInfo.getSqlKey())) {
             return ;
         }
         Element elementRoleInfo = elementSqlInfos.addElement(XPDLNames.XPDL_SqlInfo);
         SqlInfoParser parser=new SqlInfoParser();
         parser.doSerialize(sqlInfo,elementRoleInfo);
	}

	/**
     * 
     * 功能:初始化RoleInfos<br>
     * 约束:与本函数相关的约束<br>
     * @param roleInfos
     * @param parentElement
     */
    private void writeRoleInfos(List<RoleInfo> roleInfos, Element parentElement) {
        Element elementRoleInfos = parentElement.addElement(XPDLNames.XPDL_RoleInfos);
        if (ValidateUtil.isNullAndEmpty(roleInfos)) {
            return ;
        }
        for(RoleInfo roleInfo : roleInfos){
            Element elementRoleInfo = elementRoleInfos.addElement(XPDLNames.XPDL_RoleInfo);
            RoleInfoParser parser=new RoleInfoParser();
            parser.doSerialize(roleInfo,elementRoleInfo);
        }    
    }

    /**
     * 
     * 功能:初始化DeptInfos<br>
     * 约束:与本函数相关的约束<br>
     * @param deptInfos
     * @param parentElement
     */
    private void writeDeptInfos(List<DeptInfo> deptInfos, Element parentElement) {
        Element elementDeptInfos = parentElement.addElement(XPDLNames.XPDL_DeptInfos);
        if(ValidateUtil.isNullAndEmpty(deptInfos)){
            return ;
        }
        for(DeptInfo deptInfo : deptInfos){
            Element elementDeptInfo = elementDeptInfos.addElement(XPDLNames.XPDL_DeptInfo);
            DeptInfoParser parser = new DeptInfoParser();
            parser.doSerialize(deptInfo,elementDeptInfo);
        }     
    }

    /**
     * 
     * 功能:序列化UserInfos<br>
     * 约束:与本函数相关的约束<br>
     * @param userInfos
     * @param parentElement
     */
    private void writeUserInfos(List<UserInfo> userInfos, Element parentElement) {
        Element elementUserInfos = parentElement.addElement(XPDLNames.XPDL_UserInfos);
        if(ValidateUtil.isNullAndEmpty(userInfos)){
            return ;
        }
        for(UserInfo userInfo : userInfos){
            Element elementUserInfo = elementUserInfos.addElement(XPDLNames.XPDL_UserInfo);
            UserInfoParser parser = new UserInfoParser();
            parser.doSerialize(userInfo,elementUserInfo);
        } 
    }

    /* ---------- 反 序 列 化 ---------- */
    /**
     * 
     * 功能:反序列化Performer<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param performer
     */
    public void doParser(Element element, Performer performer,WorkflowProcess process) {
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_UserInfos))) {
            if (ValidateUtil.isNull(performer.getUserInfos())) {
                performer.setUserInfos(new ArrayList<UserInfo>());
            }
            loadUserInfos(element.element(XPDLNames.XPDL_UserInfos), performer.getUserInfos());
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_DeptInfos))) {
            if (ValidateUtil.isNull(performer.getDeptInfos())) {
                performer.setDeptInfos(new ArrayList<DeptInfo>());
            }
            loadDeptInfos(element.element(XPDLNames.XPDL_DeptInfos), performer.getDeptInfos());
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_RoleInfos))) {
            if (ValidateUtil.isNull(performer.getRoleInfos())) {
                performer.setRoleInfos(new ArrayList<RoleInfo>());
            }
            loadRoleInfos(element.element(XPDLNames.XPDL_RoleInfos), performer.getRoleInfos());
        }
        
        //机构角色
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_OrgRoleInfos))) {
            if (ValidateUtil.isNull(performer.getOrgRoleInfos())) {
                performer.setOrgRoleInfos(new ArrayList<OrgRoleInfo>());
            }
            loadOrgRoleInfos(element.element(XPDLNames.XPDL_OrgRoleInfos), performer.getOrgRoleInfos(), performer);
        }
        
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_SqlInfos))) {
            if (ValidateUtil.isNull(performer.getSqlInfos())) {
                performer.setSqlInfos(new SqlInfo());
            }
            loadSqlInfos(element.element(XPDLNames.XPDL_SqlInfos), performer.getSqlInfos());
        }
        
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_RestInfos))) {
            if (ValidateUtil.isNull(performer.getRestInfos())) {
                performer.setRestInfos(new RestInfo());
            }
            loadRestInfos(element.element(XPDLNames.XPDL_RestInfos), performer.getRestInfos());
        }
        
    }

    private void loadRestInfos(Element parentElement, RestInfo restInfos) {
    	Element element = parentElement.element(XPDLNames.XPDL_RestInfo);
    	RestInfoParser parser = new RestInfoParser();
        parser.doParser(element, restInfos);
	}

	private void loadSqlInfos(Element parentElement, SqlInfo sqlInfos) {
    	Element element = parentElement.element(XPDLNames.XPDL_SqlInfo);
    	SqlInfoParser parser = new SqlInfoParser();
        parser.doParser(element, sqlInfos);
	}

	/**
     * 
     * 功能:反序列化RoleInfos<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     * @param roleInfos
     */
    private void loadRoleInfos(Element parentElement, List<RoleInfo> roleInfos) {
        List<Element> elements = parentElement.elements(XPDLNames.XPDL_RoleInfo);
        for(Element element : elements){
            RoleInfo roleInfo = new RoleInfo();
            RoleInfoParser parser = new RoleInfoParser();
            parser.doParser(element, roleInfo);
            roleInfos.add(roleInfo);
        }   
    }
    
    /**
     * 
     * 功能:反序列化RoleInfos<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     * @param roleInfos
     */
    @SuppressWarnings("unchecked")
	private void loadOrgRoleInfos(Element parentElement, List<OrgRoleInfo> orgRoleInfos, Performer performer) {
    	setOrgRoleInfoValue(parentElement, performer);
        List<Element> elements = parentElement.elements(XPDLNames.XPDL_OrgRoleInfo);
        for(Element element : elements){
        	OrgRoleInfo orgRoleInfo = new OrgRoleInfo();
            OrgRoleInfoParser parser = new OrgRoleInfoParser();
            parser.doParser(element, orgRoleInfo);
            orgRoleInfos.add(orgRoleInfo);
        }   
    }

	private void setOrgRoleInfoValue(Element parentElement, Performer performer) {
		performer.getOrgRoleInfo().setAllNode(parentElement.attributeValue("allNode"));
		performer.getOrgRoleInfo().setConditionId(parentElement.attributeValue("conditionId"));
		performer.getOrgRoleInfo().setConditionName(parentElement.attributeValue("conditionName"));
		performer.getOrgRoleInfo().setDepartmentSrc(parentElement.attributeValue("departmentSrc"));
	}

    /**
     * 
     * 功能:反序列化DeptInfos<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     * @param deptInfos
     */
    private void loadDeptInfos(Element parentElement, List<DeptInfo> deptInfos) {
        List<Element> elements = parentElement.elements(XPDLNames.XPDL_DeptInfo);
        for(Element element : elements){
            DeptInfo deptInfo = new DeptInfo();
            DeptInfoParser parser = new DeptInfoParser();
            parser.doParser(element, deptInfo);
            deptInfos.add(deptInfo);
        }
    }

    /**
     * 
     * 功能:反序列化UserInfos<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     * @param userInfos
     */
    private void loadUserInfos(Element parentElement, List<UserInfo> userInfos) {
        List<Element> elements = parentElement.elements(XPDLNames.XPDL_UserInfo);
        for(Element element : elements){
            UserInfo userInfo = new UserInfo();
            UserInfoParser parser = new UserInfoParser();
            parser.doParser(element, userInfo);
            userInfos.add(userInfo);
        }  
    }

    

}
