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
	<title>待办管理</title>
	<script type="text/javascript">
	 var rootpath = "<%=path %>";
	</script>
	<script src="<%=path%>/sys-resources/import/platform-framework.js"></script>
	<%-- <jsp:include page="/sys-resources/import/design-import.jsp"/> --%>
</head>

<body>
	<input type="hidden" id="userId" name="userId" value="${userId }">
   	<div id="toolbar" style="padding-top: 3px;">
   		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="checkDetail()">查看流程详细</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="advance()">审批</a>  
<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" plain="true"  onclick="back()">回退</a>   -->
	</div>
	<div id="flowLogGrid"></div>
	<script type="text/javascript">
		
		$(function(){
			initDataGridTable();
		})
		
		function initDataGridTable(){
			$('#flowLogGrid').datagrid({
		    	//列表居中
				fit:true, //格式自适应
		        striped:true,			//隔行变色
				//title: '',	//表名
				rownumbers:true,		//序号
				/*sortname: 'id',		//默认排序列
				sortorder: 'desc',
				selectOnCheck:true,//升序*/
				singleSelect:true,		//选中单行
		        url:'<%=path%>/wf/flowLog/todoList',		//链接数据
				pageNumber: 1,			//默认第一页
				pageSize: 10, 			//每页显示条数
				pageList: [5,10, 20, 30, 40, 50],
				//showFooter: [], 		合计
				pagination: true, 		//是否显示分页
				toolbar: "#toolbar",
				fitColumns:true,
				queryParams: {
					userId: "2",
				},
				frozenColumns:[[//锁定列
				                
	    		]],
	    		columns:[[
					{field:'PROCESS_ID',title:'流程名称',width:150,halign:'center'},
					{field:'TASKINSTANCE_NAME',title:'当前任务',width:100,halign:'center'},
					{field:'ACTOR_NAME',title:'当前处理人',width:100,halign:'center'},
					{field:'CREATED_TIME',title:'到达时间',width:100,halign:'center',
						formatter:function(value, row, index){
							return sysMsToDate(value);
						}
					},
					{field:'wtr',title:'委托人',width:100,halign:'center'},
					{field:'TODODESCRIPTION',title:'待办描述',width:200,halign:'center'},
					{field:'WORKITEM_ID',title:'workitemId', hidden:true},
					{field:'PROCESSINSTANCE_ID,',title:'流程主键,', hidden:true},
					{field:'STEP_NUMBER',title:'步骤', hidden:true},
					{field:'FORMURL',title:'表单地址', hidden:true},
					/* {field:'bizType',title:'单据类型',width:100}, */
					/* {field:'workItemState',title:'状态',width:100,hidden:true}, */
	    		]]
		    });
		}
		
		function advance(){
			var selectedRow = $('#flowLogGrid').datagrid('getSelected');
			if(selectedRow != null){
				$('<div id="dbtodo"></div>').dialog({
				title: '待办业务',   
				width: 750,   
				height:650,   
				closed: false,
				resizable:true,
				maximizable:true,
				cache: false,     
			    href: '<%=path%>'+selectedRow.FORMURL,
				modal: true,
				data:{"ywid":selectedRow.ENTITYID},
				onLoad:function (){
				   
				},
				onClose:function() {
				  $("#dbtodo").dialog("destroy");
				},
				buttons:[{
				  text:'同意',
				  iconCls:'icon-save',
				  handler:function(){
				      $('<div id="dbback"></div>').dialog({
						title: '审批意见',   
						width: 550,   
						height:300,   
						closed: false,
						resizable:true,
						maximizable:true,
						cache: false,   
<%-- 						href: '<%=path%>/flow/flowback.form',   --%>
						modal: true,
						onLoad:function (){
						   
						},
						onClose:function() {
						  $("#dbback").dialog("destroy");
						},
						buttons:[{
						  text:'确定',
						  iconCls:'icon-save',
						  handler:function(){
								var comments = $("#comments",$("#flowbackform")).val();
								$.ajax({
									type:"post",
									url:"<%=path%>/wf/flowDef/advance",
									data:{'workitem':selectedRow.WORKITEM_ID,'processid':selectedRow.PROCESS_ID,'comments':comments,'ywid':selectedRow.ENTITYID},
									dataType:'text',
									success:function(data){
										if(data=="success"){
											$('#flowLogGrid').datagrid('reload');
											$("#dbback").dialog('close');
											$("#dbtodo").dialog('close');
										}
										
									},
									error:function(data){
										$('#flowLogGrid').datagrid('reload');
									}
								});
								
							}
					   },{
						text:'取消',
						iconCls:'icon-cancel',
						handler:function(){
							$("#dbback").dialog('destroy');
						}
		    	   }]
			      })
				 }
			   },{
		    		text:'不同意',
		    		iconCls:'icon-cancel',
					handler:function(){
						$('<div id="dbback"></div>').dialog({
						title: '审批意见',   
						width: 550,   
						height:300,   
						closed: false,
						resizable:true,
						maximizable:true,
						cache: false,   
<%-- 						href: '<%=path%>/flow/flowback.form',   --%>
						modal: true,
						onLoad:function (){
						   
						},
						onClose:function() {
						  $("#dbback").dialog("destroy");
						},
						buttons:[{
						  text:'确定',
						  iconCls:'icon-save',
						  handler:function(){
								$.ajax({
									type:"post",
									url:"<%=path%>/wf/executeflow/back",
									data:{'workItemId':selectedRow.WORKITEM_ID,'comments':"不准",'id':'258369', 'name':'管理员','myVar':'{"name":"郭宇成"},{"id","1"}'},
									dataType:'json',
									success:function(data){
										if(data.status=="ERROR"){
											alert(data.message);
										}
											$('#flowLogGrid').datagrid('reload');
											$("#dbback").dialog('close');
											$("#dbtodo").dialog('close');
										
									},
									error:function(data){
										$('#flowLogGrid').datagrid('reload');
									}
								});
								
							}
					   },{
						text:'取消',
						iconCls:'icon-cancel',
						handler:function(){
							$("#dbback").dialog('destroy');
						}
		    	   }]
			      })
				  }
		    	}]
			 });
			}else{
				$.messager.alert('提示信息', '未选中对象', 'info');
			}
		}
		
		function back(){
			var selectedRow = $('#flowLogGrid').datagrid('getSelected');
			if(selectedRow != null){
				$.ajax({
					type:"post",
					url:"<%=path%>/wgWorkflow/back",
					data:{'workitem':selectedRow.WORKITEM_ID},
					dataType:'json',
					success:function(data){
						if(data.status=="ERROR"){
							alert(data.message);
						}
						$('#flowLogGrid').datagrid('reload');
					},
					error:function(data){
						$('#flowLogGrid').datagrid('reload');
					}
				});
			}else{
				$.messager.alert('提示信息', '未选中对象', 'info');
			}
		}
		
		function sysMsToDate(ms){
			var date = new Date(ms);
			var yyyy = date.getFullYear();
			var MM = date.getMonth() + 1;
			MM = MM<10?"0"+MM:MM;
			var dd = date.getDate();
			dd = dd<10?"0"+dd:dd;
			var hh = date.getHours();
			hh = hh<10?"0"+hh:hh;
			var mm = date.getMinutes();
			mm = mm<10?"0"+mm:mm;
			var ss = date.getSeconds();
			ss = ss<10?"0"+ss:ss;
			return yyyy+"-"+MM+"-"+dd+" "+hh+":"+mm+":"+ss;
		}
		
		function checkDetail(){
			var rows = $("#flowLogGrid").datagrid("getSelections");
			if(rows.length != 1){
				$.messager.alert('提示信息', '请选中一条数据！', 'info');
				return ;
			}
			$("<div></div>").dialog({
				id:'detailDialog',
				title:'查看流程详细',
				iconCls:'icon-edit',
			    width:600,
			    height:400,
			    modal:true,
			    maximizable:true,
			    href: '<%=path%>/workflow/wf/toDetailLog?processinstanceId='+rows[0].PROCESSINSTANCE_ID,
			    onLoad:function(){
			    	
			    },
			    onClose:function(){
			    	$("#detailDialog").dialog('destroy');
			    },
			    buttons:[{
		    		text:'确定',
		    		iconCls:'icon-ok',
					handler:function(){
						$("#detailDialog").dialog('destroy');
					}
		    	},{
		    		text:'取消',
		    		iconCls:'icon-cancel',
					handler:function(){
						$("#detailDialog").dialog('destroy');
					}
		    	}]
			})
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
	</script>
</body>
</html>