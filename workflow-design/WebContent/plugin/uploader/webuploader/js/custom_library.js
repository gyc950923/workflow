var popupProfiles =
{
	window400Center:
	{
		height:300,
		width:400,
		center:1
	},

	window800Center:
	{
		height:600,
		width:800,
		center:1
	},

	window1024Center:
	{
		height:768,
		width:1024,
		center:1
	},
	
	window400NotNew:
	{
		height:300,
		width:400,
		center:1,
		createnew:0
	}

};

function adjustMargin()
{
	if (typeof document.documentElement.style.maxHeight != "undefined") //IE8
	{
		//IE8
		if (parent.document != null && parent.document.getElementById("frmWorkspace") != null && parent.document.getElementById("frmWorkspace").contentDocument != null)
		{
			var body = parent.document.getElementById("frmWorkspace").contentDocument.getElementsByTagName('body')[0];
			var doc = parent.document.getElementById("frmWorkspace").contentDocument.documentElement;
			if (body != null && doc != null)
			{
				//alert("scrollHeight=" + body.scrollHeight + " , clientHeight=" + doc.clientHeight);
				if (body.scrollHeight + 2 > doc.clientHeight)
				{
					//Scroll Bar Exist	
					body.style.margin = '0px,0px,0px,3px'; 
				} else {
					//Scroll Bar Not Exist
					body.style.margin = '0px,17px,0px,3px'; 
				}
			}
		}
	}
}

