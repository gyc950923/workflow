(function($) {
	
	/*监控页面数据是否发生变化*/
	$.fn.MonitorDataValueChange = function(options) {
		var tagNames = new Array('#plp','.plp');
		var tagNamesValue = new Array('input', 'select', 'textarea');
		var valueArray = new Array();

		var deafult = {
			arrTags: tagNames, //需监控控件的tagName属性数组
		};
		var ops = $.extend(deafult,options);
		
		for (var i = 0; i < ops.arrTags.length; i++) {
			for(var j = 0;j<tagNamesValue.length;j++){
				$(ops.arrTags[i]).find(tagNamesValue[j]).each(function() {
					valueArray.push($(this).val());
				});
			}
		}
		return valueArray;
	};
	/*返回页面数据是否发生变化*/
	$.fn.setValueIsChange = function(value) {
		pageDataChange = value;
	};
	
	/*返回页面数据是否发生变化*/
	$.fn.getValueIsChange = function(valueArrayOld) {
		var pageDataChange = false; //默认标识页面数据未发生改变
		var valueArrayNow = $.fn.MonitorDataValueChange();
		for(var i = 0;i< valueArrayOld.length;i++){
			var oldV = valueArrayOld[i];
			var nowV = valueArrayNow[i];
			
			if(oldV == null || oldV == "" || oldV == undefined){
				oldV = "";
			}
			if(nowV == null || nowV == "" || nowV == undefined){
				nowV = "";
			}
			if(oldV != nowV){
				pageDataChange = true;
				break;
			}
		}
		return pageDataChange;
	};
	
})(jQuery);