package com.online.workflow.executeflow.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.online.engine.Instance.impl.Todo;
import com.online.engine.model.User;
import com.online.workflow.executeflow.dao.IWorkFlowNodeNamesDao;
import com.online.workflow.executeflow.service.IWorkFlowNodeNamesService;
import com.online.workflow.process.WorkflowDefinitionInfo;
import com.online.workflow.process.enums.ActorAssignTypeEnum;
import com.online.workflow.process.enums.TransitionStartEnum;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.util.XmlUtil;

@Service("workFlowNodeNamesService")
public class WorkFlowNodeNamesServiceImpl implements IWorkFlowNodeNamesService {

	@Resource(name = "workFlowNodeNamesDao")
	private IWorkFlowNodeNamesDao workFlowNodeNamesDao;
	
	private OrgRoleInfo orgRoleInfo = new OrgRoleInfo();

	protected Element root;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAuditNames(String sid, String processid, String entityId, String entityName) {
		WorkflowDefinitionInfo wdi = getWorkflowDefinitionInfo(processid);
		List<User> userList = new ArrayList<User>();
		Map<String, Object> mapJson = new HashMap<String, Object>();
		String flag = "1"; //0:没有工作流下一步审批人 1:正常活动节点  2：审批流是最后一个节点，没有下一步审批人
		
		if (StringUtils.isBlank(wdi.getProcessContent())){
			mapJson.put("success", "0");
			mapJson.put("data", userList);
			mapJson.put("nodeName", "未找到下一审批节点");
			mapJson.put("auditNames","<font style='color:red'>未找到下一节点审批人</font>");
			return mapJson;
		}
		
		
		try {
			Document doc = DocumentHelper.parseText(wdi.getProcessContent());
			root = doc.getRootElement();
			if(StringUtils.isBlank(sid)) {//判断是否是首节点
				Element elementStart = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_StartNode);
				sid = elementStart.attributeValue("id");
				Element elementFirst = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Transitions+"//"+XPDLNames.XPDL_Transition+"[@fromNodeId='"+sid+"']");
				sid = elementFirst.attributeValue("toNodeId");
			}

			List<Node> nodeList = root.selectNodes("//" + XPDLNames.XPDL_Transitions + "//" + XPDLNames.XPDL_Transition + "[@fromNodeId='" + sid + "']");
			List<Element> list=new ArrayList<Element>();
			for(int i=0;i<nodeList.size();i++){
				Element element =(Element) nodeList.get(i);
						list.add(element); //强制转换
					}
			for (Element o : list) {
				Transition t = (Transition) XmlUtil.xmlStrToBean(o, Transition.class);
				Element et = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_EndNodes+"//"+XPDLNames.XPDL_EndNode+"[@id='" + t.getToNodeId() + "']");
				
