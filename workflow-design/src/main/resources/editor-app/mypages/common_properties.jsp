<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%
		String path = request.getContextPath();
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>通用活动节点属性</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/mypages/easyui/jQuery-easyui-master/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/mypages/easyui/jQuery-easyui-master/themes/icon.css">
<script src="<%=path%>/editor-app/libs/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/mypages/easyui/jQuery-easyui-master/jquery.easyui.min.js"></script>
<script type="text/javascript">

	var shapes = window.dialogArguments;
	var resourceId =  shapes[0].resourceId;
	var modelId = shapes[0].parent.resourceId;
	//alert("您传递的参数为：" + shapes[0].resourceId);
	 console.info("pop_windown:" + resourceId + ",modelId:" + modelId);
	
	$(function(){
		
		$.ajax({url:"<%=path%>/saveOrLoadNodeProperties",
				async:false,
				data:{resourceId:resourceId,modelId:modelId},
				dataType:"json",
				success:function(data){
					//alert(data.resourceId);
					if(data){
						if(data != ""){
							
							if(data.name){
								$("#name").val(data.name);
							}
							
							if(data.transaction){
								$("#transaction").val(data.transaction);
							}
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
	
</script>
</head>
<body>
	<div >
		节点名字：<input type="text" name="name" id="name"> <br/>
		办理人：<input type="text" name="transaction" id="transaction"> <br/>
		<button onclick="save()">确定</button>
		&nbsp;&nbsp;
		<button onclick="window.close()">取消</button>
	</div>
	<br/>
	easyui简单测试
	<div style="margin:20px 0;">
		<a href="#" class="easyui-linkbutton" onclick="confirm1();">Confirm</a>
		<a href="#" class="easyui-linkbutton" onclick="prompt1()">Prompt</a>
	</div>
</body>
</html>