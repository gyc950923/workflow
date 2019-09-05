$(function() {
	$("table.search_list_single tbody tr").each(function(){
		var tr = $(this);
		addClick(tr);
	});
});

function addClick(obj){
	var targetObj = obj.children("td:first").find(":radio:enabled,:checkbox:enabled");
	if(targetObj.length > 0){
		targetObj.eq(0).click(function (event){event.stopPropagation()});
		obj.css("cursor","pointer");
		obj.click(function(){
			targetObj.eq(0).click();
		});
	}
}

function addDbClick(obj){
	var targetObj = obj.children("td:first").find(":radio:enabled,:checkbox:enabled");
	if(targetObj.length > 0){
		targetObj.eq(0).click(function (event){event.stopPropagation()});
		obj.css("cursor","pointer");
		obj.dblclick(function(){
			targetObj.eq(0).click();
		});
	}
}

/**
 * 画面迁移
 * @param url
 */
function goToPage(url){
	document.location.href = url;
}

/**
 *
 */
function showMessage(message){

}

/**
 * 等待画面
 */
function doWait() {
	var frameprogrssBarDiv = '<div id="frameprogrssBarDiv" style="position:absolute;top:32%;left:40%;width:5%;height:7%;display:none;z-index:1000"><img src="../main/img/process.gif" /></div>';
	$(frameprogrssBarDiv).appendTo($(window.document.body));
	$("#frameprogrssBarDiv").show();
}

/**
 * 隐藏等待
 */
function hideWait(){
	$("#frameprogrssBarDiv").remove();
}

/**
 * 共通ajax请求
 * @param url
 * @param params
 * @param callback
 * @param async
 */
function cmsAjax(url, params, callback, async) {
	$.ajax({
				type : "POST",
				data : params,
				dataType : "text",
				url : url,
				async : async,
				success : function(data) {

					var jsonObjects = jQuery.parseJSON(data);
					// ajaxエラー
					if (jsonObjects != null && jsonObjects.resultCode != null
							&& jsonObjects.resultCode != ""
							&& jsonObjects.resultCode == "9") {
						callback(null);
					} else {
						callback(jsonObjects.objData);
					}
				},
				error : function(data) {
//					$(".error").remove();

//					var jsonObjects = jQuery.parseJSON(data);
//					if (jsonObjects != null) {
//						var message = jsonObjects.message;
//						createBlockDiv(target, blockDivId, message);
//					}
					callback(null);
				},
				complete : function(data) {
					data = null
				}
			});
}

function dialogNoCopy(functionId ,callback){
	$("#dialog:ui-dialog").dialog( "destroy" );
	//show dialog
	$("#"+functionId).dialog({
		autoOpen: false,
		resizable: false,
		height:150,
		width:400,
		modal: true
	});
	$("#"+functionId).siblings('div.ui-dialog-titlebar').remove();
	$("#"+functionId).dialog( "open" );
	$("#"+functionId).find("#closeBtn").click(function(){
		$("#"+functionId).dialog( "close" );
	});
	$("#"+functionId).find("#saveBtn").click(function(){
		$("#"+functionId).dialog( "close" );
		callback("ok");
	});
	$("#"+functionId).find("#cancelBtn").click(function(){
		callback("cancel");
	});
}

function dialogNoCopy2(functionId ,callback){
	$("#dialog:ui-dialog").dialog( "destroy" );
	//show dialog
	$("#"+functionId).dialog({
		autoOpen: false,
		resizable: false,
		height:150,
		width:400,
		modal: true
	});
	$("#"+functionId).siblings('div.ui-dialog-titlebar').remove();
	//$("#"+functionId).dialog( "open" );
	$("#"+functionId).find("#closeBtn").click(function(){
		$("#"+functionId).dialog( "close" );
	});
	$("#"+functionId).find("#saveBtn").click(function(){
		$("#"+functionId).dialog( "close" );
		callback("ok");
	});
	$("#"+functionId).find("#cancelBtn").click(function(){
		callback("cancel");
	});
}