				if(null != et && et.getName().equals(XPDLNames.XPDL_EndNode)){//判断是否结束节点
					flag = "2";
					mapJson.put("nodeName", et.attributeValue("name"));
					mapJson.put("auditNames", "结束");
					mapJson.put("activityId", "0");
					break;
				}
				searchActivityNode(processid, entityId, entityName,userList, mapJson, t);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == mapJson.get("success")){
			mapJson.put("success", flag);
		}
		
		mapJson.put("data", userList);
		
		return mapJson;
	}

	@SuppressWarnings("unchecked")
	private void searchActivityNode(String processid, String entityId, String entityName, List<User> userList, Map<String, Object> mapJson, Transition t) throws Exception {
		String toNodeId = t.getToNodeId();
		Element eName = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Activitys+"//"+XPDLNames.XPDL_Activity+"[@id='" + t.getToNodeId() + "']//"+XPDLNames.XPDL_InlineTasks+"//"+XPDLNames.XPDL_FormTask);
		if(null == eName){
            List<Node> nodes = root.selectNodes("//" + XPDLNames.XPDL_Transitions + "//" + XPDLNames.XPDL_Transition + "[@fromNodeId='" + t.getToNodeId() + "']");
            List<Element> list=new ArrayList<>();
            for(int i=0;i<nodes.size();i++){
                Element element =(Element) nodes.get(i);
                list.add(element); //强制转换
                    }
            for(Element e:list) {
				Transition transition = (Transition) XmlUtil.xmlStrToBean(e, Transition.class);
				if(StringUtils.isNotBlank(transition.getSqlCondition()) && sqlConditionFilter(transition, entityId)) {
					toNodeId = transition.getToNodeId();
					break;
				}
			}
		}
		
		
		Element et = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_EndNodes+"//"+XPDLNames.XPDL_EndNode+"[@id='" + toNodeId + "']");
		
		if(null != et && et.getName().equals(XPDLNames.XPDL_EndNode)){//判断是否结束节点
			mapJson.put("nodeName", et.attributeValue("name"));
			mapJson.put("auditNames", "结束");
			mapJson.put("success", 2);
			mapJson.put("activityId", "0");
			return;
		}
		
		mapJson.put("activityId", toNodeId);
		getAtivityNode(toNodeId, userList, processid, entityId, entityName, mapJson);
	}

	private WorkflowDefinitionInfo getWorkflowDefinitionInfo(String processid) {
		WorkflowDefinitionInfo wdi = workFlowNodeNamesDao.getAuditNames(processid);//获取原审批流程XML
		return wdi;
	}

	/**
	 * 2.
	 * 
	 * @param condition
	 * @param operator
	 * @param result
	 * @return
	 */
	public Boolean conditionCalc(Object condition, String operator,
			Object result) {
		// boolean result = ;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param_1", condition);
		map.put("param_2", result);
		return (Boolean) AviatorEvaluator.execute("param_1" + operator + "param_2", map);

	}

	/**
	 * 3. 获取下节点审批人员列表
	 * 
	 * @param id
	 * @return
	 */
	private List<User> getAtivityNode(String id, List<User> userList, String processid, String entityId, String entityName, Map<String, Object> mapJson) {
		
		Element eName = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Activitys+"//"+XPDLNames.XPDL_Activity+"[@id='" + id + "']//"+XPDLNames.XPDL_InlineTasks+"//"+XPDLNames.XPDL_FormTask);
		if(null != eName){
			Element name = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Activitys+"//"+XPDLNames.XPDL_Activity+"[@id='" + id + "']");
			mapJson.put("nodeName", name.attributeValue("name"));
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == eName) {
			initMap2UserList(map, userList, mapJson);
			return userList;
		}
		
		Element et = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Activitys+"//"+XPDLNames.XPDL_Activity+"[@id='" + id + "']//"+XPDLNames.XPDL_InlineTasks+"//"+XPDLNames.XPDL_FormTask+"//"+XPDLNames.XPDL_UserRule);
		if(null == et) {
			initMap2UserList(map, userList, mapJson);
			return userList;
		}
		
		Integer activiteState = Integer.parseInt(et.attributeValue("actorAssignType"));
		Element element = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Activitys+"//"+XPDLNames.XPDL_Activity+"[@id='" + id + "']//"+XPDLNames.XPDL_InlineTasks+"//"+XPDLNames.XPDL_FormTask+"//"+XPDLNames.XPDL_UserRule+"//"+XPDLNames.XPDL_Performer);
		if(activiteState == ActorAssignTypeEnum.appoint.intValue()) {//按人员
			getUserInfos(element,map);//获取指定审批人员
			getUsersByRoleInfos(element,map);//获取所选角色下的审批人员
			setOrgRolePublicParam(root, element);//设置部门+角色公用参数
			getUsersByOrgRoleInfos(element,map, entityId, entityName);//获取机构+角色下的审批人员
			mapJson.put("auditUsers", map);
		}else if(activiteState == ActorAssignTypeEnum.sqlexpression.intValue()) {//按配置SQL
			
			Element elementSql = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Activitys+"//"+XPDLNames.XPDL_Activity+"[@id='" + id + "']//"+XPDLNames.XPDL_InlineTasks+"//"+XPDLNames.XPDL_FormTask+"//"+XPDLNames.XPDL_UserRule+"//"+XPDLNames.XPDL_Performer+"//"+XPDLNames.XPDL_SqlInfos+"//"+XPDLNames.XPDL_SqlInfo);
			if(null != elementSql && StringUtils.isNotBlank(elementSql.attributeValue("sqlKey"))) {
				getSqlConditionUserInfos(map, entityId, elementSql.attributeValue("sqlKey"));
			}
		}
		//是否人工干预
		mapJson.put("ismanualintervention", true);
		
		initMap2UserList(map, userList, mapJson);
		
		return userList;
	}
	
	/**
	 * 设置部门+角色 部门来源公用信息
	 * @param element
	 */
	private void setOrgRolePublicParam(Element eRoot, Element element) {
		orgRoleInfo = new OrgRoleInfo();
		Element eOrpp = (Element) element.selectSingleNode("//"+XPDLNames.XPDL_OrgRoleInfos);
		if(null != eOrpp && StringUtils.isNotBlank(eOrpp.attributeValue("departmentSrc"))){
			setOrgRoleInfoProperties(eOrpp);
		}else{
			Element r = (Element) eRoot.selectSingleNode("//"+XPDLNames.XPDL_Activitys);
			if(null != r && StringUtils.isNotBlank(r.attributeValue("departmentSrc"))){
				setOrgRoleInfoProperties(r);
			}
		}
	}

	/**
	 * 设置机构+角色公用属性值
	 * @param eOrpp
	 */
	private void setOrgRoleInfoProperties(Element eOrpp) {
		orgRoleInfo.setAllNode(eOrpp.attributeValue("allNode"));
		orgRoleInfo.setConditionId(eOrpp.attributeValue("conditionId"));
		orgRoleInfo.setConditionName(eOrpp.attributeValue("conditionName"));
		orgRoleInfo.setDepartmentSrc(eOrpp.attributeValue("departmentSrc"));
	}

	private void getSqlConditionUserInfos(Map<String, Object> map, String entityId, String sqlKey) {
		
		List<User> list = workFlowNodeNamesDao.getSqlConditionUserInfos(entityId, sqlKey);
		setMapUser(map, list);
	}

	private void initMap2UserList(Map<String, Object> map, List<User> userList, Map<String, Object> mapJson) {
		List<String> l = new ArrayList<String>();
		Set<String> keys = map.keySet();
		for(String key:keys) {
			l.add(String.valueOf(map.get(key)));
		}
		if(l.size() == 0) {
			mapJson.put("auditNames", "<span style='color:red'>未找到下一步处理人</span>");
			mapJson.put("success", "0");
			mapJson.put("auditUsers", map);
		}else{
			mapJson.put("auditNames", String.join(",", l));
			mapJson.put("auditUsers", map);
		}
	}

	/**
	 * huoqu 机构+角色审批节点下的人员
	 * @param element
	 * @param map
	 */
	private void getUsersByOrgRoleInfos(Element element, Map<String, Object> map, String entityId, String entityName) {
		try{
			List<Element> listElement = getAuditXmlList(element, "OrgRoleInfos");
			if(listElement.size() > 0) {
				List<User> list = workFlowNodeNamesDao.getUsersByOrgRoleInfos(getAuditIds(listElement), entityId, entityName, getOrgRoleInfo());
				setMapUser(map, list);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所选角色下人员信息列表
	 * @param element
	 */
	private void getUsersByRoleInfos(Element element, Map<String, Object> map) {
		try{
			List<Element> listElement = getAuditXmlList(element, "RoleInfos");
			if(listElement.size() > 0) {
				List<User> list = workFlowNodeNamesDao.getUsersByRoleInfos(getAuditIds(listElement));
				setMapUser(map, list);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取审批人员信息
	 * @param map
	 * @param list
	 */
	private void setMapUser(Map<String, Object> map, List<User> list) {
		if(null != list){
			for (User u:list) {
				map.put(String.valueOf(u.getId()), u.getUserName());
			}
		}
	}

	/**
	 * 将审批人以逗号分隔返回
	 * @param list
	 * @return
	 */
	private String getAuditIds(List<Element> list) {
		List<String> l = new ArrayList<String>();
		for(Element e:list){
			l.add(e.attributeValue("id"));
		}
		return String.join(",", l);
	}

	/**
	 * 获取所选指定人员信息
	 * @param element
	 */
	private void getUserInfos(Element element, Map<String, Object> map) {
		try{
			List<Element> list = getAuditXmlList(element, "UserInfos");
			setMapAuditUser(map, list);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将审批人存储到Map中
	 * @param map
	 * @param list
	 */
	private void setMapAuditUser(Map<String, Object> map, List<Element> list) {
		if(null != list) {
			for (Element e:list) {
				map.put(e.attributeValue("id"), e.attributeValue("name"));
			}
		}
	}

	/**
	 * 解析审批节点相应数据
	 * @param element
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private List<Element> getAuditXmlList(Element element, String nodeName)
			throws DocumentException {
		Document document = DocumentHelper.parseText(element.asXML());
		Element root = (Element) document.getRootElement();
		Element userinfos = (Element) root.selectSingleNode("//"+nodeName); 
		List<Element> list = new ArrayList<Element>();
		if(null != userinfos){
			list = userinfos.elements();
		}
		return list;
	}

	public IWorkFlowNodeNamesDao getWorkFlowNodeNamesDao() {
		return workFlowNodeNamesDao;
	}

	public void setWorkFlowNodeNamesDao(
			IWorkFlowNodeNamesDao workFlowNodeNamesDao) {
		this.workFlowNodeNamesDao = workFlowNodeNamesDao;
	}

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getPageActionButton(String sid, String processid, String entityId) {
		WorkflowDefinitionInfo wdi = getWorkflowDefinitionInfo(processid);
		if (null == wdi || StringUtils.isBlank(wdi.getProcessContent())){
			return new ArrayList<Map<String, Object>>();
		}
		
		List<Element> listElement = new ArrayList<Element>();
		try {
			Document doc = DocumentHelper.parseText(wdi.getProcessContent());
			Element root = doc.getRootElement();
			Element element = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Transitions+"//"+XPDLNames.XPDL_Transition+"[@toNodeId='"+sid+"']");
			if(null != element) {
				Element pageactionElement = (Element) element.selectSingleNode(XPDLNames.XPDL_PageActions);
				if(null == pageactionElement) {
					element = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Transitions+"//"+XPDLNames.XPDL_Transition+"[@fromNodeId='"+sid+"']");
					if(null != element){
						element = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Transitions+"//"+XPDLNames.XPDL_Transition+"[@fromNodeId='"+element.attributeValue("toNodeId")+"']");
					}
				}
				
				Element el = null;
				if(null != element ) {
					Transition transition = (Transition) XmlUtil.xmlStrToBean(element, Transition.class);
		            if (transition.getStart() == TransitionStartEnum.none.intValue()) {
		            	el = (Element) element.selectSingleNode(XPDLNames.XPDL_PageActions);
		            }else if (transition.getStart() == TransitionStartEnum.sqlCondition.intValue()) {
		                if(this.sqlConditionFilter(transition, entityId)){
		                	el = (Element) element.selectSingleNode(XPDLNames.XPDL_PageActions);
		                }
		            }else if (transition.getStart() == TransitionStartEnum.methodCondition.intValue()){//
		                if(this.methodConditionFilter(transition, entityId)){
		                	el = (Element) element.selectSingleNode(XPDLNames.XPDL_PageActions);
		                }
		            }else if (transition.getStart() == TransitionStartEnum.varCondition.intValue()) {//暂时不支持
		                if(this.varConditionFilter(transition, entityId)){
		                	el = (Element) element.selectSingleNode(XPDLNames.XPDL_PageActions);
		                }
		            }
		            
		            if(null != el) {
	            		listElement = el.elements();
	            	}
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return getButtonList(listElement);
	}

	private List<Map<String, Object>> getButtonList(List<Element> listElement) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Element e:listElement) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("buttonName", e.attributeValue("buttonName"));
			m.put("methodName", e.attributeValue("methodName"));
			list.add(m);
		}
		return list;
	}
	
	/**
	 * 
	 * 功能:sql条件<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @return
	 */
	private boolean sqlConditionFilter(Transition e, String entityId) {
		boolean flag = false;
		Object param = workFlowNodeNamesDao.getSqlConditionValue(e.getSqlCondition(), entityId);
		if(null != param){
			Object result = null;
			if (param instanceof BigDecimal) {
				param = ((BigDecimal) param).doubleValue();
				result = new BigDecimal(e.getSqlResult()).doubleValue();
			} else {
				param = String.valueOf(param).toLowerCase();
				result = e.getSqlResult().toLowerCase();
			}
			flag = conditionCalc(param, e.getSqlOperator(), result);
		}
		return flag;
	}
	
	/**
     * 
     * 功能:自定义方法<br>
     * 约束:与本函数相关的约束<br>
     * @param transition
     * @return
     */
    @SuppressWarnings("rawtypes")
    private boolean methodConditionFilter(Transition transition, String entityId) {
        boolean flag = false;
        String className = transition.getClassName();
        String methodName = transition.getMethodName();
        String entityName = methodName.substring(methodName.indexOf("(")+1, methodName.lastIndexOf(","));
        methodName = methodName.substring(0,methodName.indexOf("("));
        Class clz = null;
        Object obj = null;
        Method method = null;
        try {
            clz = Class.forName(className);
            obj = clz.newInstance();
            method = obj.getClass().getDeclaredMethod(methodName, new Class[]{ String.class, String.class});
            Object[] params = new Object[]{ StringUtils.trim(entityName), entityId};
            flag = (Boolean) method.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    private boolean varConditionFilter(Transition transition, String entityId) {
	    boolean flag = false;
	    return flag;
    }

	public OrgRoleInfo getOrgRoleInfo() {
		return orgRoleInfo;
	}

	public void setOrgRoleInfo(OrgRoleInfo orgRoleInfo) {
		this.orgRoleInfo = orgRoleInfo;
	}

	@Override
	public Todo getWfTodo(Todo todo) {
		
		return workFlowNodeNamesDao.getWfTodo(todo);
	}
}
