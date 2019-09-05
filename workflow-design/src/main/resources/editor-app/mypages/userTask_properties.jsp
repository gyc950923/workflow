<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<%	String path = request.getContextPath();	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户活动节点属性</title>

<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/editor-app/libs/jquery-easyui/themes/icon.css">

<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/editor-app/libs/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		initFieldAuthorityGrid();
		initPageActionGrid();
		initTabsSelect();
		/* 加载后台数据 */
		$.ajax({
			url:"<%=path%>/wf/userTaskNode/loadProperties",
			async:false,
			data:{resourceId:$("#resourceId").val(),modelId:$("#modelId").val()},
			dataType:"json",
			success:function(result){
				var data = result[0];
				$("#backRange").combobox("loadData", result[1]);
				if(data){
					/*加载简单json的数据*/
					$('#dataForm').form('load',data);
					$("#isSendMsg"+data.isSendMsg).attr('checked',true);
					/*加载多层json嵌套的数据*/
					if(data.inlineTasks.length!=0){
						/*流转规则*/
						var advanceRule = (data.inlineTasks)[0].advanceRule;
						if((data.inlineTasks)[0].advanceRule!=null){
							$("#isAdvanceConfirm_"+advanceRule.advanceConfirm).attr('checked',true);
							$("#isAdjustStaff_"+advanceRule.adjustStaff).attr('checked',true);
							$("#isToBeRead_"+advanceRule.toBeRead).attr('checked',true);
						}	
						
						/*执行规则*/
						//$("#title").textbox("setValue",(data.inlineTasks)[0].name);
						$("#title").textbox("setValue",(data.inlineTasks)[0].name);
						
						var userRule = (data.inlineTasks)[0].userRule;
						if(userRule!=null){
							if(userRule.editForm!=null){
								$("#formName").textbox("setValue", userRule.editForm.name);
								$("#formUrl").val(userRule.editForm.url);
							}else if(userRule.viewForm !=null){
								$("#formName").textbox("setValue", userRule.viewForm.name);
								$("#formUrl").val(userRule.viewForm.url);
							}else if(userRule.listForm !=null){
								$("#formName").textbox("setValue", userRule.listForm.name);
								$("#formUrl").val(userRule.listForm.url);
							}
							if(userRule.performer!=null){
								if(userRule.performer.userInfos.length!=0){
									$('#user').datagrid('loadData', userRule.performer.userInfos);
								}
								if(userRule.performer.roleInfos.length!=0){
									$('#role').datagrid('loadData', userRule.performer.roleInfos);
								}
								if(userRule.performer.deptInfos.length!=0){
									$('#org').datagrid('loadData', userRule.performer.deptInfos);
								}
								if(userRule.performer.orgRoleInfos.length!=0){
									$('#orgrole').datagrid('loadData', userRule.performer.orgRoleInfos);
								}
								if(userRule.performer.sqlInfos.sqlKey!=""){
									$("#sqlKey").textbox("setValue",userRule.performer.sqlInfos.sqlKey);
								}
								if(userRule.performer.restInfos.restKey!=""){
									$("#restKey").textbox("setValue",userRule.performer.restInfos.restKey);
								}
								
								if(!$.isEmptyObject(userRule.performer.orgRoleInfo)){
									$("#departmentSrc").combobox("select",userRule.performer.orgRoleInfo.departmentSrc);
									$("#conditionName").textbox("setValue",userRule.performer.orgRoleInfo.conditionName);
									$("#conditionId").val(userRule.performer.orgRoleInfo.conditionId);
									if('90' == userRule.performer.orgRoleInfo.allNode){
										$("#allNode").attr("checked", true);
									}
								}
							}
							$("#assignmentStrategy").combobox("select", userRule.formTaskEnum);
							$("#loopStrategy").combobox("select", userRule.loopStrategy);
							
							$("#batchApproval").attr("checked",userRule.batchApproval);
							
							$("#actorAssignType_"+userRule.actorAssignType).attr('checked',true);
							/* $("#actorIdKey").textbox("setValue", userRule.actorIdKey);
							$("#actorNameKey").textbox("setValue", userRule.actorNameKey); */
						}
						//字段权限
						var fieldAuthoritys = (data.inlineTasks)[0].fieldAuthoritys;
						if(fieldAuthoritys.length !=0){
							$("#fieldAuthority").datagrid("loadData",{total:fieldAuthoritys.length,rows:fieldAuthoritys});
						}
						//按钮权限
						var pageActions = (data.inlineTasks)[0].pageActions
						if(pageActions.length != 0){
							$("#pageAction").datagrid("loadData",{total:pageActions.length,rows:pageActions});
						}
						/*回退规则*/
						var backRule = (data.inlineTasks)[0].backRule;
						if(backRule!=null){
							$("#isBackConfirm_"+backRule.backConfirm).attr('checked',true);
							//$("#backRange").combobox("select", backRule.backRange.nodeId);
							var backRangeList = $("#backRange").combobox("getData");
							if(backRangeList.length > 0){
								$.each(backRangeList,function(index,node){
									if(node.value == backRule.backRange.nodeId){
										$("#backRange").combobox("select", node.value);
										return;
									}
								})
							}
						}			
					}
					//初始化tabs页签状态
					selectCheckedTab(data);
					
				}
			}
		});
	});
	
	function selectCheckedTab (data) {
		var obj = $("#callBackStatus_"+data.callBackStatus);
		selectSplitTabs(obj, 'tt');
		var obj = $("#actorAssignType_"+data.tasks[0].userRule.actorAssignType);
		selectSplitTabs(obj, 'targetTab');
	}
		
	function selectSplitTabs(sender, id) {
		initTabs(id);
		var attr = $(sender).attr("checkedvalue").split(",");
		$(attr).each(function(i, v) {
			var n = parseInt(v);
			if(n != 20){
				$("#"+id).tabs('enableTab', n);
				if(i==0){
					$("#"+id).tabs('select', n);
				}
			}
		});
	}
	
	function initTabs(id) {
		$($("#"+id).tabs("tabs")).each(function(i,v){
			$('#'+id).tabs('disableTab', i);
		});
	} 
	
	function initTabsSelect() {
		$('#secondTabs').tabs({
		    onSelect:function(title,index){
		    	var obj = $("input[name=actorAssignType]:checked").attr("checkedvalue").split(",");
				$(obj).each(function(i, v) {
					var n = parseInt(v);
					if(n != 20){
						$("#targetTab").tabs('enableTab', n);
						if(i==0){
							$("#targetTab").tabs('select', n);
						}
					}
				})
		    }
		});
	}	
	
	/* 保存数据数据 */
	function save() {
		var isSuccess = false;
		$("#fieldAuthority").datagrid("acceptChanges");//结束字段权限的编辑状态
		$("#pageAction").datagrid("acceptChanges");//结束按钮权限的编辑状态
		if($("#dataForm").form('validate')){
			if(validateRepeatName($("#name").val())){
				$.messager.alert('提示','节点名称重复!','info');
				return isSuccess;
			}
			
			var allNode = ($("input[name=allNode]:checked").length>0 ? 90 : 0);
			var activityJson = {
					name : $("#name").val(),
					description : $("#description").val(),
					entityDocStatusName : $("#entityDocStatusName").val(),
					expression : $("#expression").val(),
					callBackStatus : $("input[name='callBackStatus']:checked").val(),
					localAddress : $("#localAddress").val(),
					restfulAddress : $("#restfulAddress").val(),
					isSendMsg : $("input[name='isSendMsg']:checked").val(),
					msgtTemplate : $("#msgtTemplate").val(),
					advanceRule : {
						isAdvanceConfirm : $("input[name='isAdvanceConfirm']:checked").val(),
						isAdjustStaff : $("input[name='isAdjustStaff']:checked").val(),
						isToBeRead : $("input[name='isToBeRead']:checked").val(),
					},
					userRule : {//执行规则
						title : $("#title").val(),
						formType : $("#formType").combobox('getValue'),
						formName : $("#formName").val(),
						formUrl : $("#formUrl").val(),
						//userName : $("#userName").val(),
						userInfos : $("#user").datagrid("getRows"),
						roleInfos : $("#role").datagrid("getRows"),
						deptInfos : $("#org").datagrid("getRows"),
						orgRoleInfos:{
							"orgRoleInfosList":$("#orgrole").datagrid("getRows"),
							"departmentSrc":$("#departmentSrc").combobox("getValue"),
							"allNode":allNode,
							"conditionId":$("#conditionId").val(),
							"conditionName":$("#conditionName").textbox("getValue")
						},
						formTaskEnum : $("#assignmentStrategy").combobox('getValue'),
						loopStrategy : $("#loopStrategy").combobox('getValue'),
						batchApproval : $("#batchApproval").is(':checked'),
						actorAssignType : $("input[name='actorAssignType']:checked").val(),
						/* actorIdKey : $("#actorIdKey").val(),
						actorNameKey : $("#actorNameKey").val(), */
						sqlInfos : {"sqlKey":$("#sqlKey").textbox("getValue")},
						restInfos : {"restKey":$("#restKey").textbox("getValue")}
					},
					backRule : {
						isBackConfirm :$("input[name='isBackConfirm']:checked").val(),
						backRange :$("#backRange").combobox('getValue')
						/* returnMode : $("#returnMode").combobox('getValue'), */
					},
					fieldAuthority:$("#fieldAuthority").datagrid("getRows"),
					pageAction:$("#pageAction").datagrid("getRows"),
			}
			$.ajax({
				type:"post",
				url:"<%=path%>/wf/userTaskNode/saveProperties",
				async:false,
				data:{resourceId:$("#resourceId").val(),modelId:$("#modelId").val() ,jsonStr : JSON.stringify(activityJson)},
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
	function validateRepeatName(name){
		var flag;
		$.ajax({
			type:"post",
			url:"<%=path%>/wf/userTaskNode/validate",
			async:false,
			data:{name:name,resourceId:$("#resourceId").val(),modelId:$("#modelId").val()},
			dataType:"json",
			success:function(data){			
				flag = data.flag;
			}
		});
		return flag;
	}
	/* 关闭window.open弹出窗口 */
	function clearForm(){
		window.close();
	}
	
	/* 弹出选择运营状态弹窗 */
	function showStatus(){
		$('#dlg').dialog({
		    title: '选择运营状态',
		    width: 400,
		    height: 200,
		    closed: false,
		    cache: false,
		    href: '<%=path%>/editor-app/mypages/selectStatus_properties.jsp',
		    modal: true,
		    buttons:[{
				text:'保存',
				handler:function(data){
					var selectRow=$("#statusGrid").datagrid("getSelected");
					$("#entityDocStatusName").textbox('setValue',selectRow.entityDocStatusName);
					$('#dlg').dialog('close');
				}
			},{
				text:'取消',
				handler:function(){
					$('#dlg').dialog('close');
				}
			}]
		})
	}
	function showExpression(){
		$('#dlg').dialog({
		    title: '选择表单',
		    width: 400,
		    height: 200,
		    closed: false,
		    cache: false,
		    content: "aaa",
		    modal: true,
		    href: '<%=path%>/editor-app/mypages/expression.jsp',
		    onLoad:function(){    
		        $("#tempExpression").textbox("setValue", $("#expression").textbox("getValue"));
		    },
		    buttons:[{
				text:'保存',
				handler:function(data){
					$("#expression").textbox("setValue", $("#tempExpression").textbox("getValue"));
					$('#dlg').dialog('close');
				}
			},{
				text:'取消',
				handler:function(){
					$('#dlg').dialog('close');
				}
			}]
		})
	}
	/*初始化下拉框内容*/
	function initCombobox(data,target){
		var param=[];
		$.each(data,function(index,tempData){
			if(tempData.value==target){
				param.push({'text':tempData.text,'value':tempData.value,'selected':'true'});
			}else{
				param.push({'text':tempData.text,'value':tempData.value});
			}
		});
		return param;
	}
	/*初始化文本框*/
	function initTextBox(data){
		var param='';
		if(data!=""){
			param=data;
		}
		return param;
	}
	/* 根据标签id清空文本框内容  */
	function clearData(id){//id为需要清空内容的标签对象
		$("#"+id).textbox('setValue',"");
		if(arguments[1]){//清除对应的隐藏域
			$("#"+arguments[1]).val("");
		}
	}
</script>
</head>
<body style="overflow: hidden;">
	<form id="dataForm" action="">
	<input type="hidden" id="modelId" name="modelId" value="${param.modelId }">
	<input type="hidden" id="resourceId" name="resourceId" value="${param.resourceId }">
	<div class="easyui-tabs"  style="width: 550px; height: 400px;margin: 10px auto;"><!-- height: 360px -->
		<div title="常规" style="padding: 5px" >
			<table>
				<tr>
					<td>名称:</td>
					<td><input class="easyui-textbox" type="text" id="name" name="name"  style="width: 375px" data-options="required:true,missingMessage:'必填'"/></td>
				</tr>
				<tr>
					<td>运营状态:</td>
					<td>
						<input class="easyui-textbox" type="text" id="entityDocStatusName" name="entityDocStatusName" style="width: 300px" readonly="true"/>
						<a href="#" class="easyui-linkbutton" style="height: 20px;" onclick="showStatus()">选择</a>
						<a href="#" class="easyui-linkbutton" style="height: 20px;" onclick="clearData('entityDocStatusName')">清除</a>
					</td>
				</tr>
				<tr style="height: 200px;">
					<td>描述:</td>
					<td >
						<input id="description" name="description" class="easyui-textbox" data-options="multiline:true,fit:true" />
					</td>
				</tr>	
			</table>
		</div>
		<div title="流转规则" style="padding: 5px">
			<table>
				<tr>
					<td>流转确认：</td>
					<td><input type="radio" id="isAdvanceConfirm_true"  name="isAdvanceConfirm" value="true" />是</td>
					<td><input type="radio" id="isAdvanceConfirm_false"  name="isAdvanceConfirm" value="false" checked="checked"/>否</td>
				</tr>
				<tr>
					<td>人员流转：</td>
					<td><input type="radio" id="isAdjustStaff_true" name="isAdjustStaff" value="true" />是</td>
					<td><input type="radio" id="isAdjustStaff_false" name="isAdjustStaff" value="false" checked="checked"/>否</td>
				</tr>
				<tr>
					<td>人员待阅：</td>
					<td><input type="radio" id="isToBeRead_true" name="isToBeRead" value="true" />是</td>
					<td><input type="radio" id="isToBeRead_false" name="isToBeRead" value="false" checked="checked"/>否</td>
				</tr>
			</table>
		</div>
		<div title="执行规则" style="padding: 5px">
			<table>
				<tr>
					<td>标题：</td>
					<td><input class="easyui-textbox" type="text" id="title" name="title" style="width: 375px" data-options="required:true,missingMessage:'必填'"/></td><!-- data-options="required:true" -->
				</tr>
				<tr>
					<td>表单类型：</td>
					<td>
						<select class="easyui-combobox" id="formType" name="formType" style="width: 375px" data-options="panelMaxHeight:75" ><!-- data-options="valueField:'value',textField:'text'" -->
							<option value="可编辑表单" selected="selected">可编辑表单</option>   
						    <option value="只读表单">只读表单</option>   
						    <option value="列表表单">列表表单</option>
						</select>
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
				<!-- <tr>
					<td>限办时间：</td>
					<td><input class="easyui-textbox" type="text" id="" name="" style="width: 375px"/></td>
				</tr>
				<tr>
					<td>时间单位:</td>
					<td>
						<input class="easyui-textbox" type="text" id="" name="" style="width: 295px"></input>
						<a href="#" class="easyui-linkbutton" style="height: 20px;">选择</a>
						<a href="#" class="easyui-linkbutton" style="height: 20px;">清除</a>
					</td>
				</tr> -->
			</table>	
			<div class="easyui-tabs" id="secondTabs" name="secondTabs" style="width: 520px;height: 270px;"><!-- height: 170px; -->
				<div title="循环策略" style="padding: 5px">
					<table>
						<tr>
							<td>循环策略：</td>
							<td>
								<select class="easyui-combobox" id="loopStrategy" name="loopStrategy" style="width: 350px" data-options="panelMaxHeight:75">
									<option value="10" selected="selected">重做：由上次处理人重新处理</option>   
								    <option value="20">忽略：流程中只执行一次</option>   
								    <option value="0">无：每次重新分配处理人</option>
								</select>
							</td>
						</tr>
						
					</table>
				</div>
				<div title="任务协作" style="padding: 5px">
					<table>
						<tr>
							<td>分配模式：</td>
							<td>
								<select class="easyui-combobox" id="assignmentStrategy" name="assignmentStrategy" style="width: 350px" data-options="panelMaxHeight:75">
									<option value="10" selected="selected">会签</option>   
								    <option value="30">抢办</option>   
								</select>
							</td>
						</tr>
						<tr>
							<td>支持批量：</td>
							<td>
								<input type="checkbox" id="batchApproval" name="batchApproval" >
								<!-- <select class="easyui-combobox" id="batchApproval" name="batchApproval" style="width: 350px" data-options="panelMaxHeight:75">
								    <option value="false" selected="selected">禁止</option>
									<option value="true" >允许</option>   
								</select> -->
							</td>
						</tr>
					</table>
				</div>
				<div title="执行者">
					<div class="easyui-layout" data-options="fit:true">   
					    <div data-options="region:'north'" style="height:30px;padding: 2px;">
					    	执行者分配方式：
							<input type="radio" name="actorAssignType" id="actorAssignType_10" value="10" checked="checked" onclick="selectSplitTabs(this,'targetTab')" checkedvalue="0,1,2,3">指定人员
							<!-- <input type="radio" name="actorAssignType" id="actorAssignType_20" value="20">按表达式 -->
							<input type="radio" name="actorAssignType" id="actorAssignType_30" value="30" onclick="selectSplitTabs(this,'targetTab')" checkedvalue="4">按SQL
							<input type="radio" name="actorAssignType" id="actorAssignType_40" value="40" onclick="selectSplitTabs(this,'targetTab')" checkedvalue="5">按Restful接口
					    </div>   
					    <div data-options="region:'center'">
					    	<div id="targetTab" class="easyui-tabs" data-options="width:500,fit:true,border:false" >   <!-- style="width: 420px;height: 170px;" -->
							    <div title="人员">   
							        <div id="user_tb">
					    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addUser()" data-options="iconCls:'icon-add'">新增</a>
					    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delRow('user')" data-options="iconCls:'icon-remove'">删除</a>
					    			</div>
					    			<table class="easyui-datagrid" id = "user"
								        data-options="
									        fit:true,
									        toolbar:'#user_tb',
									        fitColumns:true,
									        rownumbers:true,
									        striped:true,
									        border:false">   
									    <thead>
									        <tr>
									            <th data-options="field:'id',align:'center',checkbox:true,width:100">主键</th>
									            <th data-options="field:'name',width:100,halign:'center'">姓名</th>
									            <!-- <th data-options="field:'userName',width:60,halign:'center'">用户名</th> -->   
							        		</tr>
									    </thead>
									</table>
							    </div>   
							    <div title="角色">
							        <div id="role_tb">
					    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addRole('role')" data-options="iconCls:'icon-add'">新增</a>
					    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delRow('role')" data-options="iconCls:'icon-remove'">删除</a>
					    			</div>
					    			<table class="easyui-datagrid" id="role"
								        data-options="
									        fit:true,
									        toolbar:'#role_tb',
									        fitColumns:true,
									        rownumbers:true,
									        striped:true,
									        border:false">   
									    <thead>
									        <tr>
									            <th data-options="field:'id',halign:'center',checkbox:true">主键</th>
												<th data-options="field:'name',halign:'center',width:100">角色名称</th>
												<!-- <th data-options="field:'rolecode',halign:'center',width:60">角色编码</th> -->
									        </tr>
									    </thead>
									</table>    
							    </div>   
							    <div title="部门">   
							        <div id="org_tb">
					    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addOrg()" data-options="iconCls:'icon-add'">新增</a>
					    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delRow('org')" data-options="iconCls:'icon-remove'">删除</a>
					    			</div>
					    			<table class="easyui-datagrid" id="org"
								        data-options="
								        	fit:true,
								        	toolbar:'#org_tb',
								        	fitColumns:true,
									        rownumbers:true,
									        striped:true,
									        border:false">   
									    <thead>
									        <tr>
									            <th data-options="field:'id',halign:'center',checkbox:true">主键</th>
												<th data-options="field:'name',halign:'center',width:100">部门名称</th>
												<!-- <th data-options="field:'name',halign:'center',width:60">部门编码</th> -->
									        </tr>
									    </thead>
									</table>
							    </div>
							    
							    <div title="部门角色">   
							        <div id="org_role_tb">
						        		<div>
							        		 <div>
							    				 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="addRole('orgrole')" data-options="iconCls:'icon-add'">新增</a>
							    				 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="delRow('orgrole')" data-options="iconCls:'icon-remove'">删除</a>
							    				 <font style="color:BLUE"> 部门来源 </font>
							    				 <select id="departmentSrc" name="departmentSrc" class="easyui-combobox" style="width:100px;" data-options="editable:false,panelMaxHeight:100,onChange:departSrc">   
												    <option value="">----------------</option>   
												    <option value="60">上一步处理人</option>
												    <option value="70">指定部门</option>   
												    <option value="80">自定义sql</option>
												</select>
												是否用于其它节点<input type="checkbox" id="allNode" name="allNode">
											 </div>
											 <div id="srcId" name="srcId" style="display:none">
												<input class="easyui-textbox" id="conditionName" name="conditionName">
												<input type="hidden" id="conditionId" name="conditionId">
											 </div>
										</div>
				    				</div>
				    				<table class="easyui-datagrid" id="orgrole"
							        	data-options="
							        	fit:true,
							        	toolbar:'#org_role_tb',
							        	fitColumns:true,
								        rownumbers:true,
								        striped:true,
								        border:false">   
									    <thead>
									        <tr>
									            <th data-options="field:'id',halign:'center',checkbox:true">主键</th>
												<th data-options="field:'name',halign:'center',width:100">部门名称</th>
												<!-- <th data-options="field:'name',halign:'center',width:60">部门编码</th> -->
									        </tr>
									    </thead>
									</table>
							    </div>
							    
							    <div title="SQL条件" style="padding: 5px">
							    	<table>
							    		<tr>
											<td>sql条件：</td>
											<td>
												<input class="easyui-textbox" id="sqlKey" name="sqlKey" data-options="multiline:true" style="width: 350px; height:100px;"></input>
											</td>	
										</tr>
							    	</table>
							    </div>
							     
							    <div title="Restful接口" style="padding: 5px">
							    	<table>
							    		<tr>
											<td>Rest接口：</td>
											<td>
												<input class="easyui-textbox" id="restKey" name="restKey" data-options="multiline:true" style="width: 350px; height:100px;"></input>
											</td>	
										</tr>
							    	</table>
							    </div> 
							</div>
					    </div>   
					</div>
					
				</div>
				
				<div title="高级扩展" style="padding: 5px">
					<table>
						<tr>
							<td>任务实例创建器:</td>
							<td>
								<input class="easyui-textbox" type="text" id="" name="" style="width: 290px;"></input>
							</td>	
						</tr>
						<tr>
							<td>任务实例运行器:</td>
							<td>
								<input class="easyui-textbox" type="text" id="" name="" style="width: 290px;"></input>
							</td>	
						</tr>
						<tr>
							<td>任务实例的终结评价器:</td>
							<td>
								<input class="easyui-textbox" type="text" id="" name="" style="width: 290px;"></input>
							</td>	
						</tr>
						
					</table>
				</div>
			</div>
		</div>
		<div title="回退规则" style="padding: 5px">
			<table>
				<tr>
					<td>回退确认：</td>
					<td>
						<input type="radio" id="isBackConfirm_true"  name="isBackConfirm" value="true" checked="checked"/>是
						<input type="radio" id="isBackConfirm_false"  name="isBackConfirm" value="false"/>否
					</td>
				</tr>
				<!-- <tr>
					<td>返回方式：</td>
					<td>
						<select class="easyui-combobox" id="returnMode" name="returnMode" style="width: 375px" data-options="valueField:'value',textField:'text',required:true">
							
						</select>
					</td>
				</tr> -->
				<tr>
					<td>回退范围：</td>
					<td>
						<select class="easyui-combobox" id="backRange" name="backRange" style="width: 375px" data-options="valueField:'value',textField:'text'">	   
						</select>
					</td>
				</tr>
			</table>
		</div>
		<div title="字段权限">
			<div id="fieldAuthority_toolbar">
				<label>字段编码：</label>
				<input id="fieldAuthority_condition" class="easyui-textbox" data-options="buttonIcon:'icon-search',buttonText:'搜索',onClickButton:searchField" style="width:200px"> 
				|
				<a href="#" onclick="addOneRow('fieldAuthority')" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
				<a href="#" onclick="delRow('fieldAuthority')" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</div>
			<table id="fieldAuthority">
				
			</table>
		</div>
		<div title="按钮选择">
			<div id="pageAction_toolbar">
				<a href="#" onclick="addPageAction()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
				<a href="#" onclick="editPageAction()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
				<a href="#" onclick="delRow('pageAction')" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</div>
			<table id="pageAction">
				
			</table>
		</div>
		<div title="事件" style="padding: 5px">
			<div style="padding:  10px;display: inline-flex;">
				<label>启动：</label>
				<input id="callBackStatus_0" name="callBackStatus" type="radio" value="0" checked="checked" onclick="selectSplitTabs(this, 'tt')" checkedvalue="20">无
		        <input id="callBackStatus_10" name="callBackStatus" type="radio" style="margin-left: 20px;" value="10" onclick="selectSplitTabs(this, 'tt')" checkedvalue="0">本地
		        <input id="callBackStatus_20" name="callBackStatus" type="radio" style="margin-left: 20px;" value="20" onclick="selectSplitTabs(this, 'tt')" checkedvalue="1">远程
	        </div>
	        <div id="tt" class="easyui-tabs" style="width:445px ;height: 280px;">
	        	<div title="本地方法" style="padding: 20px;">
	        		<table>
	        			<tr>
	        				<td>方法路径：</td>
	        				<td><input class="easyui-textbox" type="text" id="localAddress" name="localAddress" style="width: 330px" /></td>
	        			</tr>
	        		</table>
	        	</div>
	        	<div title="远程服务" style="padding: 20px;">
	        		<table>
	        			<tr>
	        				<td>服务地址：</td>
	        				<td><input class="easyui-textbox" type="text" id="restfulAddress" name="restfulAddress" style="width: 330px" /></td>
	        			</tr>
	        		</table>
	        	</div>
	        </div>
		</div>
		<div title="短信" style="padding: 5px">
			<table>
				<tr>
					<td>是否启用：
						<input type="radio" id="isSendMsg_true"  name="isSendMsg" value="true" />是
						<input type="radio" id="isSendMsg_false"  name="isSendMsg" value="false" checked="checked"/>否
					</td>
				</tr>
	    		<tr>
					<td>短信模板：</td>
				</tr>
				<tr>
					<td>
						<input id="msgtTemplate" name="msgtTemplate" class="easyui-textbox" style="width: 455px;height: 280px;"  data-options="multiline:true"/>
					</td>
				</tr>
	    	</table>
		</div>
	</div>
	</form>
	<!-- <div style="text-align: center; padding: 5px">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">取消</a>
	</div> -->
	<div id="dlg"></div>
	<script type="text/javascript">
	
	function selectOrg() {
		$("<div></div>").dialog({
			id:'addOrgDialog',
			title:'添加部门',
			iconCls:'icon-save',
		    width:480,
		    height:350,
		    modal:true,
		    maximizable:true,
		    href: '<%=path%>/editor-app/mypages/addOrg.jsp',
		    onClose:function(){
		    	$("#addOrgDialog").dialog('destroy');
		    },
		    onLoad: function() {
		    	$("#resultGrid").treegrid({singleSelect:true});
		    },
		    buttons:[{
	    		text:'确定',
	    		iconCls:'icon-ok',
				handler:function(){
					var record = $("#resultGrid").treegrid("getSelected");
					if(null == record) {
						$.messager.alert('提示','请选中一条数据!','info');
						return ;
					}
					$("#conditionName").textbox("setValue",record.name);
					$("#conditionId").val(record.id);
					$("#addOrgDialog").dialog('destroy');
				}
	    	},{
	    		text:'取消',
	    		iconCls:'icon-cancel',
				handler:function(){
					$("#addOrgDialog").dialog('destroy');
				}
	    	}]
		})
	}
	
	function departSrc(newValue,oldValue) {
		$("#srcId").hide();
		var obj = $("#conditionName");
		if(70 == newValue) {
			$("#srcId").show();
			$(obj).textbox({multiline:false,width:264,editable:false,height:25,icons:[{iconCls:'icon-search',handler:selectOrg}],prompt:'请选择指定部门'});
			if(80 == oldValue) {
				window.conditionName80 = $(obj).textbox("getValue");
			}
			$(obj).textbox("setValue",window.conditionName70);
		}else if(80 == newValue) {
			$("#srcId").show();
			$(obj).textbox({multiline:true,editable:true,width:500,height:50,icons:[],prompt:'示例：select org_id from sys_user_org where user_id = (select xmfzrid from ia_sjxm where id = %s )'});
			if(70 == oldValue) {
				window.conditionName70 = $(obj).textbox("getValue");
			}
			$(obj).textbox("setValue",window.conditionName80);
		}else{
			if(80 == oldValue) {
				window.conditionName80 = $(obj).textbox("getValue");
			}if(70 == oldValue) {
				window.conditionName70 = $(obj).textbox("getValue");
			}
		}
	}
	
	//添加用户
	function addUser(){
		$("<div></div>").dialog({
			id:'addUserDialog',
			title:'添加用户',
			iconCls:'icon-save',
		    width:480,
		    height:350,
		    modal:true,
		    maximizable:true,
		    href: '<%=path%>/editor-app/mypages/addUser.jsp',
		    onClose:function(){
		    	$("#addUserDialog").dialog('destroy');
		    },
		    buttons:[{
	    		text:'确定',
	    		iconCls:'icon-ok',
				handler:function(){
					var resultRows = $("#resultGrid").datagrid("getSelections");
					var userRows = $("#user").datagrid("getRows");
					$.each(resultRows, function(resultIndex,resultRow){
						var isExist = false;
						$.each(userRows, function(userIndex, userRow){
							if(userRow.id == resultRow.id){
								isExist = true;
							}
						})
						if(!isExist){
							$("#user").datagrid("appendRow", resultRow);
						}
					})
					$("#addUserDialog").dialog('destroy');
				}
	    	},{
	    		text:'取消',
	    		iconCls:'icon-cancel',
				handler:function(){
					$("#addUserDialog").dialog('destroy');
				}
	    	}]
		})
	}
	//添加角色
	function addRole(objid){
		$("<div></div>").dialog({
			id:'addRoleDialog',
			title:'添加角色',
			iconCls:'icon-save',
		    width:480,
		    height:350,
		    modal:true,
		    maximizable:true,
		    href: '<%=path%>/editor-app/mypages/addRole.jsp',
		    onClose:function(){
		    	$("#addRoleDialog").dialog('destroy');
		    },
		    buttons:[{
	    		text:'确定',
	    		iconCls:'icon-ok',
				handler:function(){
					addResult($("#"+objid),$("#resultGrid"))
					$("#addRoleDialog").dialog('destroy');
				}
	    	},{
	    		text:'取消',
	    		iconCls:'icon-cancel',
				handler:function(){
					$("#addRoleDialog").dialog('destroy');
				}
	    	}]
		})
	}
	//添加部门
	function addOrg(){
		$("<div></div>").dialog({
			id:'addOrgDialog',
			title:'添加部门',
			iconCls:'icon-save',
		    width:480,
		    height:350,
		    modal:true,
		    maximizable:true,
		    href: '<%=path%>/editor-app/mypages/addOrg.jsp',
		    onClose:function(){
		    	$("#addOrgDialog").dialog('destroy');
		    },
		    buttons:[{
	    		text:'确定',
	    		iconCls:'icon-ok',
				handler:function(){
					addResult($("#org"),$("#resultGrid"));
					$("#addOrgDialog").dialog('destroy');
				}
	    	},{
	    		text:'取消',
	    		iconCls:'icon-cancel',
				handler:function(){
					$("#addOrgDialog").dialog('destroy');
				}
	    	}]
		})
	}
	//将选择结果回填到grid中
	function addResult(target,result){
		var resultRows = result.datagrid("getSelections");
		var targetRows = target.datagrid("getRows");
		$.each(resultRows, function(resultIndex,resultRow){
			var isExist = false;
			$.each(targetRows, function(targetIndex, targetRow){
				if(targetRow.id == resultRow.id){
					isExist = true;
				}
			})
			if(!isExist){
				target.datagrid("appendRow", resultRow);
			}
		})
	}
	//删除grid中的一行
	function delRow(id){
		var gridObj = $("#"+id);
		var rows = gridObj.datagrid("getSelections");
		$.each(rows, function(i, row){
			var index = gridObj.datagrid("getRowIndex", row);
			gridObj.datagrid("deleteRow", index);
		})
	}
	
	//添加表单
	function addForm(){
		$("<div></div>").dialog({
			id:'addFormDialog',
			title:'添加表单',
			iconCls:'icon-save',
		    width:480,
		    height:350,
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
	
	//初始化字段权限grid
	function initFieldAuthorityGrid(){
		$("#fieldAuthority").datagrid({
			fit:true,
			fitColumns:true,
			border:false,
			rownumbers:true,
			toolbar:'#fieldAuthority_toolbar',
			columns:[[
					{field:'ck', title:'复选框',checkbox:true},
					{field:'code', title:'字段编码', width:80, 
						editor : {
						    type : 'validatebox',
						    options : {
						       required : true
						    }
						}	
					},
					{field:'isVisible', title:'是否可见', width:80, 
						editor:{    
					        type:'checkbox',    
					        options:{    
					            on: 1,    
					            off: 0    
					        }    
					    },
					    formatter:checkboxFormatter
					},
					{field:'isRead', title:'是否只读', width:80, 
						editor:{    
					        type:'checkbox',    
					        options:{    
					            on: 1,    
					            off: 0   
					        },
					    },
						formatter:checkboxFormatter
					}
          	]],
          	onClickRow:editOnRow,
		
		})
	}
	//添加一行
	function addOneRow(id){
		var gridObj = $("#"+id);
		if(validateAllRow(gridObj)){
			gridObj.datagrid("acceptChanges");
			var length = gridObj.datagrid("getRows").length;
			gridObj.datagrid("appendRow",{});
			gridObj.datagrid("beginEdit",length);
		}
	}
	
	//编辑一行
	function editOnRow(index, row){
		var gridObj = $("#fieldAuthority");
		gridObj.datagrid("acceptChanges");
		if(validateAllRow(gridObj)){
			gridObj.datagrid("beginEdit",index);
		}
	}
	
	//验证编辑状态是否合法
	function validateAllRow(obj){
		var flag = true;
		var rows = obj.datagrid("getRows");
		$.each(rows, function(index,row){
			flag = obj.datagrid("validateRow",index);
			return flag;
		})
		return flag;
	}
	
	//复选框格式化函数
	function checkboxFormatter(value, row, index) {
		if (value ==this.editor.options.on){
			return '是';
		}else{
			return '否';
		}
		
	}
	
	//初始化按钮权限grid
	function initPageActionGrid(){
		$("#pageAction").datagrid({
			fit:true,
			fitColumns:true,
			border:false,
			rownumbers:true,
			toolbar:'#pageAction_toolbar',
			columns:[[
					{field:'ck', title:'复选框',checkbox:true},
					{field:'buttonName', title:'按钮名称', width:80, 
						editor : {
						    type : 'validatebox',
						    options : {
						       required : true
						    }
						}	
					},
					{field:'methodName', title:'事件名称', width:80, 
						editor : {
						    type : 'validatebox',
						    options : {
						       required : true
						    }
						}	
					},
          	]],
          	//onClickRow:editOnRow_pageAction,
		
		})
		
	}
	//添加按钮选择
	function addPageAction(){
		$("<div></div>").dialog({
			id:'pageActionDialog',
			title: '添加按钮',
		    width: 400,
		    height: 230,
		    href: '<%=path%>/editor-app/mypages/addPageAction.jsp',
		    modal: true,
		    onClose:function(){
		    	$('#pageActionDialog').dialog('destroy');
		    },
		    onOpen:function(){
		    	
		    },
		    buttons:[{
				text:'保存',
				handler:function(){
					var flag = $("#addPageActionForm").form("validate");
					if(flag){
						var gridObj = $("#pageAction");
						gridObj.datagrid("appendRow",{
							buttonName:$("#buttonName").val(),
							methodName:$("#methodName").val()
						});
						$('#pageActionDialog').dialog('destroy');
					}
				}
			},{
				text:'取消',
				handler:function(){
					$('#pageActionDialog').dialog('destroy');
				}
			}]
		})
	}
	//修改按钮选择
	function editPageAction(){
		var gridObj = $("#pageAction");
		var rows = gridObj.datagrid("getSelections");
		if(rows.length != 1){
			$.messager.alert('提示','请选中一条数据!','info');
			return ;
		}
		$("<div></div>").dialog({
			id:'pageActionDialog',
			title: '编辑按钮',
		    width: 440,
		    height: 230,
		    href: '<%=path%>/editor-app/mypages/addPageAction.jsp',
		    modal: true,
		    onClose:function(){
		    	$('#pageActionDialog').dialog('destroy');
		    },
		    onLoad:function(){
		    	$("#buttonName").textbox("setValue",rows[0].buttonName);
				$("#methodName").textbox("setValue",rows[0].methodName);
		    },
		    buttons:[{
				text:'保存',
				handler:function(){
					var flag = $("#addPageActionForm").form("validate");
					if(flag){
						var i = gridObj.datagrid("getRowIndex",rows[0]);
						gridObj.datagrid("updateRow",{
							index: i,
							row:{
								buttonName:$("#buttonName").val(),
								methodName:$("#methodName").val()
							}
						});
						$('#pageActionDialog').dialog('destroy');
					}
				}
			},{
				text:'取消',
				handler:function(){
					$('#pageActionDialog').dialog('destroy');
				}
			}]
		})
	}
	
	//字段权限模糊搜索
	function searchField(){
		var gridObj = $("#fieldAuthority");
		gridObj.datagrid("acceptChanges");
		gridObj.datagrid("unselectAll");
		var filedParam = $("#fieldAuthority_condition").textbox("getValue");
		var rows = gridObj.datagrid("getRows");
		$.each(rows ,function(index, row){
			if(row.code.indexOf(filedParam)!= -1){
				gridObj.datagrid("selectRow", index);
			}
		})
	}
	</script>
</body>
</html>