function bigDialogNoCopy(functionId ,callback){
	$("#dialog:ui-dialog").dialog( "destroy" );
	//show dialog
	$("#"+functionId).dialog({
		autoOpen: false,
		resizable: false,
		height:250,
		width:400,
		modal: true
	});
	$("#"+functionId).siblings('div.ui-dialog-titlebar').remove();
	$("#"+functionId).dialog( "open" );
	$("#"+functionId).find("#closeBtn").click(function(){
		$("#"+functionId).dialog( "close" );
	});
	$("#"+functionId).find("#saveBtn").click(function(){
		$("#"+functionId).dialog( "close" );
		callback("ok");
	});
	$("#"+functionId).find("#delBtn").click(function(){
		$("#"+functionId).dialog( "close" );
		callback("del");
	});
}


/**
 * エラーメッセージを明らかに示します
 * @param msgs
 * @param fieldIds
 * @returns
 */
function showFieldErrors(msgs,fieldIds){

		var errorStr = '<div class="error notification ajaxError" style="DISPLAY: block"><table class="error_msg_title_cms"><tbody>';
		$.each(msgs.split("|"),function(j,msg){
			if(msg != null && msg != ""){
				errorStr += '<tr><td>・' + msg + '</td></tr>';
			}
		});
		errorStr += '</tbody></table></div>';

		$(".error").remove();
		$(".top_title").after(errorStr);

		if(fieldIds != null && fieldIds != ""){
			$.each(fieldIds.split("|"),function(i,fieldId){
				if(fieldId != null && fieldId != ""){
					$("#"+fieldId).addClass("bg_error");
				}
			});
		}

		window.scrollTo(0,0);
}

/**
 * AJAXエラーメッセージを明らかに示します
 * @param msgs
 * @param fieldNames
 * @returns
 */
function showAjaxFieldErrors(msgs,fieldNames){

		var errorStr = '<div class="error notification ajaxError" style="DISPLAY: block"><table class="error_msg_title_cms"><tbody>';
		$.each(msgs.split("|"),function(j,msg){
			if(msg != null && msg != ""){
				errorStr += '<tr><td>・' + msg + '</td></tr>';
			}
		});
		errorStr += '</tbody></table></div>';

		$(".error").remove();
		$(".top_title").after(errorStr);

		if(fieldNames != null && fieldNames != ""){
			$.each(fieldNames.split("|"),function(i,fieldName){
				if(fieldName != null && fieldName != ""){
					//$("#"+fieldId).addClass("bg_error");
					var tag = document.getElementsByName(fieldName)[0];
					$(tag).addClass("bg_error");
				}
			});
		}

		window.scrollTo(0,0);
}

/**
 * エラーメッセージを削除します
 * @param fieldIds
 * @returns
 */
function removeError(fieldIds){
	$(".error").remove();
	$.each(fieldIds.split(","),function(i,fieldId){
		$("#"+fieldId).removeClass("bg_error");
	});
}

/**
 * 入力テキストを数値だけ入力許可
 */
function inputNumberFormat(){
    // 数値
    $(".numeric").numeric();
    // 整数
    $(".integer").numeric({ decimal: false });
    // 正の数値
    $(".positive").numeric({ negative: false });
    // 正の整数
    $(".positive-integer").numeric({ decimal: false, negative: false });
    // 整数４桁、小数２桁
    $(".decimal_4_2").numeric({ negative: false, integerPlaces : 4, decimalPlaces : 2 });
    // 整数４桁、小数３桁
    $(".decimal_4_3").numeric({ negative: false, integerPlaces : 4, decimalPlaces : 3 });
    $(".decimal_2").numeric({ negative: false, decimalPlaces : 2 });
    // *「右クリック貼り付け」対応
    $(".antirc").change(function() {
        $(this).keyup();
    });
}
