$(function () {
	
	var params = getJSONParameterJsp();
	$.ajax({
		type: "post",
		url: rootpath+"/iaSjxmExt/queryGanttViewPageList",
		data: {
			"rows": params.rows,
			"page": params.page,
			"cyear":params.year,
			"cmonth":params.month,
			"xmmc": params.xmmc
		},
		beforeSend: function(){
			ajaxLoad();//请求ajax之前加图层遮罩
        },
        complete: function () {
        	ajaxDisLoad();//请求完成之后移除图层遮罩
        },
		success: function(data) {
			joinJSON(data);
			parent.window.setPageTotal(data.total);
		},
		
		error: function() {
			$.messager.alert('提示:','加载数据异常!','info');
		}
	}); 
});

//组装甘特图格式的数据
function joinJSON(data) {
	var ganttArray = [];
	$(data.rows).each(function(){
		var seriesArray = [];
		//组装计划时间数据格式
		seriesArray.push({"name":"计划时间","start":new Date(this.startyear,this.startmonth,this.startday),"end": new Date(this.endyear,this.endmonth,this.endday),color: "#84C1FF"});
		
		//组装已用时间数据格式
		setSjDate(this,seriesArray);
		
		gantt = {"id":this.id, "name":this.name,"series":seriesArray}
		ganttArray.push(gantt);
	})
	
	setGtView(ganttArray);
}

//组装已用时间格式数据
function setSjDate(row,seriesArray){
	if(undefined != row.sjstartyear){
		var series = {"name":"已用时间"};
		series["start"] = new Date(row.sjstartyear,row.sjstartmonth,row.sjstartday);
		series["sjstartyear"] = row.sjstartyear;
		series["status"] = row.status; 
		series["sfzjzxm"] = row.sfzjzxm; 
		//获取计划时间天数
		var jhdays = daysBetween(row.xmksrq, row.xmjsrq) + 1;
		var endtime;
		//判断当前项目是否已经结束，有结束时间，说明此项目已经结束，否则是进行中的项目
		if(undefined != row.sjendyear)
			endtime = new Date(row.sjendyear,row.sjendmonth,row.sjendday);
		else
			endtime = new Date(row.dqendyear,row.dqendmonth,row.dqendday);		
		
		series["end"] = endtime;
		
		//设置结束时间值
		setEndTimeValue(row, series);
		
		seriesArray.push(series);
	}
}

//判断实际结束时间是否大于计划最大时间 判断是否处于逾期状态的项目 颜色红色的话 是逾期项目
function setEndTimeValue(row, series) {
	var endtime = row.xmsjjsrq;
	
	if(undefined == endtime)
		endtime = row.xmdqrq;
	
	var subject = daysBetween(row.xmjsrq, endtime);
	
	if(subject > 0)
		series["color"] = "#ff7575";
	else
		series["color"] = "#A6FFA6";
}

//调用甘特图进行数据的渲染
function setGtView(ganttData) {
	$("#ganttChart").empty();
	if(null != ganttData && ganttData.length >0){
		$("#ganttChart").ganttView({ 
			data: ganttData,
			slideWidth: "100%"
			/* ,behavior: {
				onClick: function (data) { 
					var msg = "You clicked on an event: { start: " + data.start.toString("M/d/yyyy") + ", end: " + data.end.toString("M/d/yyyy") + " }";
					$("#eventMessage").text(msg);	
				},
				onResize: function (data) { 
					var msg = "You resized an event: { start: " + data.start.toString("M/d/yyyy") + ", end: " + data.end.toString("M/d/yyyy") + " }";
					$("#eventMessage").text(msg);	
				},
				onDrag: function (data) { 
					var msg = "You dragged an event: { start: " + data.start.toString("M/d/yyyy") + ", end: " + data.end.toString("M/d/yyyy") + " }";
					$("#eventMessage").text(msg);	
				}
			} */
		});
	}
}

//获取两个日期之间的天数
function daysBetween(start, end) {
	if (!start || !end) {
		return 0;
	}

	start = Date.parse(start);
	end = Date.parse(end);
	if (start.getYear() == 1901 || end.getYear() == 8099) {
		return 0;
	}

	var count = 0, date = start.clone();
	while (date.compareTo(end) == -1) {
		count = count + 1;
		date.addDays(1);
	}
	return count;
}