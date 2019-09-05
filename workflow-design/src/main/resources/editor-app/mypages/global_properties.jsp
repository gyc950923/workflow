<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%
		String path = request.getContextPath();
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>全局属性</title>

<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
	
<script type="text/javascript">
	
	$(function(){

		$.ajax({
			type:"get",
			url:"<%=path%>/wf/global/loadProperties",
				async:false,
				data:{modelId:$("#modelId").val()},
				dataType:"json",
				success:function(data){
					if(data){
						$('#dataForm').form('load',data);
						$("#entityCode").val(data.entityValue);
						$("#formName").textbox('setValue',data.form.name);
						$("#formUrl").val(data.form.url);
						$("#formInfo").textbox('setValue',data.form.description);
					}
				}
		});
	});

	function save() {
		var isSuccess = false;
		var globalJson = {
			name : $("#name").val(),
			description : $("#description").val(),
			startRule : {
				entityName : $("#entityName").val(),
				entityCode : $("#entityCode").val(),
				formName : $("#formName").val(),
				formUrl : $("#formUrl").val(),
				formInfo : $("#formInfo").val(),
				state : $("input[name='state']:checked").val()
			},
			toDoDescription : $("#toDoDescription").val()
		}
		if($("#dataForm").form('validate')){
			$.ajax({
				type:"post",
				async:false,
				url:"<%=path%>/wf/global/saveProperties",
				data:{modelId:$("#modelId").val(),jsonStr:JSON.stringify(globalJson)},//$("#dataForm").serializeArray(),
				//dataType:null,
				success:function(data){
					isSuccess = true;
				}
			});
		}else{
			$.messager.alert('提示','存在不合法数据!','info');
		}
		return isSuccess;
	}
	
	function clearForm(){
		window.close();
	}
	function clearData(id){//id为需要清空内容的标签对象
		$("#"+id).textbox('setValue',"");
		if(arguments[1]){
			$("#"+arguments[1]).val("");
		}
	}
	
	function showState(){
		$('#dlg').dialog({
		    title: '选择表单',
		    width: 400,
		    height: 200,
		    closed: false,
		    cache: false,
		    href: '<%=path%>/editor-app/mypages/selectForm_properties.jsp',
		    modal: true,
		    buttons:[{
				text:'保存',
				handler:function(){
				}
			},{
				text:'取消',
				handler:function(){
					 $('#dlg').dialog('close');
				}
			}]
		});
	}
	
</script>
</head>
<body style="overflow: hidden;">
	<form id="dataForm" action="">
	<input type="hidden" id="modelId" name="modelId" value="${param.modelId }">
	<div id="" class="easyui-tabs" title="" style="width: 470px;height: 290px;margin: 10px auto;">
		<div title="常规" style="padding: 10px;">
			<table >
				<tr>
					<td>名称:&emsp;&emsp;&nbsp;</td>
					<td><input class="easyui-textbox" type="text" id="name" name="name"  style="width: 375px;" data-options="required:true,missingMessage:'必填'"/></td>
				</tr>
				<tr style="height: 190px;">
					<td>描述:</td>
					<td >
						<input id="description" name="description" class="easyui-textbox" data-options="multiline:true,fit:true"/>
					</td>
				</tr>	
			</table>
		</div>
		<div title="启动规则" style="padding:10px;">
	    	<table>
				<tr>
					<td>主实体:</td>
					<td>
						<input class="easyui-textbox" type="text" id="entityName" name="entityName" style="width: 295px" readonly="true"></input>
						<input type="hidden" id="entityCode" name="entityCode"></input>
						<a href="#" class="easyui-linkbutton" style="height: 20px;" onclick="addEntity()">选择</a>
						<a href="#" class="easyui-linkbutton" style="height: 20px;" onclick="clearData('entityName','entityCode')">清除</a>
					</td>
				</tr>
				<tr>
					<td>表单:</td>
					<td>
						<input class="easyui-textbox" type="text" id="formName" name="formName" style="width: 295px" readonly="true"></input>
						<input type="hidden" id="formUrl" name="formUrl"></input>
						<a href="#" class="easyui-linkbutton" style="height: 20px;" onclick="addForm()">选择</a>
						<a href="#" class="easyui-linkbutton" style="height: 20px;" onclick="clearData('formName','formUrl')">清除</a>
					</td>
				</tr>
				<tr style="height: 150px;">
					<td>说明:</td>
					<td >
						<input id="formInfo" name="formInfo" class="easyui-textbox" data-options="multiline:true,fit:true"/>
					</td>
				</tr>
				<tr>
					<td>流程状态：</td>
					<td>
						<input type="radio" id="state_true"  name="state" value="true" />启动
						<input type="radio" id="state_false"  name="state" value="false" checked="checked"/>未启动
					</td>
				</tr>
				
			</table>
	    </div>
	    <div title="待办描述" style="padding:10px;">
	    	<table>
	    		<tr style="height: 190px;">
					<td>待办描述：</td>
					<td >
						<input id="toDoDescription" name="toDoDescription" class="easyui-textbox" style="width: 375px;height: 190px;"  data-options="multiline:true"/>
					</td>
				</tr>
	    	</table>
	    </div>
	</div>
	</form>
	<!-- <div style="text-align:center;padding:5px;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()">确定</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm(this)">取消</a>
	</div> -->
	<div id="dlg"></div>
	<script type="text/javascript">
	//添加表单
	function addForm(){
		$("<div></div>").dialog({
			id:'addFormDialog',
			title:'添加表单',
			iconCls:'icon-save',
		    width:480,
		    height:300,
		    modal:true,
		    maximizable:true,
		    href: '<%=path%>/editor-app/mypages/addForm.jsp',
		    onClose:function(){
		    	$("#addFormDialog").dialog('destroy');
		    },
		    buttons:[{
	    		text:'确定',
	    		iconCls:'icon-ok',
				handler:function(){
					var resultRow = $("#resultGrid").datagrid("getSelected");
					$("#formName").textbox("setValue", resultRow.name);
					$("#formUrl").val(resultRow.url);
					$("#addFormDialog").dialog('destroy');
				}
	    	},{
	    		text:'取消',
	    		iconCls:'icon-cancel',
				handler:function(){
					$("#addFormDialog").dialog('destroy');
				}
	    	}]
		})
	}
	//添加实体
	function addEntity(){
		$("<div></div>").dialog({
			id:'addEntityDialog',
			title:'添加实体',
			iconCls:'icon-save',
		    width:480,
		    height:300,
		    modal:true,
		    maximizable:true,
		    href: '<%=path%>/editor-app/mypages/addEntity.jsp',
		    onClose:function(){
		    	$("#addEntityDialog").dialog('destroy');
		    },
		    buttons:[{
	    		text:'确定',
	    		iconCls:'icon-ok',
				handler:function(){
					var resultRow = $("#resultGrid").datagrid("getSelected");
					$("#entityName").textbox("setValue", resultRow.name);
					$("#entityCode").val(resultRow.code);
					$("#addEntityDialog").dialog('destroy');
				}
	    	},{
	    		text:'取消',
	    		iconCls:'icon-cancel',
				handler:function(){
					$("#addEntityDialog").dialog('destroy');
				}
	    	}]
		})
	}
	</script>
</body>
</html>