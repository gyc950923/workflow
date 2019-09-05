window.onload = function(){
	$('#loading-mask').fadeOut();
}
var _menus ;
var onlyOpenTitle="欢迎使用";//不允许关闭的标签的标题

$(function(){
	 
 	$.ajax({
			url         : "login/getMenuTree",	
			type        : "POST",
			dataType    : 'json',
			async:false,
		success : function(data) {
			//_menus = _menus11;	
			_menus = data;
			InitLeftMenu();
			sysTabClose();
			tabCloseEven();
		},
		error : function(data) {
			$.messager.alert('提示:','获取菜单权限失败','info');
		}
	}); 
	
	

/* 选择TAB时刷新内容
	$('#tabs').tabs({
        onSelect: function (title) {
            var currTab = $('#tabs').tabs('getTab', title);
            var iframe = $(currTab.panel('options').content);

			var src = iframe.attr('src');
			if(src)
				$('#tabs').tabs('update', { tab: currTab, options: { content: sysCreateFrame(src)} });

        }
    });
*/
 	
 	    $( "#tabs" ).tabs({  
 		      activate: function( event, ui ){  
 		        refresh_ifr_page();  
 		      }  
 		    }); 

})

/* 
//初始化左侧
function InitLeftMenu() {
	$("#nav").accordion({animate:false,fit:true,border:false});
	var selectedPanelname = '';
	if(undefined == _menus.menus) return;
	
    $.each(_menus.menus, function(i, n) {
		var menulist ='';
		menulist +='<ul class="navlist">';
        $.each(n.menus, function(j, o) {
			menulist += '<li><div ><a ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div> ';

			if(o.child && o.child.length>0)
			{
				//li.find('div').addClass('icon-arrow');

				menulist += '<ul class="third_ul">';
				$.each(o.child,function(k,p){
					menulist += '<li><div><a ref="'+p.menuid+'" href="#" rel="' + p.url + '" ><span class="icon '+p.icon+'" >&nbsp;</span><span class="nav">' + p.menuname + '</span></a></div> </li>'
				});
				menulist += '</ul>';
			}

			menulist+='</li>';
        })
		menulist += '</ul>';

		$('#nav').accordion('add', {
            title: n.menuname,
            content: menulist,
				border:false,
            iconCls: 'icon ' + n.icon
        });

		if(i==0){
			selectedPanelname =n.menuname;
		}
			
		//alert(menulist);
    });
	$('#nav').accordion('select',selectedPanelname);


	$('.navlist li a').click(function(){
		var tabTitle = $(this).children('.nav').text();

		var url = $(this).attr("rel");
		var menuid = $(this).attr("ref");
		var icon = $(this).find('.icon').attr('class');

		var third = find(menuid);
		if(third && third.child && third.child.length>0)
		{
			$('.third_ul').slideUp();

			var ul =$(this).parent().next();
			if(ul.is(":hidden"))
				ul.slideDown();
			else
				ul.slideUp();



		}
		else{
			addTab(tabTitle,url,icon);
			$('.navlist li div').removeClass("selected");
			$(this).parent().addClass("selected");
		}
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});





	//选中第一个
	//var panels = $('#nav').accordion('panels');
	//var t = panels[0].panel('options').title;
    //$('#nav').accordion('select', t);
}
 */

/* */