function initTable()
{
    $('table.search_list').each(function() 
    {
        var table = $(this);
       
        var groupRows = table.attr("groupRows");
        if(!groupRows)
            groupRows = 1;
            
        var onlyDisp = false;
        if($("tbody tr td:first-child input[type='checkbox']", table).length <= 0 &&
            $("tbody tr td:first-child input[type='radio']", table).length <= 0)
            onlyDisp = true;

        var rowIndex = 0;
        $('tbody tr', table).each(function()
        {
            var row = $(this);

            if (rowIndex%(2*groupRows) >= groupRows)
            {
                //row.addClass('alternateRow');
            } 
            rowIndex ++;
            
            if(onlyDisp)
                return;
            
            
            //if the selected checkbox is not the first child, set a switch to ignore the 'toggleRow' onclick event  
             var show1 = true;
             var show2 = true;
             var show3 = true;
             var show4 = true;
				       $("td:not(:first-child) input[type='checkbox']", row).click( 
                 function () { 
                 		 show1 = false;
                 		 show2 = false;
                 		 show3 = false;
                 		 show4 = false;
            	   }   
	             );
          		
            row.click(function(e)
            {            	
            	       		           	
		        var target = null;
                if(window.event)
                    target = window.event.srcElement;
                else
                    target = e.target;
                    
                if(target.tagName == "SELECT")
                    return;
                else if(target.tagName == "INPUT")
                {
                    if(target.type == "text" || 
                        target.type == "button")
                        return;
                }
                
                var sel = true;
                //if(row.is('.selectedRow'))
                if(row.find("input[type='radio']").attr("checked") != "checked")
                    sel = false;
                    
                if(groupRows > 1)
                {
                    var trCollection = $("tbody tr", table);
                    var index = trCollection.index(this);
                    
                    setTimeout(function(){
											 		 if(!show1){
													 	  show1 = true;
											       	return;
											     }
			                    var start = groupRows * Math.floor(index/groupRows); 
			                    for(var i = 0; i < groupRows; i ++)
			                    {
			                        var currow = $(trCollection.get(start + i));
				                        
				                        toggleRow(table, currow, sel);
				                        
			                    }
                    
                     },200);
                }
                else
                {
                	setTimeout(function(){
											 		 if(!show2){
													 	  show2 = true;
											       	return;
											     }
                   			 toggleRow(table, row, sel);
                     },200);
                }
							
							if($("td input[type='radio']", table).length > 0){	
					$('tbody tr', table).each(function() {
						$(this).removeClass('selectedRow');
					});
					//row.find("input[type='radio']").attr("checked","true");
				}
				
                if(sel == false)
                {
                		setTimeout(function(){
											 		 if(!show3){
													 	  show3 = true;
											       	return;
											     }
                    				toggleRow(table, row, sel);
                    },200);
                    $("thead tr td input[name='selectAll']", table).attr("checked", false);
                }
                else
                {
                		setTimeout(function(){
											 		 if(!show4){
													 	  show4 = true;
											       	return;
											     }
                   				 toggleRow(table, row, sel);
                    },200);
                    
                    if($("tbody tr td:first-child input[type='checkbox']:checked", table).length == 
                            $("tbody tr td:first-child input[type='checkbox']", table).length)
                    {
                        $("thead tr td:first-child input[name='selectAll']", table).each(function()
                        {
                            $(this).attr("checked", true);
                        });
                    }
                }
				           	
		           	
		         
                
                
                
                
            });;//end loop row
            
            if($("td:first-child input[type='checkbox']", row).attr('checked') == true)
                row.addClass('selectedRow');
        });
        
        $("thead tr td input[name='selectAll']", table).each(function()
        {
            $(this).click(function()
            {
                if($(this).prop("checked"))
                {
                    $("tbody tr", table).addClass('selectedRow');
                    $("tbody tr td:first-child input[type='checkbox']", table).attr("checked", true);
                }
                else
                {
                    $('tbody tr', table).removeClass('selectedRow');
                    $("tbody tr td:first-child input[type='checkbox']", table).attr("checked", false);
                }
            });
        });
    });
    
     $('table.search_list_single').each(function() 
    {
        var table = $(this);
        
        var groupRows = table.attr("groupRows");
        //if(!groupRows)
            groupRows = 1;
            
        var onlyDisp = false;
        if($("tbody tr td:first-child input[type='checkbox']", table).length <= 0 &&
            $("tbody tr td:first-child input[type='radio']", table).length <= 0)
            onlyDisp = true;

        var rowIndex = 0;
        $('tbody tr', table).each(function()
        {
            var row = $(this);

            if (rowIndex%(2*groupRows) >= groupRows)
            {
                //row.addClass('alternateRow');
            } 
            rowIndex ++;
            
            if(onlyDisp)
                return;
            
            
            //if the selected checkbox is not the first child, set a switch to ignore the 'toggleRow' onclick event  
             var show1 = true;
             var show2 = true;
             var show3 = true;
             var show4 = true;
				       $("td:not(:first-child) input[type='checkbox']", row).click( 
                 function () { 
                 		 show1 = false;
                 		 show2 = false;
                 		 show3 = false;
                 		 show4 = false;
            	   }   
	             );
          		
            row.click(function(e)
            {            	
            	       		           	
		           	var target = null;
                if(window.event)
                    target = window.event.srcElement;
                else
                    target = e.target;
                    
                if(target.tagName == "SELECT")
                    return;
                if(target.tagName == "A")
                    return;
                if(""){
                }
                else if(target.tagName == "INPUT")
                {
                    if(target.type == "text" || 
                        target.type == "button")
                        return;
                }
                
                var sel = true;
                //if(row.is('.selectedRow'))
                if(row.find("input[name='checkbox']").attr("checked") != "checked")
                    sel = false;
                    
                if(groupRows > 1)
                {
                    var trCollection = $("tbody tr", table);
                    var index = trCollection.index(this);
                    
                    setTimeout(function(){
											 		 if(!show1){
													 	  show1 = true;
											       	return;
											     }
			                    var start = groupRows * Math.floor(index/groupRows); 
			                    for(var i = 0; i < groupRows; i ++)
			                    {
			                        var currow = $(trCollection.get(start + i));
				                        
				                        toggleRow(table, currow, sel);
				                        
			                    }
                    
                     },200);
                }
                else
                {
                	setTimeout(function(){
											 		 if(!show2){
													 	  show2 = true;
											       	return;
											     }
                   			 toggleRow(table, row, sel);
                     },200);
                }
							
							if($("td input[type='radio']", table).length > 0){	
					$('tbody tr', table).each(function() {
						$(this).removeClass('selectedRow');
					});
					//row.find("input[type='radio']").attr("checked","true");
				}
				
                if(sel == false)
                {
                		setTimeout(function(){
											 		 if(!show3){
													 	  show3 = true;
											       	return;
											     }
                    				toggleRow(table, row, sel);
                    },200);
                    $("thead tr td input[name='selectAll']", table).attr("checked", false);
                }
                else
                {
                		setTimeout(function(){
											 		 if(!show4){
													 	  show4 = true;
											       	return;
											     }
                   				 toggleRow(table, row, sel);
                    },200);
                    
                    if($("tbody tr td:first-child input[type='checkbox']:checked", table).length == 
                            $("tbody tr td:first-child input[type='checkbox']", table).length)
                    {
                        $("thead tr td:first-child input[name='selectAll']", table).each(function()
                        {
                            $(this).attr("checked", true);
                        });
                    }
                }
				           	
		           	
		         
                
                
                
                
            });;//end loop row
            
            if($("td:first-child input[type='checkbox']", row).attr('checked') == true)
                row.addClass('selectedRow');
        });
        
        $("thead tr td input[name='selectAll']", table).each(function()
        {
            $(this).click(function()
            {
				//alert($(this).prop("checked"));
                if($(this).prop("checked"))
                {
	                //$('tbody tr', table).addClass('selectedRow');
	                $('tbody tr', table).each(function() {
						if ($(this).attr('class') != "totalTR")
						{
							$(this).addClass('selectedRow');
							//$('tbody tr', table).addClass('selectedRow');
						}
					});

                    $("tbody tr td:first-child input[type='checkbox']", table).prop("checked", true);
                    
                }
                else
                {
                    $('tbody tr', table).removeClass('selectedRow');
                    $('tbody tr', table).removeClass('hoverRow_072');
                    $("tbody tr td:first-child input[type='checkbox']", table).prop("checked", false);
                }
            });
        });
    });
    
    
    $('table.search_list_total').each(function() 
    {
        var table = $(this);
        
        var groupRows = table.attr("groupRows");
        //if(!groupRows)
            groupRows = 1;
            
        var onlyDisp = false;
        if($("tbody tr td:first-child input[type='checkbox']", table).length <= 0 &&
            $("tbody tr td:first-child input[type='radio']", table).length <= 0)
            onlyDisp = true;

        var rowIndex = 0;
        $('tbody tr', table).each(function()
        {
            var row = $(this);

            if (rowIndex%(2*groupRows) >= groupRows)
            {
                //row.addClass('alternateRow');
            } 
            rowIndex ++;
            
            if(onlyDisp)
                return;
            
            
            //if the selected checkbox is not the first child, set a switch to ignore the 'toggleRow' onclick event  
             var show1 = true;
             var show2 = true;
             var show3 = true;
             var show4 = true;
				       $("td:not(:first-child) input[type='checkbox']", row).click( 
                 function () { 
                 		 show1 = false;
                 		 show2 = false;
                 		 show3 = false;
                 		 show4 = false;
            	   }   
	             );
          		
            row.click(function(e)
            {            	
            	       		           	
		        var target = null;
                if(window.event)
                    target = window.event.srcElement;
                else
                    target = e.target;
                    
                if(target.tagName == "SELECT")
                    return;
                if(target.tagName == "A")
                    return;
                if(""){
                }
                else if(target.tagName == "INPUT")
                {
                    if(target.type == "text" || 
                        target.type == "button")
                        return;
                }
                
                var sel = true;
                //if(row.is('.selectedRow'))
                if(row.find("input[name='checkbox']").attr("checked") != "checked")
                     sel = false;
                    
                if(groupRows > 1)
                {
                    var trCollection = $("tbody tr", table);
                    var index = trCollection.index(this);
                    
                    setTimeout(function(){
											 		 if(!show1){
													 	  show1 = true;
											       	return;
											     }
			                    var start = groupRows * Math.floor(index/groupRows); 
			                    for(var i = 0; i < groupRows; i ++)
			                    {
			                        var currow = $(trCollection.get(start + i));
				                        
				                        //toggleRow(table, currow, sel);
				                        
			                    }
                    
                     },200);
                }
                else
                {
                	setTimeout(function(){
											 		 if(!show2){
													 	  show2 = true;
											       	return;
											     }
                   			 //toggleRow(table, row, sel);
                     },200);
                }
							
				if($("td input[type='radio']", table).length > 0){	
					$('tbody tr', table).each(function() {
						$(this).removeClass('selectedRow');
					});
					//row.find("input[type='radio']").attr("checked","true");
				}
				
                if(sel == false)
                {
                		setTimeout(function(){
											 		 if(!show3){
													 	  show3 = true;
											       	return;
											     }
                    				//toggleRow(table, row, sel);
                    },200);
                    $("thead tr td input[name='selectAll']", table).attr("checked", false);
                }
                else
                {
                		setTimeout(function(){
											 		 if(!show4){
													 	  show4 = true;
											       	return;
											     }
                   				 //toggleRow(table, row, sel);
                    },200);
                    
                    if($("tbody tr td:first-child input[type='checkbox']:checked", table).length == 
                            $("tbody tr td:first-child input[type='checkbox']", table).length)
                    {
                        $("thead tr td:first-child input[name='selectAll']", table).each(function()
                        {
                            $(this).attr("checked", true);
                        });
                    }
                    
                }
				           	
                
                
            });;//end loop row
            
            /*if($("td:first-child input[type='checkbox']", row).attr('checked') == true)
                row.addClass('selectedRow');*/
     
        });
        
        $("thead tr td input[name='selectAll']", table).each(function()
        {
            $(this).click(function()
            {
                if($(this).prop("checked"))
                {
                    //$("tbody tr", table).addClass('selectedRow');
                    $("tbody tr td:first-child input[type='checkbox']", table).attr("checked", true);
                }
                else
                {
                    //$('tbody tr', table).removeClass('selectedRow');
                    $('tbody tr', table).removeClass('hoverRow_072');
                    $("tbody tr td:first-child input[type='checkbox']", table).attr("checked", false);
                }
            });
        });
    });   
    
    
    
}






function goPage(url)
{
	if(url != null && typeof url == "string")
	{
		window.location.href = url;
	}
}

function popupPage(url)
{
	if(url != null && typeof url == "string")
	{
		window.showModalDialog(url)
	}
}

function addButtonClickEventHandler(id, url, popup)
{
	var elment_str = ":button[id='" + id + "']";
	$(elment_str).each
	(
		function()
		{
			el = $(this);
			if(el != null )
			{
				el.click
				(
					function()
					{
						if(popup)
						{
							popupPage(url);
						}
						else
						{
							goPage(url);
						}
					}
				);
			}
		}
	)
}

function toggleRow(table, row, sel)
{
    if(sel == true)
    {
        row.addClass('selectedRow');
        //$("td:first-child input[type='checkbox']", row).attr('checked', true);
    }
    else
    {
        row.removeClass('selectedRow');
        //$("td:first-child input[type='checkbox']", row).attr('checked', false);
    }
}

function printPreview(){
	window.parent.document.all.framecontent.ExecWB(7,1);
}