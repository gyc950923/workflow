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
	<title>经过已结束流程</title>
	<script src="<%=path%>/sys-resources/import/platform-framework.js"></script>
</head>
<body>
	<input type="hidden" id="userId" name="userId" value="258369">
   <div id="toolbar" style="padding-top: 3px;">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-page-find" plain="true" onclick="checkDetail()">查看流程详细</a>  
<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="advance()">处理</a>   -->
<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="back()">回退</a>   -->
	</div>
	<div id="flowLogGrid"></div>
	<div id="dlg"></div>
	<script type="text/javascript">
		
		$(function(){
			initDataGridTable();
		})
		
		function initDataGridTable(){
			$('#flowLogGrid').datagrid({
		    	//列表居中
				fit:true, //格式自适应
		        striped:true,			//隔行变色
				//title: '',	//表名
				rownumbers:true,		//序号
				/*sortname: 'id',		//默认排序列
				sortorder: 'desc',
				selectOnCheck:true,//升序*/
				singleSelect:true,		//选中单行
		        url:'<%=path%>/wf/flowLog/passAndFinish',		//链接数据
				pageNumber: 1,			//默认第一页
				pageSize: 10, 			//每页显示条数
				pageList: [5,10, 20, 30, 40, 50],
				//showFooter: [], 		合计
				pagination: true, 		//是否显示分页
				toolbar: "#toolbar",
				fitColumns:true,
				queryParams: {
					userId: $("#userId").val(),
				},
				frozenColumns:[[//锁定列
	    			
	    		]],
	    		columns:[[
					{field:'XID',title:'实体ID',width:160,halign:'center',hidden:true},
					{field:'PROCESSINSTANCE_ID',title:'流程主键',width:300,halign:'center',hidden:true},
					{field:'NAME',title:'流程名称',width:120,halign:'center'},
					/* {field:'TASKINSTANCE_NAME',title:'当前任务',width:100,halign:'center'},
					{field:'ACTOR_NAME',title:'当前处理人',width:300,halign:'center'}, */
					{field:'CREATED_TIME',title:'创建时间',width:160,halign:'center'
					},
					{field:'END_TIME',title:'结束时间',width:160,halign:'center'
					},
					{field:'PROCESSDESCRIPTION',title:'流程描述',width:120,halign:'center'},
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
		
		function checkDetail(){
			var rows = $("#flowLogGrid").datagrid("getSelections");
			if(rows.length != 1){
				$.messager.alert('提示信息', '请选中一条数据！', 'info');
				return ;
			}
			$("<div></div>").dialog({
				id:'detailDialog',
				title:'查看流程详细',
				iconCls:'icon-edit',
			    width:500,
			    height:400,
			    modal:true,
			    maximizable:true,
			    href: '<%=path%>/workflow/toDetailLog?processinstanceId='+rows[0].PROCESSINSTANCE_ID,
			    onLoad:function(){
			    	
			    },
			    onClose:function(){
			    	$("#detailDialog").dialog('destroy');
			    },
			    buttons:[{
		    		text:'确定',
		    		iconCls:'icon-ok',
					handler:function(){
						$("#detailDialog").dialog('destroy');
					}
		    	},{
		    		text:'取消',
		    		iconCls:'icon-cancel',
					handler:function(){
						$("#detailDialog").dialog('destroy');
					}
		    	}]
			})
		}
	</script>
</body>
</html>