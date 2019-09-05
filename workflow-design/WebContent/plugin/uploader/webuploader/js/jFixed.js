(function($) {
	$.fn.jFixedtable = function(options) {
		var options = $.extend({
			width : "640",
			height : "320",
			margin : "0",
			padding : "0",
			overflow : "hidden",
			colWidths : undefined,
			fixedCols : 0,
			headerRows : 1,
			onStart : function() {
			},
			onFinish : function() {
			},
			onCumclick : function() {
			},
			edit : false,
			pkey : null
		}, options);
		return this.each(function() {
			this.headerRows = parseInt(options.headerRows || "1");
			this.fixedCols = parseInt(options.fixedCols || "0");
			this.colWidths = options.colWidths || [];
			this.initFunc = options.onStart || null;
			this.callbackFunc = options.onFinish || null;
			this.initFunc && this.initFunc();
			this.oncumclick = options.oncumclick;
			this.sHeaderHeight = 0;
			this.replaceStr = function(str) {
				str = str.replace(/&lt;/g, "<");
				str = str.replace(/&gt;/g, ">");
				return str;
			};
			this.id = $(this).attr("id");
			$(this).data("id", this.id);
			$table = $(this);
			$(this).attr("id", "");
			this.sBase = $("<div></div>");
			this.sFHeader = this.sBase.clone(true);
			this.sHeader = this.sBase.clone(true);
			this.sHeaderInner = this.sBase.clone(true);
			this.sFData = this.sBase.clone(true);
			this.sFDataInner = this.sBase.clone(true);
			this.sData = this.sBase.clone(true);
			this.sDataTable = $table;
			this.sColGroup = document.createElement("COLGROUP");
			this.sFHeader.addClass("sFHeader");
			this.sHeader.addClass("sHeader");
			this.sHeaderInner.addClass("sHeaderInner");
			this.sFData.addClass("sFData");
			this.sFDataInner.addClass("sFDataInner");
			this.sData.addClass("sData");
			this.sData.attr("id", this.id + "_sData");
			this.sBase.addClass("sBase");
			this.ToFixTb = function() {
				$table.wrap("<div></div>");
				$table.parent().append(this.sBase);
				this.sBase.parent().css({
					width : options.width,
					height : options.height
				});
				var $th = $table.find("tr:first");
				var $ths = $table.find("tr:first>td");
				if (!$ths){
					$ths = $table.find("tr:first>th");
				}
				this.sHeaderTable = $("<table class='tableCss'><tbody></tbody></table>");
				if (this.headerRows > 0) {
					for (var i = 0; i < this.headerRows; i++) {
						//以第一行为标准高度
						this.sHeaderHeight += parseInt($table.find("tr").eq(0).outerHeight());
					}
					$th = $table.find("tr:lt(" + this.headerRows + ")");
				}
				for (i = 0; i < this.headerRows; i++) {
					this.sHeaderTable.append($table.find("tr").eq(i).clone(true));
				}
				this.sFHeaderWidth = 0;
				this.sHeaderInner.append(this.sHeaderTable);
				if (this.fixedCols) {
					this.sFHeaderTable = this.sHeaderTable.clone(true);
					this.sFHeader.append(this.sFHeaderTable);
					this.sFDataTable = this.sDataTable.clone(true);
					this.sFDataInner.append(this.sFDataTable);
				}
				var tds = $table.find("tr").eq(this.headerRows).find("td");

				var tdNum = 0;
				for(var _i = 0; _i < tds.size(); _i++){
					var tdColspan = tds.eq(_i).attr("colspan");
					if(tdColspan == undefined || tdColspan == null){
						tdColspan = 1;
					}
					tdNum +=parseInt(tdColspan);
				}
				if(tds.size() == tdNum){
					for (i = 0, j = tdNum; i < j; i++) {
						if (i === this.colWidths.length || this.colWidths[i] === -1) {
							this.colWidths[i] = parseInt(tds[i].offsetWidth);
						}
					}
				}else{
					var trs = $table.find("tr");
					var _trIndex = 0;
					for(;_trIndex<trs.size();_trIndex++){
						var _tr = $table.find("tr").eq(_trIndex).find("td");
						if(_tr.size() == tdNum){
							break;
						}
					}
					var _tds = $table.find("tr").eq(_trIndex).find("td");
					for (i = 0, j = tdNum; i < j; i++) {
						if (i === this.colWidths.length || this.colWidths[i] === -1) {
							this.colWidths[i] = parseInt(_tds[i].offsetWidth);
						}
					}
				}

				for (i = 0; i < this.headerRows; i++) {
					var tds = this.sDataTable.find("tr").eq(i).find("td");
					for (var k = 0; k < tds.size(); k++) {
						tds.eq(k).removeAttr("width");
					}
				}
				for (i = 0, j = this.colWidths.length; i < j; i++) {
					this.sColGroup.appendChild(document.createElement("COL"));
					this.sColGroup.lastChild.setAttribute("width",this.colWidths[i]);
					this.sColGroup.lastChild.setAttribute("colreq",i);
				}
				$(this.sColGroup.cloneNode(true)).prependTo(this.sDataTable);
				$(this.sColGroup.cloneNode(true)).prependTo(this.sHeaderTable);
				if (this.fixedCols > 0) {
					$(this.sColGroup.cloneNode(true)).prependTo(this.sFHeaderTable);
					$(this.sColGroup.cloneNode(true)).prependTo(this.sFDataTable);
				}
				if (this.fixedCols > 0) {
					this.sBase.append(this.sFHeader);
				}
				this.sBase.append(this.sHeader);
				this.sHeader.append(this.sHeaderInner);
				if (this.fixedCols > 0) {
					this.sFData.append(this.sFDataInner);
					this.sBase.append(this.sFData);
				}
				this.sData.append(this.sDataTable);
				this.sBase.append(this.sData);
				if (this.fixedCols > 0) {
					this.sFHeaderWidth = this.sDataTable[0].tBodies[0].rows[this.headerRows].cells[this.fixedCols].offsetLeft + 1;
				} else{
					this.sFHeaderWidth = 0;
				}
				this.sFHeader.css("width", this.sFHeaderWidth + "px");
				this.sData.css({
					"margin-left" : this.sFHeaderWidth,
					"margin-top" : this.sHeaderHeight,
					height : (options.height - this.sHeaderHeight),
					width : (options.width - this.sFHeaderWidth)
				});
				this.sDataTable.css({
					"margin-left" : -this.sFHeaderWidth,
					"margin-top" : -this.sHeaderHeight
				});
				this.id = $(this).data("id");
				this.sDataTable.attr("id", this.id);
				if (this.fixedCols > 0) {
					this.sData.scroll(function() {
						$(this).parent().find(".sHeaderInner").css({
							right : $(this).scrollLeft() + "px"
						});
						$(this).parent().find(".sFDataInner").css({
							top : -1 * $(this).scrollTop() + "px"
						});
					});
				} else {
					this.sData.scroll(function() {
						$(this).parent().find(".sHeaderInner").css({
							right : $(this).scrollLeft() + "px"
						});
					});
				}
				if (options.edit)
					$("#" + this.id).jGrid({
						ftb : options
					});
			};
			this.ToFixTb();
		});
	}
})(jQuery);

