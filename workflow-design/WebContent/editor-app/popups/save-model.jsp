<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<%	String path = request.getContextPath();	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>保存模板</title>

<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">

<script type="text/javascript" src="<%=path%>/editor-app/libs/angular_1.2.13/angular.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/configuration/url-config.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/configuration/toolbar-default-actions.js"></script>
	
<script type="text/javascript">

	function save() {
		var isSuccess = false;
		var currentWindow = frameElement.api;//获取弹出窗中的窗体对象
		/*参数modelID为为模板 id   json_model为流程图表的内容，*/
		var param={
				isInsert:$("input[name='state']:checked").val(),
				modelId:currentWindow.opener._MODEL_ID,
				json_model:currentWindow.opener.JSON_MODEL,
		}
		$.ajax({
			type:"post",
			url:"<%=path%>/wf/model/saveModel",
			data:param,
			async:false,
			dataType:'json',
			success:function(data){
				console.info(data);
				if(data.flag){
					/*将后台返回的流程ID更新到前台全局变量中*/
					currentWindow.opener._MODEL_ID = data.msg;
					isSuccess = true;
				}else{
					$("#msg").text(data.msg)
				}
			}
		})
		return isSuccess;
	}
	
	
	function clearForm(){
		window.close();
	}
	
</script>
</head>
<body >
	<div class="easyui-panel"  data-options="fit:true">
	    <form id="" method="post">
	    	<table cellpadding="5">
	    		<tr>
	    			<td><label id="msg" style="color: red;"></label></td>
	    		</tr>
	    		<tr>
					<td>
						<input type="radio" id="state_false"  name="state" value="true"/>保存为新的版本		
					</td>
				</tr>
	    		<tr>
					<td>
						<input type="radio" id="state_true"  name="state" value="false" checked="checked"/>发布
					</td>
				</tr>
	    	</table>
	    </form>
	</div>
</body>
</html>