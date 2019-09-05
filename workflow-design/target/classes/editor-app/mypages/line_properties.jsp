<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%	String path = request.getContextPath();	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>连线属性</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">

<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		$.ajax({
			type:"post",
			url:"<%=path%>/wf/sequenceFlow/loadProperties",
			async:false,
			data:{resourceId:$("#resourceId").val(),modelId:$("#modelId").val()},
			dataType:"json",
			success:function(data){
				if(data){
					$('form').form('load',data);
					$("#pageAction").datagrid("loadData",data.pageActions);
					setTimeout(function(){
						selectCheckedTab(data);
					},200);
				}
			}
		});
	})
	
	function selectCheckedTab (data) {
		var obj = $("#start_"+data.start);
		selectTabs(obj);
	}
	
	/* 保存数据  */
	function save() {
		var isSuccess = false;
		$("#pageAction").datagrid("acceptChanges");//结束按钮权限的编辑状态
		if($("#dataForm").form('validate')){
			$("#jsonext").val(JSON.stringify($("#pageAction").datagrid("getRows")));
			var data = $("#dataForm").serializeArray();
			$.ajax({
				type:"post",
				url:"<%=path%>/wf/sequenceFlow/saveProperties",
				async:false,
				data:data,
				dataType:"json",
				success:function(data){
					isSuccess = true;
				}
			});
		}else{
			$.messager.alert('提示','存在不合法数据!','info');
		}
		return isSuccess;
	}
	
	
	function selectTabs(sender) {
		var attr =  $(sender).attr("checkedvalue").split(",");
		initTabs();
		$(attr).each(function(i, v) {
			var n = parseInt(v);
			if(n != 20){
				$("#tt").tabs('enableTab', n);
				if(i==0){
					$("#tt").tabs('select', n);
				}
			}
		})
	}	
	
	function initTabs() {
		$($("#tt").tabs("tabs")).each(function(i,v){
			$('#tt').tabs('disableTab', i);
		});
	}
	
	//初始化按钮权限grid
	function initPageActionGrid(data){
		$("#pageAction").datagrid({
			fit:true,
			fitColumns:true,
			border:false,
			rownumbers:true,
			toolbar:'#pageAction_toolbar',
			columns:[[
					{field:'ck', title:'复选框',checkbox:true},
					{field:'buttonName', title:'按钮名称', width:80, 
						editor : {
						    type : 'validatebox',
						    options : {
						       required : true
						    }
						}	
					},
					{field:'methodName', title:'事件名称', width:80, 
						editor : {
						    type : 'validatebox',
						    options : {
						       required : true
						    }
						}	
					},
          	]],
          	data:data
		})
		
	}
	//添加按钮选择
	function addPageAction(){
		$("<div></div>").dialog({
			id:'pageActionDialog',
			title: '添加按钮',
		    width: 400,
		    height: 230,
		    href: '<%=path%>/editor-app/mypages/addPageAction.jsp',
		    modal: true,
		    onClose:function(){
		    	$('#pageActionDialog').dialog('destroy');
		    },
		    buttons:[{
				text:'保存',
				handler:function(){
					var flag = $("#addPageActionForm").form("validate");
					if(flag){
						var gridObj = $("#pageAction");
						gridObj.datagrid("appendRow",{
							buttonName:$("#buttonName", $("#pageActionDialog")).val(),
							methodName:$("#methodName", $("#pageActionDialog")).val()
						});
						$('#pageActionDialog').dialog('destroy');
					}
				}
			},{
				text:'取消',
				handler:function(){
					$('#pageActionDialog').dialog('destroy');
				}
			}]
		})
	}
	//修改按钮选择
	function editPageAction(){
		var gridObj = $("#pageAction");
		var rows = gridObj.datagrid("getSelections");
		if(rows.length != 1){
			$.messager.alert('提示','请选中一条数据!','info');
			return ;
		}
		$("<div></div>").dialog({
			id:'pageActionDialog',
			title: '编辑按钮',
		    width: 440,
		    height: 230,
		    href: '<%=path%>/editor-app/mypages/addPageAction.jsp',
		    modal: true,
		    onClose:function(){
		    	$('#pageActionDialog').dialog('destroy');
		    },
		    onLoad:function(){
		    	$("#buttonName", $("#pageActionDialog")).textbox("setValue",rows[0].buttonName);
				$("#methodName", $("#pageActionDialog")).textbox("setValue",rows[0].methodName);
		    },
		    buttons:[{
				text:'保存',
				handler:function(){
					var flag = $("#addPageActionForm").form("validate");
					if(flag){
						var i = gridObj.datagrid("getRowIndex",rows[0]);
						gridObj.datagrid("updateRow",{
							index: i,
							row:{
								buttonName:$("#buttonName", $("#pageActionDialog")).val(),
								methodName:$("#methodName", $("#pageActionDialog")).val()
							}
						});
						$('#pageActionDialog').dialog('destroy');
					}
				}
			},{
				text:'取消',
				handler:function(){
					$('#pageActionDialog').dialog('destroy');
				}
			}]
		})
	}
	
	function delRow() {
		var r = $("#pageAction").datagrid("getSelected");
		if(null == r) {
			$.messager.alert('提示','请选中一条数据!','info');
			return ;
		}
		$("#pageAction").datagrid("deleteRow",$("#pageAction").datagrid("getRowIndex",r));
	}
