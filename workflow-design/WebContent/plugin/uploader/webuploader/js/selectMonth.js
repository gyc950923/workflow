	document.onclick = function (event)
        {
		//  alert('test');
            var e = event || window.event;
           var elem = e.srcElement||e.target;


            while(elem)
            {
			    if(elem.id == "div1"||elem.className=="monthSelectImage")
                {
				  $("#div1").show();
				  break;
                       // return;
                } else{
				   	  $("#div1").hide();
				}
                elem = elem.parentNode;

            }
           //隐藏div的方法
           //hideDiv1();
      }

	  function hideDiv1(){

		 	$("#div1").animate({opacity: "hide" }, "slow");
			$("#div1").empty();
	  }

		function popupDiv(item,date,name,temp) {
			var flag=item;
			var rq="";
			$("#"+name).empty();
			var currentDate= new Date();
			var currentYear,currentMonth="";
			if(null!=date&&date!=""){
				currentYear=date.split("/")[0];
				currentMonth=date.split("/")[1];

			}else{
			    currentYear=currentDate.getFullYear();
				currentMonth=currentDate.getMonth()+1;

			}
          var calender = "<table ><tr><td class='th1'>"+
        "<input type='button' onclick=changeYear('down','"+flag+"') class='btnLeft'/></td><td id='year"+flag+"' class='th2'></td><td class='th1'>"+
						 "<input type='button' onclick=changeYear('up','"+flag+"')  class='btnRight'/></td></tr>";
			//$("#"+name).append("<table cellspacing='1'><tr><td><button onclick=changeYear('down','"+flag+"')><img src='1.gif'></button></td><td id='year"+flag+"' class='th2'></td><td><button onclick=changeYear('up','"+flag+"')><img src='2.gif'></button></td></tr><tr><td><button onclick=hideDiv('"+item+"','"+name+"',1,'"+flag+"')>1</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',2,'"+flag+"')>2</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',3,'"+flag+"')>3</button></td></tr><tr><td><button onclick=hideDiv('"+item+"','"+name+"',4,'"+flag+"')>4</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',5,'"+flag+"')>5</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',6,'"+flag+"')>6</button></td></tr><tr><td><button onclick=hideDiv('"+item+"','"+name+"',7,'"+flag+"')>7</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',8,'"+flag+"')>8</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',9,'"+flag+"')>9</button></td></tr><tr><td><button onclick=hideDiv('"+item+"','"+name+"',10,'"+flag+"')>10</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',11,'"+flag+"')>11</button></td><td><button onclick=hideDiv('"+item+"','"+name+"',12,'"+flag+"')>12</button></td></tr></table>");
			//$("#"+name).append(calender);

			for (var dRow = 1; dRow < 13; dRow++) {
				if(dRow%3==1){
                   calender+="<tr class='header'>";
				}
				if(dRow==currentMonth)
                calender+="<td style='PADDING-TOP: 1px;PADDING-BOTTOM: 1px; BORDER-RIGHT-STYLE: none;BORDER-BOTTOM-STYLE: none;'><button type='button' class='btn1_mouseout' style='border:#71AACD 1px solid; margin-bottom:2;'  onclick=hideDiv('"+item+"','"+name+"','"+dRow+"','"+flag+"')>"+dRow+"月</button></td>";
				else
				calender+="<td style='PADDING-TOP: 1px;PADDING-BOTTOM: 1px;BORDER-RIGHT-STYLE: none;BORDER-BOTTOM-STYLE: none;'><button type='button'  class='btn1_mouseout' onmouseover=change1(this) onmouseout=change2(this) onclick=hideDiv('"+item+"','"+name+"','"+dRow+"','"+flag+"')>"+dRow+"月</button></td>";
                if(dRow%3==0){
                   calender+="</tr>";
				}
			}
			calender+="</table>";

			$("#"+name).append(calender);

		    $("#year"+flag).append(currentYear+"年");
			$("#"+name).show();
		}
		function hideDiv(tag,name,item,flag) {
		    var currentTime= $("#year"+flag).text();
			var time=currentTime.substring(0,currentTime.indexOf("年"));
			switch(parseInt(item)){
				case 1:
					time+="/01";
					break;
				case 2:
					time+="/02";
					break;
				case 3:
					time+="/03"
					break;
				case 4:
					time+="/04"
					break;
				case 5:
					time+="/05"
					break;
				case 6:
					time+="/06"
					break;
				case 7:
					time+="/07"
					break;
				case 8:
				time+="/08"
					break;
				case 9:
				time+="/09"
					break;
				case 10:
				time+="/10"
					break;
				case 11:
				time+="/11"
					break;
				case 12:
				time+="/12"
					break;
			}
			$("."+tag).attr("value",time);
			$("#"+name).animate({opacity: "hide" }, "slow");
			$("#"+name).empty();
		}
        var temp = "";
		function changeYear(item,flag){
			if(item=="up"){
				$("#year"+flag).empty();
				$("#year"+flag).append(++temp+"年");
			}
			else{
				$("#year"+flag).empty();
				$("#year"+flag).append(--temp+"年");
			}
		}
		/*$(document).ready(function(){
			$("input").click(function(){
				x=$("#"+this.id).offset();
				if($("#div1").length==0){
				$("#"+this.id).after("<div id='div1' class='div1' style='position:absolute' ></div>");
			      }
				$("#div1").css({ top: ""+(x.top+20)+"px",left:""+x.left+"px"})
				popupDiv(this.id,"div1");


			});

		});*/

		function showTime(item){
			   var date= $("."+item.id).val();
			   if(null!=date&&date!=""){
				   temp= Number(date.split("/")[0]);
				}else{
					var currentDate= new Date();
				   temp= currentDate.getFullYear();
				}
               var x=$("."+item.id).offset();
				if($("#div1").length==0){
				$("#"+item.id).after("<div id='div1' class='div1' style='position:absolute' ></div>");
			    }
				$("#div1").css({ top: ""+(x.top+21)+"px",left:""+x.left+"px"})
				popupDiv(item.id,date,"div1");
		}

		function change1(item){

		 item.className="btn1_mouseover";
		}

		function change2(item){

		 item.className="btn1_mouseout";
		}



