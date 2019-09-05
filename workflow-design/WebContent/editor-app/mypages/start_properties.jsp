<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>开始节点属性</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript">
	
	$(function(){
		/* 加载后台数据  */
		$.ajax({
			type:"post",
			url:"<%=path%>/wf/node/loadStartNodeProperties",
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
	/* 保存数据  */
	function save() {
		var isSuccess = false;
		if($("#dataForm").form('validate')){
			$.ajax({
				type:"post",
				url:"<%=path%>/wf/node/saveStartNodeProperties",
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
	/* 关闭弹出窗口 */
	function clearForm(){
		window.close();
	}
</script>
</head>
<body style="overflow: hidden;">
	 <form id = "dataForm"  method="post">
	 <input type="hidden" id ="modelId" name="modelId" value="${param.modelId }">
	 <input type="hidden" id="resourceId" name="resourceId" value="${param.resourceId }">
	 <div class="easyui-tabs" style="width:450px;height:330px;margin: 10px auto;">
		<div title="常规" style="padding: 10px" >
			<table >
				<tr>
					<td>名称:</td>
					<td><input class="easyui-textbox" type="text" id="name" name="name"  style="width: 375px" data-options="required:true,missingMessage:'必填'"/></td>
				</tr>
				<tr>
					<td>活动:</td>
					<td>
						<input class="easyui-textbox" type="text" style="width: 300px"/>
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
	</div>
	</form>
	<!-- <div style="text-align:center;padding:5px">
    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()">确定</a>
    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">取消</a>
	</div> -->
</body>
</html>