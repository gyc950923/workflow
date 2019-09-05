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
	<title>表单管理</title>
	<script src="/workflow-design/sys-resources/import/platform-framework.js"></script>
</head>
<body>
   <div id="toolbar" style="padding-top: 3px;">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add()">新增</a>  
	</div>
	<div id="userGrid"></div>
	<div id="dlg"></div>
	<script type="text/javascript">
		
		$(function(){
			initDataGridTable();
		})
		
		function initDataGridTable(){
			$('#userGrid').datagrid({
				fit:true, //格式自适应
		        striped:true,			//隔行变色
				rownumbers:true,		//序号
				singleSelect:true,		//选中单行
		        url:'<%=path%>/wf/flowDef/getStudents',		//链接数据
				pageNumber: 1,			//默认第一页
				pageSize: 10, 			//每页显示条数
				pageList: [5,10, 20, 30, 40, 50],
				pagination: true, 		//是否显示分页
				toolbar: "#toolbar",
				frozenColumns:[[//锁定列
	    			
	    		]],
	    		columns:[[
					{field:'name',title:'姓名',width:80},
					{field:'age',title:'年龄',width:70},
					{field:'days',title:'请假天数',width:100},
	    		]]
		    });
		}
		
		function formatDate(value){
			if(value == null){
				return "";
			}
			else{
				return new Date(value.time).format("yyyy-MM-dd hh:mm:ss");
			}
		}
		
		//时间格式化方法   format="yyyy-MM-dd hh:mm:ss"
		Date.prototype.format = function(format)
		{
			var o = {
				"M+" : this.getMonth() + 1,
				"d+" : this.getDate(),  
				"h+" : this.getHours(),  
				"m+" : this.getMinutes(),  
				"s+" : this.getSeconds(),  
				"q+" : Math.floor((this.getMonth() + 3) / 3),  
				"S" : this.getMilliseconds()
			}
			if (/(y+)/.test(format)) {
				format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
		      		- RegExp.$1.length));
			}
			for (var k in o) {
				if (new RegExp("(" + k + ")").test(format)) {  
				   format = format.replace(RegExp.$1, RegExp.$1.length == 1  
				       ? o[k]  
				       : ("00" + o[k]).substr(("" + o[k]).length));
		   		}
			}  
		 	return format;  
		}
		
		function add(){
			
			$('#dlg').dialog({
			    title: '新增',
			    width: 400,
			    height: 200,
			    closed: false,
			    cache: false,
			    href: '<%=path%>/workflow/form/add.jsp',
			    modal: true,
			    buttons:[{
					text:'保存',
					handler:function(data){
						var param={
								name:$("input[name='name']:checked").val(),
								age:$("input[name='age']:checked").val(),
								days:$("input[name='days']:checked").val(),
								flowId:$("input[name='flowId']:checked").val(),
						}
						$.ajax({
							type:"post",
							url:"<%=path%>/wf/flowDef/saveFrom",
							data:$("#addForm").serializeArray(),
							success:function(data){
								$('#userGrid').datagrid('reload');
								$('#dlg').dialog('close');
							}
						})		
					}
				},{
					text:'取消',
					handler:function(){
						$('#dlg').dialog('close');
					}
				}]
			})    
		}
	</script>
</body>
</html>