(function($) {
	var pageDataChange = false //默认标识页面数据未发生改变
	/*监控页面数据是否发生变化*/
	$.fn.MonitorDataChange = function(options) {
		var tagNames = new Array('#plp','.plp');

		var deafult = {
			arrTags: tagNames, //需监控控件的tagName属性数组
		};
		var ops = $.extend(deafult,options);

		for (var i = 0; i < ops.arrTags.length; i++) {
			$(ops.arrTags[i]).each(function() {
				$(this).bind('change', function() {
					pageDataChange = true;
				});
			});
		}
		return this;
	};
	/*返回页面数据是否发生变化*/
	$.fn.getValue = function() {
		return pageDataChange;
	};

	$.fn.setValue = function(value) {
		pageDataChange = value;
	};
})(jQuery);