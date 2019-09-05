if (typeof(Cms_paging) == 'undefined') {
	Cms_paging = {};
    /** extends * */
	Cms_paging.extend = function (self, obj) {
      if (self == null) {
        self = {};
      }
      for (var i = 1; i < arguments.length; i++) {
        var o = arguments[i];
        if (typeof(o) != 'undefined' && o != null) {
          for (var k in o) {
            self[k] = o[k];
          }
        }
      }
      return self;
    };
}
Cms_paging = Cms_paging.extend(Cms_paging,
{
	gotoPage : function(page) {
		var targetForm = document.forms[0].id;

		Cms_paging.setValueOfPageNo(page);
		Cms_paging.setValueOfSortInfo();
    	var targerForm = document.forms[0];
    	var target = targerForm.action;
    	if(document.getElementById('__defaultAction') != 'undefined' && document.getElementById('__defaultAction') != null){
    		target = document.getElementById('__defaultAction').value + "?___pageNo=" + document.getElementById('___pageNo').value;
    	} else {
    		target = target + "?___pageNo=" + document.getElementById('___pageNo').value;
    	}
    	var arr = target.split("?");
    	if(arr.length > 2){
    		target = arr[0]+ "?" + arr[arr.length-1];
    	}
    	targerForm.action = target;
    	targerForm.removeChild(document.getElementById('___pageNo'));
    	$("#mstEditDiv :input[type != button]").val("");
    	targerForm.submit();
	},

	// create pageNo
	setValueOfPageNo:function(page){
		var targerForm = document.forms[0];
		var pageNo   = document.getElementById('___pageNo');
		if(pageNo==undefined || pageNo==null){
			pageNo   =document.createElement('input');
		    pageNo.id = "___pageNo";
		    pageNo.name  = "___pageNo";
		    pageNo.type  = "hidden";
		    pageNo.value = page;
		    targerForm.appendChild(pageNo);
		}else{
			pageNo.value = page;
		}
	},

	// when document is ready, add some value to html's body
	setValueOfSortInfo:function(){
		var targerForm = document.forms[0];
	    var pageNo1   = document.getElementById('___pageNoSort');
		if(pageNo1==undefined || pageNo1==null){
			pageNo1   = document.createElement('input');
		    pageNo1.id = "___pageNoSort";
		    pageNo1.name  = "___pageNoSort";
		    pageNo1.type  = "hidden";
		    var  fieldSortValue =$('#fieldSortId').val();
		    var  sortFieldValue =$('#sortField').val();
		    if(fieldSortValue!=null)
		    	 pageNo1.value=fieldSortValue;
		    if(sortFieldValue!="")
		    	 pageNo1.value=sortFieldValue;
		    targerForm.appendChild(pageNo1);
		}else{
			var  fieldSortValue =$('#fieldSortId').val();
		    var  sortFieldValue =$('#sortField').val();
		    if(fieldSortValue!=null)
		    	 pageNo1.value=fieldSortValue;
		    if(sortFieldValue!="")
		    	 pageNo1.value=sortFieldValue;
		}
	},

	// set sortField
	setSortField:function(obj,obj1){
		var sortExecute = document.getElementById("fieldSortId");
		if(sortExecute != null){
			var trIndex = obj.parent().index();
		    var preSortTh = $('#sortThIndex').val();
		    var curSortThIndex = obj.index();
		    if(preSortTh == ""){
		        obj.append("<img src='" + imgPath + "/main/img/sort_asc_on.png' class='sort_icon'/>");
		    	$('#sortThIndex').val(trIndex+','+curSortThIndex + '_' + '0');
		    	$('#sortField').val(obj.attr("id")+'/'+'asc'+'/'+$('#sortThIndex').val());
		    }
		    else{
		    	var sort = preSortTh.split('_');
		    	var thIndex = sort[0];
		    	var sortType = sort[1];
		    	var curThIndex = trIndex + "," +curSortThIndex;
		    	$(".sort_icon").remove();
		    	if(thIndex == curThIndex){
		    		if('0' == sortType){
		    			$('img',obj1).replaceWith("<img src='" + imgPath + "/main/img/sort_desc_on.png' class='sort_icon'/>");
		    			$('#sortThIndex').val(trIndex+','+curSortThIndex + '_' + '1');
		    			$('#sortField').val(obj.attr("id")+'/'+'desc'+'/'+$('#sortThIndex').val());
		    		}else{
		    			$('img',obj1).replaceWith("<img src='" + imgPath + "/main/img/sort_asc_on.png' class='sort_icon'/>");
		    			$('#sortThIndex').val(trIndex+','+curSortThIndex + '_' + '0');
		    			$('#sortField').val(obj.attr("id")+'/'+'asc'+'/'+$('#sortThIndex').val());
		    		}
		    	}else{
		    		obj.find('thead tr td img').remove();
		    		obj.append("<img src='" + imgPath + "/main/img/sort_asc_on.png' class='sort_icon'/>");
		    	    $('#sortThIndex').val(trIndex+','+curSortThIndex + '_' + '0');
		    	    $('#sortField').val(obj.attr("id")+'/'+'asc'+'/'+$('#sortThIndex').val());
		    	}
		    }
		}
	}
});



