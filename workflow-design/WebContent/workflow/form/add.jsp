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
	<title>新增</title>
	<script src="/workflow-design/sys-resources/import/platform-framework.js"></script>
	
</head>
<body>
	<style type="text/css">
		.addForm-cell{
			margin: 5px;
		}
	</style>
	<form id="addForm" method="post">
	    <div class="addForm-cell">
	        <label for="name">姓名:</label>
	        <input class="easyui-textbox" type="text" name="name"/>
	    </div>
	    <div class="addForm-cell">
	        <label for="age">年龄:</label>
	        <input class="easyui-textbox" type="text" name="age"/>   
	    </div>
	    <div class="addForm-cell">
	        <label for="days">请假天数:</label>
	        <input class="easyui-textbox" type="text" name="days"/>
	    </div>
	    <div class="addForm-cell">
	        <label for="flowId">流程名称:</label>
	        <input class="easyui-textbox" type="text" name="flowId"/>
	    </div>
	</form>  
</body>
</html>