</script>
</head>
<body>
	
	<form id="dataForm" name="dataForm">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'north'" style="height:30px">
					<input type="hidden" id ="modelId" name="modelId" value="${param.modelId }">
			 		<input type="hidden" id="resourceId" name="resourceId" value="${param.resourceId }">
			 		<input type="hidden" id="jsonext" name="jsonext">
				    <div style="padding:  5px;display: inline-flex;">
				    	<div style="width: 50px;text-align: right;">启动：</div>
				        <input id="start_0" name="start" type="radio" value="0" checked="checked" onclick="selectTabs(this)" checkedvalue="0,4">无
				        <input id="start_10" name="start" type="radio" style="margin-left: 15px;" value="10" onclick="selectTabs(this)" checkedvalue="1,4">SQL条件
				        <input id="start_30" name="start" type="radio" style="margin-left: 15px;" value="30" onclick="selectTabs(this)" checkedvalue="2,4">自定义变量
				        <input id="start_20" name="start" type="radio" style="margin-left: 15px;" value="20" onclick="selectTabs(this)" checkedvalue="3,4">自定义方法
				        <!-- <input id="start_40" name="start" type="radio" style="margin-left: 15px;" value="40" onclick="selectTabs(this)" checkedvalue="4">自定义按钮 -->
				    </div>
			</div>
			
			<div id="tt" name="tt" class="easyui-tabs" style="width: 100%;height: 350px;margin-top:30px;border:0px;">
				<div title="常规" style="padding: 20px;">
			    	<table >
						<tr>
							<td>名称：</td>
							<td><input id="name" name="name" class="easyui-textbox" style="width: 385px;"></td>
						</tr>
							
					</table>
			    </div>
		    
		    	
		    	<div title="sql条件" style="padding: 20px;disabled:true" >
			    	<div>
			    		<div>sql条件：</div>
						<input id="sqlCondition" name="sqlCondition" class="easyui-textbox" data-options="multiline:true" style="width: 430px;height: 80px;">
			    	</div> 
					<div style="margin-top: 5px;">
						运算符号：
						<select id="sqlOperator" name="sqlOperator" class="easyui-combobox" style="width:365px;" data-options="panelHeight:'auto',editable:false">   
						    <option value="==">==</option>
						    <option value="!=">!=</option>
						    <option value="&gt;">&gt;</option>   
						    <option value="&gt;=">&gt;=</option>   
						    <option value="&lt;">&lt;</option>   
						    <option value="&lt;=">&lt;=</option>   
						</select>
					</div>
					<div style="margin-top: 5px;">
						运算结果：
						<input id="sqlResult" name="sqlResult" class="easyui-textbox"  style="width:365px;">
					</div>
			    	<h4>说明：</h4>
			    	<div>1.sql条件为标准的sql语句，且返回值必须唯一</div>
			    	<div>2.sql条件返回值类型必须与运算结果的数据类型一致</div>
			    	<div>3.sql条件必须带上where条件id=:xid（其中id为实体ID,xid为固定标识）</div>
			    	<div>3.sql条件实例：select name from sys_user where id=:xid</div>
			    </div>
			    
			    
			    <div title="自定义变量" style="padding: 20px;">   
					<div>自定义变量：</div>
					<input id="varCondition" name="varCondition" class="easyui-textbox" data-options="multiline:true" style="width: 430px;height: 80px;">
			    	<div style="margin-top: 5px;">
						运算符号：
						<select id="varOperator" name="varOperator" class="easyui-combobox" style="width:365px;" data-options="panelHeight:'auto',editable:false">   
						    <option value="==">==</option>   
						    <option value="!=">!=</option>   
						    <option value="&gt;">&gt;</option>   
						    <option value="&gt;=">&gt;=</option>   
						    <option value="&lt;">&lt;</option>   
						    <option value="&lt;=">&lt;=</option>   
						</select>
					</div>
					<div style="margin-top: 5px;">
						运算结果：
						<input id="varResult" name="varResult" class="easyui-textbox"  style="width: 365px;">
					</div>
			    	<h4>说明：</h4>
			    	<div>1.自定义变量中填写变量名称</div>
			    	<div>2.自定义变量的个数最多为一个</div>
			    	<div>3.运算结果必须与自定义变量的数据类型保持一致</div>
			    	<div>4.自定义变量不区分大小写</div>
			    </div>
			    <div title="自定义方法" style="padding: 20px;">   
			        <div>绝对路径：</div>
					<input id="className" name="className" class="easyui-textbox" style="width: 430px;height: 80px;" data-options="multiline:true">
			        <div style="margin-top: 10px;">方法：</div>
					<input id="methodName" name="methodName" class="easyui-textbox" style="width: 430px;">
			    	<h4>说明：</h4>
			    	<div>1.绝对路径格式：包名.类名（例：com.online.ClssName）</div>
			    	<div>2.方法格式：方法名(实体名称,xid)（例：isTrue(sys_user, xid)）</div>
			    	<div>3.方法中的xid为固定标识，不能更改</div>
			    	<div>4.方法的返回值为boolean类型，返回true条件满足，返回false条件不满足</div>
			    	<div>5.方法访问限定符为public</div>
			    </div>
		    
			    <div title="按钮选择" style="padding: 5px;">
					<div id="pageAction_toolbar">
						<a href="#" onclick="addPageAction()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
						<a href="#" onclick="editPageAction()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
						<a href="#" onclick="delRow()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
					</div>
					<table id="pageAction" class="easyui-datagrid" data-options="pagination:false, singleSelect:true, fit:true, toolbar:'#pageAction_toolbar'">
						<thead>
							<tr>
								<th data-options="field:'buttonName',halign:'center',width:'200'">按钮名称</th>
								<th data-options="field:'methodName',halign:'center',width:'200'">事件名称</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</form>		
</body>
</html>