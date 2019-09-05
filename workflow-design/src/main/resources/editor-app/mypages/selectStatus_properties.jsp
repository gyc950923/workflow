<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%
		String path = request.getContextPath();
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>选择运营状态</title>

	<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">
	<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
	
<script type="text/javascript">
	
	$(function(){
		initDataGrid();
		$.ajax({url:"<%=path%>/saveOrLoadNodeProperties",
				async:false,
				data:{resourceId:resourceId,modelId:modelId},
				dataType:"json",
				success:function(data){
					//alert(data.resourceId);
					if(data){
						if(data != ""){
							
							/*
							if(data.name){
								$("#name").val(data.name);
							}
							
							if(data.transaction){
								$("#transaction").val(data.transaction);
							}
							*/
							$('#ff').form('load',data);
						}
					}
				}
		});
	});

	function save() {

		$.post(
				"<%=path%>/saveOrLoadNodeProperties",
				{
				name : $("#name").val(),
				transaction : $("#transaction").val(),
				resourceId : resourceId,
				modelId:modelId
				}, 
				function(result) {
					alert("保存成功！");
					window.close();
				});
	}
	
	function confirm1(){
		$.messager.confirm('My Title', 'Are you confirm this?', function(r){
			if (r){
				alert('confirmed: '+r);
			}
		});
	}
	function prompt1(){
		$.messager.prompt('My Title', 'Please type something', function(r){
			if (r){
				alert('you type: '+r);
			}
		});
	}
	
	function clearForm(){
		window.close();
	}
	
	function initDataGrid(){
		$('#dataGrid').datagrid({
	    	//列表居中
			fit:true,
	        striped:true,			//隔行变色
			rownumbers:true,		//序号
			singleSelect:true,		//选中单行
	        url:null,		//链接数据
			pageNumber: 1,			//默认第一页
			pageSize: 10, 			//每页显示条数
			pageList: [5,10, 20, 30, 40, 50],
			pagination: false, 		//是否显示分页
			frozenColumns:[[//锁定列
    			//{field:'ck',checkbox:true},
    			//{field:'id',title:'序号',width:60,sortable:true}
    		]],
    		columns:[[
				/* {field:'name',title:'表单名称',width:150}, */
				
    		]]
	    });
	}
</script>
</head>
<body >
	<div id="dataGrid">
		
	</div>
	<table id="statusGrid" class="easyui-datagrid"
	   data-options="url:'datagrid_data.json',fitColumns:true,singleSelect:true,fit:true">
	   <thead>
		<tr>
			<th data-options="field:'id',width:15">序号</th>
			<th data-options="field:'entityDocStatusName',width:100">表单名称</th>
		</tr>
	   </thead>
	   <tbody>
		<tr>
			<td>001</td><td>运营状态1</td>
		</tr>
		<tr>
			<td>002</td><td>运营状态2</td>
		</tr>
	</tbody>
	   
	</table>
</body>
</html>