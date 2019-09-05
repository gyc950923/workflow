<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%
		String path = request.getContextPath();
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>结束节点属性</title>
<!--  
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/mypages/easyui/jQuery-easyui-master/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/mypages/easyui/jQuery-easyui-master/themes/icon.css">
<script src="<%=path%>/editor-app/libs/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/mypages/easyui/jQuery-easyui-master/jquery.easyui.min.js"></script>
-->
	<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">
	<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
	
<script type="text/javascript">
	
	$(function(){

		$.ajax({
			type:"post",
			url:"<%=path%>/wf/node/loadEndNodeProperties",
			async:false,
			data:{resourceId:$("#resourceId").val(),modelId:$("#modelId").val()},
			dataType:"json",
			success:function(data){
				if(data){
					$('form').form('load',data);
				}
			}
		});
	});

	function save() {
		var isSuccess = false;
		if($("#dataForm").form('validate')){
			$.ajax({
				type:"post",
				url:"<%=path%>/wf/node/saveEndNodeProperties",
				async:false,
				data:$("#dataForm").serializeArray(),
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
</script>
</head>
<body style="overflow: hidden;">

	<form id = "dataForm"  method="post" >
	<input type="hidden" id ="modelId" name="modelId" value="${param.modelId }">
	<input type="hidden" id="resourceId" name="resourceId" value="${param.resourceId }">
	<div class="easyui-tabs" style="width:470px;height:330px;margin: 10px auto;">
		<div title="常规" style="padding: 10px" >
				<table>
					<tr>
						<td>名称:</td>
						<td><input class="easyui-textbox" type="text" id="name" name="name" style="width: 375px" data-options="required:true,missingMessage:'必填'"/></td>
					</tr>
					<tr>
						<td>运营状态:</td>
						<td>
							<input class="easyui-textbox" type="text" id="" name="" style="width: 300px"/>
							<a href="#" class="easyui-linkbutton" style="height: 20px;">选择</a>
							<a href="#" class="easyui-linkbutton" style="height: 20px;">清除</a>
						</td>
					</tr>
					<tr style="height: 200px;">
						<td>描述:</td>
						<td >
							<input class="easyui-textbox" id="description" name="description" data-options="multiline:true,fit:true" />
						</td>
					</tr>	
				</table>
		</div>
		<div title="事件" style="padding: 10px">
			<div style="padding:  10px;display: inline-flex;">
				<label>启动：</label>
				<input id="callBackStatus_0" name="callBackStatus" type="radio" value="0" checked="checked">无
		        <!-- <input id="callBackStatus_10" name="callBackStatus" type="radio" style="margin-left: 20px;" value="10">本地 -->
		        <input id="callBackStatus_20" name="callBackStatus" type="radio" style="margin-left: 20px;" value="20">远程
	        </div>
	        <div id="tt" class="easyui-tabs" style="width:445px ;height: 280px;">
	        	<!-- <div title="本地方法" style="padding: 20px;">
	        		<table>
	        			<tr>
	        				<td>方法路径：</td>
	        				<td><input class="easyui-textbox" type="text" id="localAddress" name="localAddress" style="width: 330px" /></td>
	        			</tr>
	        		</table>
	        	</div> -->
	        	<div title="远程服务" style="padding: 20px;">
	        		<table>
	        			<tr>
	        				<td>服务地址：</td>
	        				<td><input class="easyui-textbox" type="text" id="restfulAddress" name="restfulAddress" style="width: 330px" /></td>
	        			</tr>
	        		</table>
	        	</div>
	        </div>
		</div>
	</div>
	<!-- <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">取消</a>
	</div> -->
	
</body>
</html>