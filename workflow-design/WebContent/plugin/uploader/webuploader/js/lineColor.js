
var lineH = 0;  // 线型

$(document).ready(function(){
//	$('#cpDiv').ColorPicker({
//		flat: true,
//		color: '#a5a5a5',
//		/*onShow: function (colpkr) {
//			$(colpkr).fadeIn(500);
//			return false;
//		},
//		onHide: function (colpkr) {
//			$(colpkr).fadeOut(500);
//			return false;
//		},*/
//		onBeforeShow: function () {
//			var  colors = [];
//			for(var n=0;n<arr_selected_id.length;n++)
//			{
//				var pageNo = parseInt(arr_selected_id[n].substr(5,1));
//				var row = parseInt(arr_selected_id[n].substr(7,1));
//				var col = parseInt(arr_selected_id[n].substr(9,1));
//				color = page[pageNo-1].rowx[row].grid[col].backgroundColor;
//				if(color != null && $.inArray(color,colors) == -1){
//					colors.push(color);
//				}
//			}
//			if (colors.length == 1) {
//				$(this).ColorPickerSetColor(colors[0] == "" ? '0000ff' : colors[0]);
//			} else if (colors.length > 1) {
//				$(this).ColorPickerSetColor('FFFFFF');
//			}
//		},
//		onChange: function (hsb, hex, rgb) {
//			var color = '#' + hex;
//			$('.line').css('border-top-color', color);
//		}
//	});
	/*$('#cpDiv').colorpicker({color:'#a5a5a5',
		history: false,
		strings: '色(S), , , , .'});

	$("#cpDiv").on("change.color", function(event, color){
	    $('.line').css('background-color', color);
	});*/
});

function closeLineColor(){
	if(!closeFlag){
		$("#colorSelect").hide();
	}
}

function openLineColor(top,left,callback)
{
	lineH = 0;

	//$("#cpDiv").colorpicker("val",'#a5a5a5');
	//$('#cpDiv').ColorPickerShow();
	//$('#cpDiv').show();
	var  colors = [];
	for(var n=0;n<arr_selected_id.length;n++)
	{
		var pageNo = parseInt(arr_selected_id[n].substr(5,1));
		var row = parseInt(arr_selected_id[n].substr(7,1));
		var col = parseInt(arr_selected_id[n].substr(9,1));
		var color1 = page[pageNo-1].rowx[row].grid[col].borderTopColor;
		if(color1 != null && color1 != '' && $.inArray(color1,colors) == -1){
			colors.push(color1);
		}
		var color2 = page[pageNo-1].rowx[row].grid[col].borderBottomColor;
		if(color2 != null && color2 != '' && $.inArray(color2,colors) == -1){
			colors.push(color2);
		}
		var color3 = page[pageNo-1].rowx[row].grid[col].borderLeftColor;
		if(color3 != null && color3 != '' && $.inArray(color3,colors) == -1){
			colors.push(color3);
		}
		var color4 = page[pageNo-1].rowx[row].grid[col].borderRightColor;
		if(color4 != null && color4 != '' && $.inArray(color4,colors) == -1){
			colors.push(color4);
		}
	}
	if (colors.length == 1) {
		var selColor = colors[0] == "" ? '#0000ff' : colors[0];
		selectColor($("#cpDiv").find('div[key=' + selColor + ']').eq(0), selColor);
	} else if (colors.length > 1) {
		$("#cpDiv").attr('selectColor', '#FFFFFF');
	} else {
		$("#cpDiv").attr('selectColor', '#a5a5a5');
	}

	var lineColor = $("#cpDiv").attr('selectColor') ;//'#' + $("#cpDiv div.colorpicker_hex :text").val();

	$('.line').css('border-top-color', lineColor);

	$('.line').parent().removeClass('selected');
	$('.line').parent().removeClass('lineOver');
	$('.line').eq(0).parent().click();

	$('#previewDiv').css('border','1px dotted #a5a5a5');

	$('#previewDiv1').css('border-left','1px dotted #a5a5a5');

	$('#previewDiv2').css('border-top','1px dotted #a5a5a5');

	$('#previewDiv').attr('lineH1','0');
	$('#previewDiv').attr('lineColor1','');
	$('#previewDiv').attr('lineWidth1',0);
	$('#previewDiv').attr('lineH2','0');
	$('#previewDiv').attr('lineColor2','');
	$('#previewDiv').attr('lineWidth2',0);
	$('#previewDiv').attr('lineH3','0');
	$('#previewDiv').attr('lineColor3','');
	$('#previewDiv').attr('lineWidth3',0);
	$('#previewDiv').attr('lineH4','0');
	$('#previewDiv').attr('lineColor4','');
	$('#previewDiv').attr('lineWidth4',0);

	$('#previewDiv1').attr('lineH','0');
	$('#previewDiv1').attr('lineColor','');
	$('#previewDiv1').attr('lineWidth',0);

	$('#previewDiv2').attr('lineH','0');
	$('#previewDiv2').attr('lineColor','');
	$('#previewDiv2').attr('lineWidth',0);

	$('#colorSelect').css('top',top + 'px');
	$('#colorSelect').css('left',left + 'px');

	$('#colorSelect').show();

	$("#colorSelect").find("#okBtn").unbind( "click" );
	$("#colorSelect").find("#cancelBtn").unbind( "click" );

	$("#colorSelect").find("#okBtn").click(function(){
		var result = do_ok();
		callback(result);
	});
	$("#colorSelect").find("#cancelBtn").click(function(){
		do_cancel()
	});
}