//初始化左侧
function InitLeftMenu() {
	$("#nav").accordion({animate:false,height:'100%'});

	var selectedPanelname = '';
	if(undefined == _menus) return;
	
    $.each(_menus, function(i, n) {
		var menulist ='';
		if(null == n.parentId || '' == n.parentId) {
		menulist +='<ul class="navlist">';
        $.each(n.children, function(j, o) {
        	menulist += '<li><div ><a ref="'+o.id+'" href="#" rel="' + o.mUrl + '" ><span class="icon '+o.iconCls+'">&nbsp;</span><span class="nav">' + o.text + '</span></a></div> ';
        	if(o.children && o.children.length>0)
			{
				//li.find('div').addClass('icon-arrow');
        		menulist += getChildrenMenu(o.children);
				/*menulist += '<ul class="third_ul">';
				$.each(o.children,function(k,p){
					menulist += '<li><div><a ref="'+p.id+'" href="#" rel="' + p.mUrl + '" ><span class="icon '+p.iconCls+'" >&nbsp;</span><span class="nav">' + p.text + '</span></a></div> </li>'
				});
				menulist += '</ul>';*/
			}

			menulist+='</li>';
       
        })
		menulist += '</ul>';
		$('#nav').accordion('add', {
            title: n.text,
            content: menulist,
		    border:false,
            iconCls: 'icon ' + n.iconCls
        });

		if(i==0){
			selectedPanelname =n.text;
		}
			
		//alert(menulist); 
		}
    });
	$('#nav').accordion('select',selectedPanelname);


	$('.navlist li a').click(function(){
		var tabTitle = $(this).children('.nav').text();

		var url = $(this).attr("rel");
		var menuid = $(this).attr("ref");
		var icon = $(this).find('.icon').attr('class');

		var third = find(menuid);
		if(third && third.child && third.child.length>0)
		{
			$('.third_ul').slideUp();

			var ul =$(this).parent().next();
			if(ul.is(":hidden"))
				ul.slideDown();
			else
				ul.slideUp();



		}
		else{
			addTab(tabTitle,url,icon);
			$('.navlist li div').removeClass("selected");
			$(this).parent().addClass("selected");
		}
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});

}


/**/
//获取左侧导航的图标
function getIcon(menuid){
	var icon = 'icon ';
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				icon += o.icon;
			}
		 })
	})

	return icon;
}
function getChildrenMenu(data){
	var menulist = '<ul class="third_ul">';
	$.each(data,function(k,p){
		menulist += '<li><div><a ref="'+p.id+'" href="#" rel="' + p.mUrl + '" ><span class="icon '+p.iconCls+'" >&nbsp;</span><span class="nav">' + p.text + '</span></a></div>'
		if(p.children && p.children.length>0){
			menulist += getChildrenMenu(p.children);
		}
		menulist +='</li>';
	});
	menulist += '</ul>';
	return menulist;
}
/*
function find(menuid){
	var obj=null;
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				obj = o;
			}
		 });
	});

	return obj;
}
 */
/* */
function find(menuid){
	var obj=null;
	$.each(_menus, function(i, n) {
		 $.each(n.children, function(j, o) {
		 	if(o.menuid==menuid){
				obj = o;
			}
		 });
	});

	return obj;
}


function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:sysCreateFrame(url),
			closable:true,
			icon:icon
		});
	}else{
		$('#tabs').tabs('select',subtitle);
		$('#mm-tabupdate').click();
	}
	sysTabClose();
}

function sysCreateFrame(url)
{
	var s = '<iframe id="mainFrame" name="mainFrame" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function sysTabClose()
{
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}


//绑定右键菜单事件
function tabCloseEven() {

    $('#mm').menu({
        onClick: function (item) {
            closeTab(item.id);
        }
    });

    return false;
}

function closeTab(action)
{
    var alltabs = $('#tabs').tabs('tabs');
    var currentTab =$('#tabs').tabs('getSelected');
	var allTabtitle = [];
	$.each(alltabs,function(i,n){
		allTabtitle.push($(n).panel('options').title);
	})


    switch (action) {
        case "refresh":
            var iframe = $(currentTab.panel('options').content);
            var src = iframe.attr('src');
            $('#tabs').tabs('update', {
                tab: currentTab,
                options: {
                    content: sysCreateFrame(src)
                }
            })
            break;
        case "close":
            var currtab_title = currentTab.panel('options');
            if(validateTabId(currtab_title.id)){
            	$('#tabs').tabs('close', currtab_title.title);
            }else {
            	$.messager.alert('提示:','欢迎页签不能删除','info');
            }
            break;
        case "closeall":
            $.each(allTabtitle, function (i, n) {
                if (n != onlyOpenTitle){
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeother":
            var currtab_title = currentTab.panel('options').title;
            $.each(allTabtitle, function (i, n) {
                if (n != currtab_title && n != onlyOpenTitle)
				{
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeright":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);

            if (tabIndex == alltabs.length - 1){
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i > tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
        case "closeleft":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);
            if (tabIndex == 1) {
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i < tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
    /*    case "exit":
            $('#closeMenu').menu('hide');
            break;*/
    }
}

/**
 * 判断tabid是否是欢迎首页，如果是欢迎首页将不允许删除
 * */
function validateTabId(id){
	if(null != id && undefined != id && id == 'noclose')
		return false;
	else
		return true;
}


//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}
