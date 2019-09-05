<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String path = request.getContextPath();	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加接收人</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">

<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
    	<div data-options="region:'center',border:false">
    		<div id="resultGrid_tb">
				<form id="resultGrid_queryForm">
					&nbsp;&nbsp;姓名：<input id="name" name="name" class="easyui-textbox" style="width: 170px;">&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="query('resultGrid_queryForm')">查询</a> 
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('resultGrid_queryForm')" data-options="iconCls:'icon-reload'">重置</a>	
				</form>
			</div><%-- url:'<%=rootpath %>/user/getPageList', --%>
    		<table class="easyui-datagrid" id="resultGrid"
		        data-options="
			        fit:true,
			        url:'<%=path%>/wf/business/getAllUsersByPage',
			        toolbar:'#resultGrid_tb',
			        fitColumns:true,
			        striped:true,
			        rownumbers:true,
			        pagination:true,
			        border:false">   
			    <thead>   
			        <tr>
			        	<th data-options="field:'id',align:'center',checkbox:true">主键</th>
			            <!-- <th data-options="field:'userName',width:60,halign:'center'">姓名</th> -->
			            <th data-options="field:'name',width:100,halign:'center'">姓名</th>   
			        </tr>   
			    </thead>   
			</table>
    	</div>   
	</div>  
	<script type="text/javascript">
		function query(formId){
			var queryData = {
					name : $("#name",$("#"+formId)).textbox("getValue")
			}
			$("#resultGrid").datagrid("load" , queryData);
		}
		
		function clearForm(formId){
			$("#"+formId).form("reset");
		}
	</script>
</body>
</html>