function lineMouseOver(obj){
	$(obj).addClass('lineOver');
}

function lineMouseOut(obj){
	if(!$(obj).hasClass('selected')){
		$(obj).removeClass('lineOver');
	}

}

function selectColor(obj, val){
	$('#cpDiv').find('div').removeClass('colorSelected');
	$(obj).addClass('colorSelected');

	$('.line').css('border-top-color', val);

	$("#cpDiv").attr('selectColor',val)
}

function selectLine(obj ,val){
	$(obj).parent().children().removeClass('selected');
	$(obj).parent().children().removeClass('lineOver');
	$(obj).addClass('selected');
	$(obj).addClass('lineOver');

	lineH = val;
}

function do_select(cmd){
	var lineColor,lineWidth,lineborderVal;
	if(lineH == 0){ // 如果无线型
		lineColor = '#a5a5a5';
		lineWidth = '1px';
		lineborderVal = lineWidth + ' dotted ' +  lineColor;
		//return;
	} else {
		lineColor = $("#cpDiv").attr('selectColor') ; //'#' + $("#cpDiv div.colorpicker_hex :text").val();//$("#cpDiv").colorpicker("val");
		lineWidth = $("#lineWidthDiv :radio:checked").val();
		lineborderVal = lineWidth + 'mm ' + lineH + ' ' +  lineColor;
	}
	if(lineColor == ''){
		lineColor = '#a5a5a5';
	}

	lineH = (lineH == 0 ? '0' : lineH);
	lineColor = (lineH == 0 ? '' : lineColor);

	//var lineColor = '#' + $("#cpDiv div.colorpicker_hex :text").val();//$("#cpDiv").colorpicker("val");
	//var lineborderVal = lineH + 'px solid ' +  lineColor;
	//var lineWidth = $("#lineWidthDiv :radio:checked").val();
	//var lineborderVal = lineWidth + 'mm ' + lineH + ' ' +  lineColor;

	var defaultBorder = '1px dotted #a5a5a5';
	if(cmd == 'lineNot'){
		$('#previewDiv').css('border','1px dotted #a5a5a5');

		$('#previewDiv1').css('border-left','1px dotted #a5a5a5');

		$('#previewDiv2').css('border-top','1px dotted #a5a5a5');

		$('#previewDiv').attr('lineH1','0');
		$('#previewDiv').attr('lineColor1','');
		$('#previewDiv').attr('lineWidth1',0);
		$('#previewDiv').attr('lineH2','0');
		$('#previewDiv').attr('lineColor2','');
		$('#previewDiv').attr('lineWidth2',0);
		$('#previewDiv').attr('lineH3','0');
		$('#previewDiv').attr('lineColor3','');
		$('#previewDiv').attr('lineWidth3',0);
		$('#previewDiv').attr('lineH4','0');
		$('#previewDiv').attr('lineColor4','');
		$('#previewDiv').attr('lineWidth4',0);

		$('#previewDiv1').attr('lineH','0');
		$('#previewDiv1').attr('lineColor','');
		$('#previewDiv1').attr('lineWidth',0);

		$('#previewDiv2').attr('lineH','0');
		$('#previewDiv2').attr('lineColor','');
		$('#previewDiv2').attr('lineWidth',0);
	}
	else if(cmd == 'lineOutAll'){
		$('#previewDiv').css('border',lineborderVal);
		var lineWidth = $("#lineWidthDiv :radio:checked").val();

		$('#previewDiv').attr('lineH1',lineH);
		$('#previewDiv').attr('lineColor1',lineColor);
		$('#previewDiv').attr('lineWidth1',lineWidth);
		$('#previewDiv').attr('lineH2',lineH);
		$('#previewDiv').attr('lineColor2',lineColor);
		$('#previewDiv').attr('lineWidth2',lineWidth);
		$('#previewDiv').attr('lineH3',lineH);
		$('#previewDiv').attr('lineColor3',lineColor);
		$('#previewDiv').attr('lineWidth3',lineWidth);
		$('#previewDiv').attr('lineH4',lineH);
		$('#previewDiv').attr('lineColor4',lineColor);
		$('#previewDiv').attr('lineWidth4',lineWidth);
	}
	else if(cmd == 'lineOut1'){
		if($('#previewDiv').attr('lineWidth1') == '0'){
			var lineWidth = $("#lineWidthDiv :radio:checked").val();
			$('#previewDiv').css('border-top',lineborderVal);
			$('#previewDiv').attr('lineH1',lineH);
			$('#previewDiv').attr('lineColor1',lineColor);
			$('#previewDiv').attr('lineWidth1',lineWidth);
		}
		else{
			$('#previewDiv').attr('lineH1','0');
			$('#previewDiv').attr('lineColor1','');
			$('#previewDiv').attr('lineWidth1',0);
			$('#previewDiv').css('border-top',defaultBorder);
		}

	}
	else if(cmd == 'lineOut2'){
		if($('#previewDiv').attr('lineWidth2') == '0'){
			var lineWidth = $("#lineWidthDiv :radio:checked").val();
			$('#previewDiv').css('border-bottom',lineborderVal);
			$('#previewDiv').attr('lineH2',lineH);
			$('#previewDiv').attr('lineColor2',lineColor);
			$('#previewDiv').attr('lineWidth2',lineWidth);
		}
		else{
			$('#previewDiv').attr('lineH2','0');
			$('#previewDiv').attr('lineColor2','');
			$('#previewDiv').attr('lineWidth2',0);
			$('#previewDiv').css('border-bottom',defaultBorder);
		}
	}
	else if(cmd == 'lineOut3'){
		if($('#previewDiv').attr('lineWidth3') == '0'){
			var lineWidth = $("#lineWidthDiv :radio:checked").val();
			$('#previewDiv').css('border-left',lineborderVal);
			$('#previewDiv').attr('lineH3',lineH);
			$('#previewDiv').attr('lineColor3',lineColor);
			$('#previewDiv').attr('lineWidth3',lineWidth);
		}
		else{
			$('#previewDiv').attr('lineH3','0');
			$('#previewDiv').attr('lineColor3','');
			$('#previewDiv').attr('lineWidth3',0);
			$('#previewDiv').css('border-left',defaultBorder);
		}
	}
	else if(cmd == 'lineOut4'){
		if($('#previewDiv').attr('lineWidth4') == '0'){
			var lineWidth = $("#lineWidthDiv :radio:checked").val();
			$('#previewDiv').css('border-right',lineborderVal);
			$('#previewDiv').attr('lineH4',lineH);
			$('#previewDiv').attr('lineColor4',lineColor);
			$('#previewDiv').attr('lineWidth4',lineWidth);
		}
		else{
			$('#previewDiv').attr('lineH4','0');
			$('#previewDiv').attr('lineColor4','');
			$('#previewDiv').attr('lineWidth4',0);
			$('#previewDiv').css('border-right',defaultBorder);
		}
	}
	else if(cmd == 'lineCenterAll'){
		var lineWidth = $("#lineWidthDiv :radio:checked").val();
		$('#previewDiv2').css('border-top',lineborderVal);

		$('#previewDiv1').css('border-left',lineborderVal);

		$('#previewDiv1').attr('lineH',lineH);
		$('#previewDiv1').attr('lineColor',lineColor);
		$('#previewDiv1').attr('lineWidth',lineWidth);

		$('#previewDiv2').attr('lineH',lineH);
		$('#previewDiv2').attr('lineColor',lineColor);
		$('#previewDiv2').attr('lineWidth',lineWidth);
	}
	else if(cmd == 'lineCenter1'){
		if($('#previewDiv2').attr('lineWidth') == '0'){
			var lineWidth = $("#lineWidthDiv :radio:checked").val();
			$('#previewDiv2').css('border-top',lineborderVal);

			$('#previewDiv2').attr('lineH',lineH);
			$('#previewDiv2').attr('lineColor',lineColor);
			$('#previewDiv2').attr('lineWidth',lineWidth);
		}
		else{
			$('#previewDiv2').attr('lineH','0');
			$('#previewDiv2').attr('lineColor','');
			$('#previewDiv2').attr('lineWidth',0);
			$('#previewDiv2').css('border-top',defaultBorder);
		}
	}
	else if(cmd == 'lineCenter2'){
		if($('#previewDiv1').attr('lineWidth') == '0'){
			var lineWidth = $("#lineWidthDiv :radio:checked").val();
			$('#previewDiv1').css('border-left',lineborderVal);

			$('#previewDiv1').attr('lineH',lineH);
			$('#previewDiv1').attr('lineColor',lineColor);
			$('#previewDiv1').attr('lineWidth',lineWidth);
		}
		else{
			$('#previewDiv1').attr('lineH','0');
			$('#previewDiv1').attr('lineColor','');
			$('#previewDiv1').attr('lineWidth',0);
			$('#previewDiv1').css('border-left',defaultBorder);
		}
	}
}

