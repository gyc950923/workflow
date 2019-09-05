<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%
		String path = request.getContextPath();
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>表达式</title>

	<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">
	<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
	
</head>
<body >
	<input id="tempExpression" class="easyui-textbox" data-options="multiline:true" style="width:100%;height: 100%;"> 
</body>
</html>