
window.onload = initPage;
function initPage()
{
	adjustMargin();
	init();
	initTable();
}

function init(){
	$('table.search_list').each(function() 
    {
    	var table = $(this);
		$('tbody tr',table).mouseover(
				function () {
					var thisObj = $(this);
					var id = thisObj.index();
					if(!thisObj.find('td').find('intput[type=checkbox}').attr('checked')){
						thisObj.find('td').addClass('hoverRow');
						if(id%2==0){
					    	thisObj.next().find('td').addClass('hoverRow');
					 	}
					 	else{
					 		thisObj.prev().find('td').addClass('hoverRow');
					 	}
				 	}
				 }
				);

			$('tbody tr',table).mouseout(
				function () {
					var thisObj = $(this);
					thisObj.find('td').removeClass('hoverRow');
					var id = thisObj.index();
					if(thisObj.find('td').find('intput[type=checkbox}').attr('checked')){
						if(id%2==0){
					    	thisObj.next().find('td').addClass('hoverRow');
					 	}
					 	else{
					 		thisObj.prev().find('td').addClass('hoverRow');
					 	}
					}
				    else{
						if(id%2==0){
					    	thisObj.next().find('td').removeClass('hoverRow');
					 	}
					 	else{
					 		thisObj.prev().find('td').removeClass('hoverRow');
					 	}
				 	}
				 }
				);
	
	$('thead tr td.sortField',table).each(function()
        {
            $(this).click(function()
            {
            	var trIndex = $(this).parent().index();
            	var preSortTh = $('#sortThIndex').val();
            	var curSortThIndex = $(this).index();
            	if(preSortTh == ""){
	            	$(this).append("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            	    $('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            	}
            	else{
            		var sort = preSortTh.split('_');
            		var thIndex = sort[0];
            		var sortType = sort[1];
            		var curThIndex = trIndex + "/" +curSortThIndex;
            		if(thIndex == curThIndex){
            			if('0' == sortType){
            				$('img',this).replaceWith("<img src='../main/img/sort_asc_on.gif' class='sort_icon'/>");
            				$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '1');
            			}else{
            				$('img',this).replaceWith("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            			$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            			}
            		}else{
            			table.find('thead tr td img').remove();
            			$(this).append("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            	    	$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            		}
            	}
            });
    });
    
    });
    
	$('table.search_list_single').each(function() 
    {
    	var table = $(this);
		$('tbody tr',table).mouseover(
				function () {
					var evt = window.event || arguments.callee.caller.arguments[0];
					var td = evt.srcElement || evt.target;
					if(typeof(td.cellIndex)=="undefined") td = td.parentNode;
					if(td.parentNode.className.indexOf("totalTR")<0&&td.parentNode.className.indexOf("noMouseOverTR")<0){
						var thisObj = $(this);
						thisObj.find('td').addClass('hoverRow');
					}
				 }
				);

		$('tbody tr',table).mouseout(
			function () {
				var thisObj = $(this);
				thisObj.find('td').removeClass('hoverRow');
			 }
			);
	
	$('thead tr td.sortField',table).each(function()
        {
            $(this).click(function()
            {
            	var trIndex = $(this).parent().index();
            	var preSortTh = $('#sortThIndex').val();
            	var curSortThIndex = $(this).index();
            	if(preSortTh == ""){
	            	$(this).append("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            	    $('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            	}
            	else{
            		var sort = preSortTh.split('_');
            		var thIndex = sort[0];
            		var sortType = sort[1];
            		var curThIndex = trIndex + "/" +curSortThIndex;
            		if(thIndex == curThIndex){
            			if('0' == sortType){
            				$('img',this).replaceWith("<img src='../main/img/sort_asc_on.gif' class='sort_icon'/>");
            				$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '1');
            			}else{
            				$('img',this).replaceWith("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            			$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            			}
            		}else{
            			table.find('thead tr td img').remove();
            			$(this).append("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            	    	$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            		}
            	}
            });
    });
    
    });
    
	
	$('table.search_list_mik').each(function() 
    {
    	var table = $(this);
		$('tbody tr',table).mouseout(
			function () {
				var thisObj = $(this);
				thisObj.find('td').removeClass('hoverRow');
				
			 }
			);
	
	$('thead tr td.sortField',table).each(function()
        {
            $(this).click(function()
            {
            	var trIndex = $(this).parent().index();
            	var preSortTh = $('#sortThIndex').val();
            	var curSortThIndex = $(this).index();
            	if(preSortTh == ""){
	            	$(this).append("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            	    $('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            	}
            	else{
            		var sort = preSortTh.split('_');
            		var thIndex = sort[0];
            		var sortType = sort[1];
            		var curThIndex = trIndex + "/" +curSortThIndex;
            		if(thIndex == curThIndex){
            			if('0' == sortType){
            				$('img',this).replaceWith("<img src='../main/img/sort_asc_on.gif' class='sort_icon'/>");
            				$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '1');
            			}else{
            				$('img',this).replaceWith("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            			$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            			}
            		}else{
            			table.find('thead tr td img').remove();
            			$(this).append("<img src='../main/img/sort_desc_on.gif' class='sort_icon'/>");
            	    	$('#sortThIndex').val(trIndex+'/'+curSortThIndex + '_' + '0');
            		}
            	}
            });
    });
    
    });
	
	
 $('table.search_list_single_AUT003').each(function() 
    {
    	var table = $(this);
		$(this).click(
				function () {
					var thisObj = $(this);
					var id = thisObj.index();
					if(!thisObj.find('td').find('intput[type=checkbox}').attr('checked')){
						thisObj.find('td').addClass('hoverRow_003');
					 	//else{
					 	//	thisObj.prev().find('td').addClass('hoverRow');
					 	//}
				 	}
				 }
				);

			$(this).mouseout(
				function () {
					$(this).removeClass('hoverRow_003');
					var thisObj = $(this);
					var id = thisObj.index();
					if(!thisObj.find('td').find('intput[type=checkbox}').attr('checked')){
						thisObj.find('td').removeClass('hoverRow_003');
					 	//else{
					 	//	thisObj.prev().find('td').addClass('hoverRow');
					 	//}
				 	}
				 }
				);
					
					
					});
    

		
    $(".notification").each(function()
    {
        $(this).click(function()
        {
        	
        	var table = $(this);
			if(!table.find('#errorMsgList').css("display") || 
                table.find('#errorMsgList').css("display") == "none")
            {
                //table.next().css("display", "block");
                table.find('#errorMsgList').toggle();
				//adjustMargin();
                table.find(".right").find("img").attr("src", "../main/img/minus.gif");
            }
            else
            {
                table.find('#errorMsgList').toggle();
				//adjustMargin();
				table.find(".right").find("img").attr("src", "../main/img/plus.gif");
            }
        }
        );
    });

	
	$('table.search_list_refannayi').each(function() 
	{
		var table = $(this);
		var height = window.screen.height - 100;
		$('tbody tr',table).click(
				function () {
					var evt = window.event || arguments.callee.caller.arguments[0];
					var td = evt.srcElement || evt.target;
					if(typeof(td.cellIndex)=="undefined") td = td.parentNode;
					if(td.cellIndex>0&&(!$(this).hasClass("totalTR"))){
						if($(this).attr("id")=="kanli_test"){
							showBigModalDlg("BBONAPL033.html",null,1265,height, "", 20);
						}
						else{
							showBigModalDlg("BBONAPL033_1.html",null,1265,height, "", 20);
						}
					}
				 }
				);
	});
	
	$('table.search_list_overcolor').each(function() 
	{
	  	var table = $(this);
		$('tbody tr',table).mouseover(
				function () {
					var thisObj = $(this);
					var id = thisObj.index();
					if(!thisObj.find('td').find('intput[type=checkbox}').attr('checked')){
						thisObj.find('td').removeClass('hoverRow');
						if(id%2==0){
					    	thisObj.next().find('td').removeClass('hoverRow');
					 	}
					 	else{
					 		thisObj.prev().find('td').removeClass('hoverRow');
					 	}
				 	}
				 }
			);

			$('tbody tr',table).click(
				function () {
					var thisObj = $(this);
					var evt = window.event || arguments.callee.caller.arguments[0];
					var td = evt.srcElement || evt.target;
					if(typeof(td.cellIndex)=="undefined") td = td.parentNode;
					try{
						try{
							var chk = td.parentNode.cells[0].childNodes[0];
							if(typeof(chk.checked)=="undefined") chk = chk.childNodes[0];
							chk.checked = true;
						}catch(e){
							for(var index = 0;index< td.parentNode.parentNode.rows.length;index++){
								var trEach = td.parentNode.parentNode.rows[index];
								if(trEach!=null)
									trEach.className = trEach.className.replace("hoverRow","");
							}
							var trIndex = td.parentNode.rowIndex;
							var tr = td.parentNode.parentNode.rows[trIndex-3];
							var chk = tr.cells[0].childNodes[0];
							if(typeof(chk.checked)=="undefined") chk = chk.childNodes[0];
							tr.className = tr.className + " hoverRow";
							td.parentNode.className = td.parentNode.className + " hoverRow";
							chk.checked = true;
						}
					}catch(e){}
				 }
			);
	});

	$('table.search_list_specTable').each(function() 
    {
    	var table = $(this);
		$('tbody tr',table).mouseover(
		
				function () {
					
					var tab = document.getElementById("search_list");
					//tab.style.cursor= 'hand';
					var evt = window.event || arguments.callee.caller.arguments[0];
					var td = evt.srcElement || evt.target;
					if(typeof(td.cellIndex)=="undefined") 
						td = td.parentNode;
					if(typeof(td.name)=="undefined" || td.name!="except"){
						if(td.attributes("rowspan").value*1>1){
							selectAll(td,tab);
						}
						else{
							groupTr(td.parentNode,tab);
						}
					}else{
						tr = td.parentNode;
						for(var index = 0;index<tr.cells.length;index++){
							if( tr.cells[index].name=="except" ){
							tr.cells[index].className=tr.cells[index].className+' hoverRow';
							}
						}
					}
				 }
				);

		$('tbody tr',table).mouseout(
			function () {
					var tab = document.getElementById("search_list_edit");
					var evt = window.event || arguments.callee.caller.arguments[0];
					var td = evt.srcElement || evt.target;
					if(typeof(td.cellIndex)=="undefined") td = td.parentNode;
					for(var index =0 ;index<tab.rows.length;index++){
						tab.rows[index].className = tab.rows[index].className.replace(' hoverRow','');
					}
					for(var index =0 ;index<tab.rows.length;index++){
						var tr = tab.rows[index];
						for(var m=0;m<tr.cells.length;m++)
							tr.cells[m].className = tr.cells[m].className.replace(' hoverRow','');
					}
			 }
			);
    
    });
    

	
	$("table.search_list_single tbody tr").each(function(){
		var tr = $(this);
		$(this).children().click(function (){
			//if(!$(this).is(":first-child")){
				//find the special row 
				if(tr.children("td").find("input[class='defalutCheckbox']").length>0){
					if(tr.children("td").find("input[type='checkbox']").attr("checked")=="checked"){
						tr.children("td").find("input[type='checkbox']").attr("checked",false);
						tr.removeClass("hoverRow_072");
						tr.addClass("pinkTR");
					}else{
						tr.children("td").find("input[type='checkbox']").attr("checked","checked");
						tr.removeClass("pinkTR");
						tr.addClass("hoverRow_072");			
					}						
				}else{
					var evt = window.event || arguments.callee.caller.arguments[0];
					var td = evt.srcElement || evt.target;
					if(td.tagName == "A")return;
					if(typeof(td.cellIndex)=="undefined") td = td.parentNode;
					if(typeof(td.parentNode.cells)!="undefined"){
						if(td.parentNode.className.indexOf("totalTR")<0){
							var tab = td.parentNode.parentNode;
							if(td.parentNode.cells[0].innerHTML.indexOf("checkbox")>=0){
								//checked --> unchecked
								if(tr.children("td").find("input[type='checkbox']").attr("checked")=="checked"){
									//change checkbox status and remove "hoverRow_072" class from current row
									tr.children("td").find("input[type='checkbox']").attr("checked",false);
									tr.removeClass("hoverRow_072");
								//unchecked --> checked
								}else{
									//change checkbox status and add "hoverRow_072" class to current row
									tr.children("td").find("input[type='checkbox']").attr("checked","checked");
									tr.addClass("hoverRow_072");			
								}
							}
							else if(td.parentNode.cells[0].innerHTML.indexOf("radio")>=0){
									tr.children("td").find("input[type='radio']").attr("checked","checked");
									removeAllClass(tab);
									tr.addClass("hoverRow_072");	
							}
							else{
								removeAllClass(tab);
								tr.addClass("hoverRow_072");	
							}
						}
					}
				}
				

			//}
		})
	});
				

				
	//normal checkbox
	$(".checkbox").each(function(){
		$(this).click(function (){
			var td = $(this).parent();
			var tr = $(this).parent().parent();
			if($(this).attr("checked")=="checked"){
				$(this).attr("checked",false);
				tr.addClass("hoverRow_072");
			}else{
				$(this).attr("checked","checked");
				tr.removeClass("hoverRow_072");
			}
			
		})
	});
				
	//the special checkbox 
	$(".defalutCheckbox").each(function(){
		$(this).click(function (){
			//the special row 
			var tr = $(this).parent().parent();
			if($(this).attr("checked")=="checked"){
				$(this).attr("checked",false);
				tr.removeClass("pinkTR");
				tr.addClass("hoverRow_072");
			}else{
				$(this).attr("checked","checked");
				tr.removeClass("hoverRow_072");
				tr.addClass("pinkTR");
			}

		})
	});

}  

function removeAllClass(tab,classname){
	for(var index=0;index<tab.rows.length;index++){
		if(tab.rows[index]!=null){
			tab.rows[index].className = tab.rows[index].className.replace('hoverRow_072','').replace('selectedRow','');
		}
	}
}

function selectAll(td,tab){
	var rowspan = td.attributes("rowspan").value;
	selectOrRemoveTr(td.parentNode,rowspan,tab);
}

function rowSpanCount(tr){
	var rowspan = 1;
	for(var index = 0;index < tr.cells.length-1; index++){
		if(tr.cells[index].attributes("rowspan").value!="1"
		&&(tr.cells[index+1].attributes("rowspan")==null || tr.cells[index+1].attributes("rowspan").value=="1")){
			rowspan = tr.cells[index].attributes("rowspan").value;
			break;
		}
		else if(tr.cells[index].attributes("rowspan").value=="1"
			   &&tr.cells[index+1].attributes("rowspan").value!="1"){
			rowspan = tr.cells[index+1].attributes("rowspan").value;
			break;
		}
	}
	return rowspan;
}

function groupTr(selTr,tab){
	var rowspan = rowSpanCount(selTr);
	if(rowspan*1>1) {
		selectOrRemoveTr(selTr,rowspan,tab);
		}
	else{
		var tr = selTr;
		for(var index=selTr.rowIndex;index>=0;index--) {
			tr = tab.rows[index];
			if(tr==null) break;
			rowspCont = rowSpanCount(tr);
			if(rowspCont>1) break;
		}
		selectOrRemoveTr(tr,rowSpanCount(tr),tab);
	}
}

/**
*/



function selectOrRemoveTr(tr,rowSpan,tab){
	var rowIndex = tr.rowIndex;
	var evt = window.event || arguments.callee.caller.arguments[0];
	var selTd = evt.srcElement || evt.target;
	if(typeof(selTd.cellIndex)=="undefined") selTd = selTd.parentNode;
	var selTr = selTd.parentNode;
	if(range(tr,rowSpan,tab,selTr)){
		
		exception(tr);
		for(var index = 1;index < rowSpan;index++){
			if(tab.rows[rowIndex+index].className.indexOf('hoverRow')<0){
				tab.rows[rowIndex+index].className=tab.rows[rowIndex+index].className+' hoverRow';
			}
		}
	} else{
		
		exception(selTr);
	}
	
}

function range(tr,rowSpan,tab,selTr){
	var rowIndex = tr.rowIndex;
	for(var index = 0;index < rowSpan;index++){
		if(tab.rows[rowIndex+index]==selTr)
			return true;
	}
	return false;
}

function exception(tr){
	var evt = window.event || arguments.callee.caller.arguments[0];
	var selTd = evt.srcElement || evt.target;
	if(typeof(selTd.cellIndex)=="undefined") selTd = selTd.parentNode;
	var rowspan = 1;

	if(selTd.attributes("rowspan")!=null && typeof(selTd.attributes("rowspan"))!="undefined") {
	
		rowspan = selTd.attributes("rowspan").value;
	}
	if(rowspan*1>1){
		
		for(var index = 0;index<tr.cells.length;index++){
			if(tr.cells[index].attributes("rowspan").value*1<=rowspan*1 && tr.cells[index].name!="except"){
				if(tr.cells[index].className.indexOf('hoverRow')<0)
					tr.cells[index].className=tr.cells[index].className+' hoverRow';
			}
		}
	} else{
	
		var rowspan = rowSpanCount(tr);
		for(var index = 0;index<tr.cells.length;index++){
			if(tr.cells[index].attributes("rowspan").value*1<=rowspan*1 && tr.cells[index].name!="except" ){
				if(tr.cells[index].className.indexOf('hoverRow')<0)
					tr.cells[index].className=tr.cells[index].className+' hoverRow';
			}
		}
	}
}








