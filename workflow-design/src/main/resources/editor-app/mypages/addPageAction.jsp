<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String path = request.getContextPath();	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加按钮选择</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">

<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<form id="addPageActionForm">
		<table style="margin: 30px 10px 5px 10px;line-height:30px;">
			<tr>
				<td>按钮名称</td>
				<td><input id="buttonName" class="easyui-textbox" style="width:300px;" data-options="required:true"></td>
			</tr>
			<tr>
				<td>事件名称</td>
				<td><input id="methodName" class="easyui-textbox" style="width:300px;" data-options="required:true"></td>
			</tr>
		</table>
	</form>
</body>
</html>