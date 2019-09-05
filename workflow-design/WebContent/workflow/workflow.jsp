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
	<title>工作流设计器</title>
<script type='text/javascript'>
 var rootpath = "<%=path%>";
</script>
<script src="<%=path%>/sys-resources/import/platform-framework.js"></script>
<script type="text/javascript" src="<%=path%>/plugin/easyui-1.4.5/datagrid-filter/datagrid-filter.js"></script>
	<style type="text/css">
		html,body {
			margin: 0;
			padding: 0;
			height: 100%;
			width: 100%;
		}
	</style>
</head>
<body>
   <div id="toolbar" style="padding-top: 3px;">
   		<span plain="false" class="label">模糊搜索</span>
		<input plain="false"  id = "PROCESS_ID" name = "PROCESS_ID" prompt = "流程名称" class="easyui-textbox"/>
		<a iconCls="search" plain="false"  onclick="searchProcess(this)" class="easyui-linkbutton" >查询</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newModel();">新建流程</a>  
    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"  plain="true" onclick="editModel()">编辑</a>
    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteModel()">删除</a>
	</div>
	<div id="dataGridTable"></div>
	<div id="dlg"></div>
	<script type="text/javascript">
	//var designerUrl = "http://192.168.70.190:8080/workflow-design/modeler.html";
		var designerUrl = "/workflow-design/modeler.html";
		$(function(){
			initDataGridTable();
		})		

		function searchProcess() {
			var obj = $.getValues($("#toolbar"));
			$('#dataGridTable').datagrid("load",{"processId":obj.PROCESS_ID});
		}
		
		function initDataGridTable(){
			$('#dataGridTable').datagrid({
		    	//列表居中
		    	singleSelect:false,
		    	ctrlSelect:true,
		    	checkOnSelect:true,
		    	selectOnCheck:true,
				fit:true,
		        striped:true,			//隔行变色
				rownumbers:true,		//序号
		        url:'<%=path%>/wf/flowDef/flowDefList',		//链接数据
				pageNumber: 1,			//默认第一页
				pageSize: 20, 			//每页显示条数
				pageList: [10, 20, 35, 50, 75, 100],
				pagination: true, 		//是否显示分页
				toolbar: "#toolbar",
				fitColumns:true,
				frozenColumns:[[//锁定列
	    			
	    		]],
	    		columns:[[
					{field:'ID',title:'主键',width:100,checkbox:true},
					{field:'PROCESS_ID',title:'流程名称',width:100,halign:'center'},
					{field:'VERSION',title:'版本',width:80,halign:'center'},
					/* {field:'UPLOAD_USER',title:'更新者',width:100},
					{field:'UPLOAD_TIME',title:'更新时间',width:150,
						formatter:function(value, row, index){
							return sysMsToDate(value);
						}
					}, */
					{field:'PUBLISH_USER',title:'发布者',width:100,halign:'center'},
					{field:'PUBLISH_TIME',title:'发布时间',width:150,halign:'center',
						formatter:function(value, row, index){
							return sysMsToDate(value);
						}
					},
					{field:'STATE',title:'启动状态',width:70,halign:'center',
						formatter:function(value, row, index){
							if(value == 0){
								return "未启动"
							}else{
								return "启动"
							}
						}	
					},
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
		
		function newModel(){
			var userName = "${requestScope.realName}";//获取request中的用户名称
			var userId = "";
			var deptId = "";
			var paramStr = "userId="+userId+"&userName="+userName+"&deptId="+deptId;
			window.open(designerUrl+"?"+paramStr);
			
		}
		function editModel(){
			var userName = "${requestScope.realName}";//获取request中的用户名称
			var rows = $("#dataGridTable").datagrid('getSelections');
			if(rows.length != 1){
				$.messager.alert('提示信息', '请选中一条数据', 'info');
				return ;
			}
			var modelId = rows[0].ID;
			var userId = "";
			var deptId = "";
			var paramStr = "modelId="+modelId+"&userId="+userId+"&userName="+userName+"&deptId="+deptId;
			window.open(designerUrl+"?"+paramStr);
		}
		
		function deleteModel(){
			var rows = $("#dataGridTable").datagrid('getSelections');
			if(rows.length == 0){
				$.messager.alert('提示信息', '请选中数据', 'info');
				return ;
			}
			$.messager.confirm('提示信息', '您确认想要删除记录吗？', function(r){
				if (r){
					var idCollection = [];
					$.each(rows, function(index , row){
						idCollection.push(row.ID);
					})
					$.ajax({
						type:'post',
						url:'<%=path%>/wf/workflow/deleteWorkflowDefs',
						data:{"ids": idCollection.join(",")},
						dataType:'json',
						success:function(data){
							$('#dataGridTable').datagrid("reload");
						},
						error:function(data){
							
						}
					})
				}
			});
		}
		
		function formatDate(value){
			if(value == null){
				return "";
			}
			else{
				return new Date(value.time).format("yyyy-MM-dd hh:mm:ss");
			}
		}
		
		//时间格式化方法   format="yyyy-MM-dd hh:mm:ss"
		Date.prototype.format = function(format)
		{
			var o = {
				"M+" : this.getMonth() + 1,
				"d+" : this.getDate(),  
				"h+" : this.getHours(),  
				"m+" : this.getMinutes(),  
				"s+" : this.getSeconds(),  
				"q+" : Math.floor((this.getMonth() + 3) / 3),  
				"S" : this.getMilliseconds()
			}
			if (/(y+)/.test(format)) {
				format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
		      		- RegExp.$1.length));
			}
			for (var k in o) {
				if (new RegExp("(" + k + ")").test(format)) {  
				   format = format.replace(RegExp.$1, RegExp.$1.length == 1  
				       ? o[k]  
				       : ("00" + o[k]).substr(("" + o[k]).length));
		   		}
			}  
		 	return format;  
		}
	</script>
</body>
</html>