function do_ok(){
	/*var line1 = [$('#previewDiv').attr('lineH1'),$('#previewDiv').attr('lineColor1')];
	var line2 = [$('#previewDiv').attr('lineH2'),$('#previewDiv').attr('lineColor2')];
	var line3 = [$('#previewDiv').attr('lineH3'),$('#previewDiv').attr('lineColor3')];
	var line4 = [$('#previewDiv').attr('lineH4'),$('#previewDiv').attr('lineColor4')];
	var line5 = [$('#previewDiv2').attr('lineH'),$('#previewDiv2').attr('lineColor')];
	var line6 = [$('#previewDiv1').attr('lineH'),$('#previewDiv1').attr('lineColor')];*/

	// 数组元素说明：['线宽','颜色','线型',]
	var lineWidth = $("#lineWidthDiv :radio:checked").val();
	var line1 = [$('#previewDiv').attr('lineWidth1'),$('#previewDiv').attr('lineColor1'),$('#previewDiv').attr('lineH1')];
	var line2 = [$('#previewDiv').attr('lineWidth2'),$('#previewDiv').attr('lineColor2'),$('#previewDiv').attr('lineH2')];
	var line3 = [$('#previewDiv').attr('lineWidth3'),$('#previewDiv').attr('lineColor3'),$('#previewDiv').attr('lineH3')];
	var line4 = [$('#previewDiv').attr('lineWidth4'),$('#previewDiv').attr('lineColor4'),$('#previewDiv').attr('lineH4')];
	var line5 = [$('#previewDiv2').attr('lineWidth'),$('#previewDiv2').attr('lineColor'),$('#previewDiv2').attr('lineH')];
	var line6 = [$('#previewDiv1').attr('lineWidth'),$('#previewDiv1').attr('lineColor'),$('#previewDiv1').attr('lineH')];

	var result = [line1,line2,line3,line4,line5,line6];

	$('#colorSelect').hide();

	return result;
}

function do_cancel(){
	$('#colorSelect').hide();
}