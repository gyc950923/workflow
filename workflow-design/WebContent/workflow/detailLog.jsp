<%-- <%@page import="com.online.platform.user.model.User"%> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path= request.getContextPath();
	request.setAttribute("path", path);
	request.setAttribute("url", request.getAttribute("url"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="UTF-8">
	<title>查看流程详细</title>
	<script src="<%=path%>/sys-resources/import/platform-framework.js"></script>
</head>
<body>
	<input type="hidden" id="processinstanceId" name="processinstanceId" value="19a87639-40ac-4ef0-8290-6ac423f66b72">
	<div id="detailLogGrid"></div>
	<script type="text/javascript">
		
		$(function(){
			initDataGridTable();
		})
		
		function initDataGridTable(){
			var deal_advance = 10;
			var deal_back = 20;
			$('#detailLogGrid').datagrid({
		    	//列表居中
				fit:true, //格式自适应
		        striped:true,			//隔行变色
				//title: '',	//表名
				rownumbers:true,		//序号
				singleSelect:true,		//选中单行
		        url:'<%=path%>/wf/flowLog/detaiLog',		//链接数据
				pageNumber: 1,			//默认第一页
				pageSize: 10, 			//每页显示条数
				pageList: [5,10, 20, 30, 40, 50],
				//showFooter: [], 		合计
				pagination: true, 		//是否显示分页
				queryParams: {
					processinstanceId: $("#processinstanceId").val(),
				},
				frozenColumns:[[//锁定列
	    			
	    		]],
	    		columns:[[
					{field:'PROCESS_ID',title:'流程名称',width:120,halign:'center'},
					{field:'TASKINSTANCE_NAME',title:'当前任务',width:100,halign:'center'},
					{field:'ACTOR_NAME',title:'当前处理人',width:100,halign:'center'},
					{field:'STEP_NUMBER',title:'步骤',width:100,halign:'center'},
					{field:'COMPELETEMODE',title:'处理结果',width:100,halign:'center',
						formatter:function(value,row,index){
							if(value == deal_advance){
								return "同意";
							}else if(value == deal_back){
								return "退回";
							}else{
								return "待处理";
							}
							
						}	
					},
					{field:'COMMENTS',title:'审批意见',width:100,halign:'center'},
					{field:'created_time',title:'创建时间',width:160,halign:'center',
						formatter:function(value,row,index){
							return sysMsToDate(value);
						}
					},
					{field:'FORMURL',title:'表单地址', hidden:true},
	    		]]
		    });
		}
		
		function sysMsToDate(ms){
			var date = new Date(ms);
			var yyyy = date.getFullYear();
			var MM = date.getMonth() + 1;
			MM = MM<10?"0"+MM:MM;
			var dd = date.getDate();
			dd = dd<10?"0"+dd:dd;
			var hh = date.getHours();
			hh = hh<10?"0"+hh:hh;
			var mm = date.getMinutes();
			mm = mm<10?"0"+mm:mm;
			var ss = date.getSeconds();
			ss = ss<10?"0"+ss:ss;
			return yyyy+"-"+MM+"-"+dd+" "+hh+":"+mm+":"+ss;
		}
		
		
	</script>
</body>
</html>