(function($) {
	$.fn.extend({
		jGrid : function(options) {
			var op = $.extend({
				onStart : function() {
				},
				onFinish : function() {
				},
				onCumclick : function() {
				},
				edit : false,
				fixed : false,
				ftb : null
			}, options);
			var $table = $(this);
			var $th = "<tr></tr>";
			var trs = gettrs();
			var head = op.ftb ? $(this).find("tr").eq(parseInt(op.ftb.headerRows) - 1) : $(this).find("tr:first");
			var ths = getth();
			var edittd = null;
			var currnettr = null;
			var isbr = false;
			var colums = [];
			this.pkey = [];
			init();

			function init() {
				initHead();
				ths = getth();
				$th = $("<tr></tr>");
				$th.append($table.data("head"));
				currenttr = trs.eq(0);
				var chbox = $th.find("td:first").find(":checkbox");
				if (chbox) {
					if (op.ftb) {
						if (op.ftb.fixedCols > 1) {
							var hebox = $table.parent().parent().find(".sFHeader>table>tbody>tr:first>td:first").find(":checkbox");
							hebox.click(function() {
								gettrs().each(function() {
									var cbox = $(this).find("td:first").find(":checkbox");
									if (cbox) {
										if (hebox.is(":checked")){
											cbox.attr("checked","true");
										}else{
											cbox.attr("checked","");
										}
									}
								});
								getsfdatatr().each(function() {
									var sfcbox = $(this).find("td:first").find(":checkbox");
									if (sfcbox) {
										if (hebox.is(":checked")){
											sfcbox.attr("checked","true");
										}else{
											sfcbox.attr("checked","");
										}
									}
								});
							});
						} else {
							var hebox = $table.parent().parent().find(".sFHeader>table>tbody>tr:first>td:first").find(":checkbox");
							hebox.click(function() {
								gettrs().each(function() {
									var cbox = $(this).find("td:first").find(":checkbox");
									if (cbox) {
										if (hebox.is(":checked")){
											cbox.attr("checked","true");
										}else{
											cbox.attr("checked","");
										}
									}
								});
							});
						}
					} else {
						var rlchbox = $table.find("tr:first>td:first").find(":checkbox");
						rlchbox.click(function() {
							gettrs().each(function() {
								var cbox = $(this).find("td:first").find(":checkbox");
								if (cbox) {
									if (rlchbox.is(":checked")){
										cbox.attr("checked","true");
									}else{
										cbox.attr("checked","");
									}
								}
							});
						});
					}
				};
				$table.data("ftb", op.ftb);
			};

			function initHead() {
				var lsttr = op.ftb ? $table.find("tr:lt(" + op.ftb.headerRows + "):last") : $table.find("tr:first");
				var dtr = $("<tr></tr>");
				var curtr = $table.find("tr").eq(0);
				var k = 0;
				for (var j = 0; j < curtr.children().size(); j++) {
					var curtd = curtr.children().eq(j);
					var rowspan = curtd.attr("rowspan");
					var colspan = curtd.attr("colspan");
					if (colspan > 1) {
						for (var i = 0; i < colspan; i++) {
							var dex = k + i;
							dtr.append(lsttr.children().eq(dex).clone(true));
						}
						k = k + parseInt(colspan);
					} else {
						dtr.append(curtd.clone(true));
					}
				}
				$table.data("head", dtr.html());
				$table.data("op", op);
			};

			function tabcut(td, i) {
				var edittype = $th.find("td").eq(i).attr("EditType");
				$(td).trigger("click", [ td, edittype, $th.find("td")[i] ]);
			};

			function getth() {
				return $($table.data("head"));
			};

			function gettrs() {
				return op.ftb ? $table.find("tr:gt(" + parseInt(op.ftb.headerRows - 1) + ")") : $table.find("tr").not(":first");
			};

			function getsfdatatr() {
				return $table.parent().parent().find(".sFData").find(".sFDataInner>table>tbody>tr");
			};

			function reSet() {
				th = _getth();
				trs = _gettrs();
			};

			function ClearTd(td) {
				$(td).html("");
			};
		}
	});

	$.jtool = {
		formatNumber : function(num, pattern) {
			var strarr = num ? num.toString().split('.') : [ '0' ];
			var fmtarr = pattern ? pattern.split('.') : [ '' ];
			var retstr = '';
			var str = strarr[0];
			var fmt = fmtarr[0];
			var i = str.length - 1;
			var comma = false;
			for (var f = fmt.length - 1; f >= 0; f--) {
				switch (fmt.substr(f, 1)) {
				case '#':
					if (i >= 0)
						retstr = str.substr(i--, 1) + retstr;
					break;
				case '0':
					if (i >= 0)
						retstr = str.substr(i--, 1) + retstr;
					else
						retstr = '0' + retstr;
					break;
				case ',':
					comma = true;
					retstr = ',' + retstr;
					break;
				}
			}
			if (i >= 0) {
				if (comma) {
					var l = str.length;
					for (; i >= 0; i--) {
						retstr = str.substr(i, 1) + retstr;
						if (i > 0 && ((l - i) % 3) == 0){
							retstr = ',' + retstr;
						}
					}
				} else
					retstr = str.substr(0, i + 1) + retstr;
			}
			retstr = retstr + '.';
			str = strarr.length > 1 ? strarr[1] : '';
			fmt = fmtarr.length > 1 ? fmtarr[1] : '';
			i = 0;
			for (var f = 0; f < fmt.length; f++) {
				switch (fmt.substr(f, 1)) {
					case '#':
						if (i < str.length){
							retstr += str.substr(i++, 1);
						}
						break;
					case '0':
						if (i < str.length){
							retstr += str.substr(i++, 1);
						}else{
							retstr += '0';
						}
						break;
				}
			}
			return retstr.replace(/^,+/, '').replace(/\.$/, '');
		},
		addRow : function(id, option) {
			var $table = $("#" + id);
			this.table = $table;
			var op = eval($table.data("op"));
			this.op = op;
			var headerrows = null;
			var lasttr = null;
			var newtr = null;
			if (option) {
				var data = eval(option.data);
				var pkey = data.pkey;
				var values = data.value;
				var hd = $table.data("head");
				var ftb = $table.data("ftb");
				var tr = $("<tr></tr>");
				newtr = $("<tr></tr>");
				tr.append($(hd).clone(true));
				var x = $table.find("tr").size();
				for (var i = 0; i < values.length; i++) {
					for (var j = 0; j < tr.find("td").size(); j++) {
						var td = tr.find("td").eq(j);
						if (td.children().size() > 0 && j==0) {
							var checkBoxHtml = "<input id="+values[i][0].value+" name='innerCheck' onclick='innerCkFun(this)' type='checkbox' checked='checked'>";
							var tdc = $("<td class='td_gray_readonly tab1check'></td>").append(checkBoxHtml);
							newtr.append(tdc);
						} else {
							newtr.append($("<td class='td_gray_readonly'>" + values[i][j].value+ "</td>"));
						}
					}
					var ttr = newtr.clone();
					ttr.data("pkey", pkey);
					$table.append(ttr);
					this._getsfdata().append(ttr.clone(true).data("pkey", pkey));
					newtr = $("<tr></tr>");
				}
				$table.jGrid({ftb : ftb});
			}
		},
		addCol : function(id, name,option) {
			var $table = $("#" + id);
			this.table = $table;

			for(var i = 0; i < $table.find("tr").size(); i++){
				var $tr = $table.find("tr").eq(i);
				var td = $tr.find("td").last();
				if (td.children().size() > 0) {
					$tr.append($("<td class="+td.attr('class')+"></td>").append(td.children().clone(true)));
				}else{
					$tr.append("<td class="+td.attr('class')+"></td>");
				}
			}
			$table.find("colgroup").append("<col width='50'/>");

			var sFData = this.table.parent().parent().find(".sFData");
			var sfdataTr = this._getsfdata().find("tr");
			for(var i = 0; i < sfdataTr.size(); i++){
				var $tr = sfdataTr.eq(i);
				var td = $tr.find("td").last();
				if (td.children().size() > 0) {
					$tr.append($("<td class="+td.attr('class')+"></td>").append(td.children().clone(true)));
				}else{
					$tr.append("<td class="+td.attr('class')+"></td>");
				}
			}
			sFData.find(".sFDataInner>table>colgroup").append("<col width='50'/>");;

			var sHeader = this.table.parent().parent().find(".sHeader");
			var sHeaderTr = sHeader.find(".sHeaderInner>table>tbody").find("tr");
			for(var i = 0; i < sHeaderTr.size(); i++){
				var $tr = sHeaderTr.eq(i);
				var td = $tr.find("td").last();
				var tdCss = td.attr("class");
				if(i == 8){
					td.attr("colspan",parseInt(td.attr("colspan")) + 1);
				}else{
					if (td.children().size() > 0) {
						var tdClone = td.children().clone(true);
						tdClone.attr("name",name);
						tdClone.prop("checked",true);
						$tr.append($("<td class='"+td.attr('class')+" tab1check' align='center'></td>").append(tdClone));
					}else{
						$tr.append($("<td class="+td.attr('class')+"></td>"));
					}
				}
			}
			sHeader.find(".sHeaderInner>table>colgroup").append("<col width='50'/>");
		},
		_getth : function() {
			return $(this.table.data("head"));
		},
		_gettrs : function() {
			if (typeof (this.op) == "undefined") {
				var op = eval($table.data("op"));
				this.op = op;
			}
			return this.op.ftb ? this.table.find("tr:gt("+ parseInt(this.op.ftb.headerRows - 1) + ")") : this.table.find("tr").not(":first");
		},
		_getsfdata : function() {
			return this.table.parent().parent().find(".sFData").find(".sFDataInner>table>tbody");
		},

		loaddata : function(id, options) {
			var op = $.extend({
				headerRows : 1
			}, options);
			var $table = $("#" + id);
			this.table = $table;
			var lsttr = $table.find("tr:lt(" + op.headerRows + "):last");
			var dtr = $("<tr></tr>");
			var curtr = $table.find("tr").eq(0);
			var k = 0;
			for (var j = 0; j < curtr.children().size(); j++) {
				var curtd = curtr.children().eq(j);
				var rowspan = curtd.attr("rowspan");
				var colspan = curtd.attr("colspan");
				if (colspan > 1) {
					for (var i = 0; i < colspan; i++) {
						var dex = k + i;
						dtr.append(lsttr.children().eq(dex).clone(true));
					}
					k = k + parseInt(colspan);
				} else {
					dtr.append(curtd.clone(true));
				}
			}
			$table.data("head", dtr.html());
			var values = op.data.value;
			var hd = $table.data("head");
			var tr = $("<tr></tr>");
			newtr = $("<tr></tr>");
			tr.append($(hd).clone(true));
			var x = $table.find("tr").size();
			for (var i = 0; i < values.length; i++) {
				for (var j = 0; j < tr.find("td").size(); j++) {
					var td = tr.find("td").eq(j);
					if (td.children().size() > 0) {
						var tdc = $("<td></td>").append(td.children().clone(true));
						newtr.append(tdc);
					} else {
						newtr.append($("<td>" + values[i][j].value + "</td>"));
					}
				}
				var ttr = newtr.clone();
				ttr.data("pkey", op.data.pkey);
				$table.append(ttr);
				newtr = $("<tr></tr>");
			}
		},
		dragRow : function(id,flgId){
			var selectedRow;
			var mytableRow;
			var keyStatus = 0;
			var $col = $(".sFDataInner .specification_table tr:gt(11) td:nth-child(3)");
			$col.mousedown(function(){
				keyStatus = 1;
				mytableRow = $("#"+id+" tr td[rowreq="+$(this).attr("rowreq")+"]").parent();
				//压下鼠标时选取行
				selectedRow = $(this).parent();
				$col.css("cursor","url(../main/img/cursor_hand_on.cur),auto");
				return false;	//防止拖动时选取文本内容，必须和 mousemove 一起使用
			}).mouseenter(function(){
				if(keyStatus == 1){
					$(this).addClass("td_violet_readonly");
				}
			}).mouseleave(function(){
				$(this).removeClass("td_violet_readonly");
			});
			$col.mousemove(function(){
				return false;	//防止拖动时选取文本内容，必须和 mousedown 一起使用
			});
			//释放鼠标键时进行插入
			$col.mouseup(function(){
				if(flgId!=null){
					if($("#"+flgId)){
						$("#"+flgId).val("1");
					}
				}
				keyStatus = 0;
				if(selectedRow){
					if(selectedRow != this){
						$(this).parent().before($(selectedRow)).removeClass('mouseOver'); //插入
						$("#"+id+" tr td[rowreq="+$(this).attr("rowreq")+"]").parent().before($(mytableRow)); //插入
					}
					$col.css("cursor","url(../main/img/cursor_hand.cur),auto");
					$(".sFDataInner .specification_table tr:gt(11) td:nth-child(3)").each(function(i){
						$(this).html(i+1);
					});
					selectedRow = null;
					mytableRow = null;
				}
			});
		},

		dragCol : function(id,flgId){
			var selectedCol;
			var mytableCol;
			var selColgroup;
			var mytableColgroup;
			var $col = $(".sHeaderInner .tableCss tr:eq(10) td:gt(0)");

			var keyStatus = 0;
			$col.mousedown(function(){
				//压下鼠标时选取行
				var selColreq = $(this).attr("colreq");
				selectedCol = $(".sHeaderInner .tableCss tr td[colreq="+selColreq+"]");
				mytableCol = $("#"+id+" tr td[colreq="+selColreq+"]");
				selColgroup = $(".sHeaderInner .tableCss colgroup col[colreq="+selColreq+"]");
				mytableColgroup = $("#"+id+" colgroup col[colreq="+selColreq+"]");
				$col.css("cursor","url(../main/img/cursor_hand_on.cur),auto");
				keyStatus = 1;
				return false;	//防止拖动时选取文本内容，必须和 mousemove 一起使用
			}).mouseenter(function(){
				if(keyStatus == 1){
					$(this).addClass("td_violet_readonly");
				}
			}).mouseleave(function(){
				$(this).removeClass("td_violet_readonly");
			});
			$col.mousemove(function(){
				return false;	//防止拖动时选取文本内容，必须和 mousedown 一起使用
			});
			//释放鼠标键时进行插入
			$col.mouseup(function(){
				if(flgId!=null){
					if($("#"+flgId)){
						$("#"+flgId).val("1");
					}
				}
				keyStatus = 0;
				if(selectedCol){
					if(selectedCol != this){
						var selColreq = $(this).attr("colreq");
						$(".sHeaderInner .tableCss colgroup col[colreq="+selColreq+"]").before(selColgroup);
						$("#"+id+" colgroup col[colreq="+selColreq+"]").before(mytableColgroup);

						$(this).parent().parent().find("tr").each(function(){
							var tds = $(this).find("td");
							for(var i=0;i<tds.length;i++){
								var rowreq = tds.eq(i).attr("rowreq");
								if(tds.eq(i).attr("colreq") == selColreq){
									var selectItem;
									$(selectedCol).each(function(){
										if(rowreq == $(this).attr("rowreq")){
											selectItem = $(this);
										}
									});
									tds.eq(i).before($(selectItem));
								}
							}
						}); //插入

						$("#"+id+" tr").each(function(){
							var tds = $(this).find("td");
							for(var i=0;i<tds.length;i++){
								var rowreq = tds.eq(i).attr("rowreq");
								if(tds.eq(i).attr("colreq") == selColreq){
									var selectItem;
									$(mytableCol).each(function(){
										if(rowreq == $(this).attr("rowreq")){
											selectItem = $(this);
										}
									});
									tds.eq(i).before($(selectItem));
								}
							}
						}); //插入
					}
					$col.css("cursor","url(../main/img/cursor_hand.cur),auto");
					$(".sHeaderInner .tableCss tr:eq(10) td:gt(0)").each(function(i){
						$(this).html(i+1);
					});
					selectedCol = null;
					mytableCol = null;
					selColgroup = null;
					mytableColgroup = null;
				}
			});
		}
	}
})(jQuery);
