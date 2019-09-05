// 2006/01/31 created by wenliming

var g_top_imgUpDwonPath1 = g_images.img_go_up1.src;
var g_top_imgUpDwonPath2 = g_images.img_go_up2.src;
var g_search_imgUpDwonPath1 = g_images.img_go_up1.src;
var g_search_imgUpDwonPath2 = g_images.img_go_up2.src;


function hideSearchPanel_another(isHide)
{
	var searchPanel = document.getElementById("edit_area_another");
	var img = document.getElementById("imgSearchUpDawnTopAnother");
	if (searchPanel == null || img == null) return;
	
	if(searchPanel.style.display == "")
	{
		searchPanel.style.display = "none";
		img.src="../main/img/go_down1.gif";	
	}
	else
	{
		searchPanel.style.display = "";
		img.src="../main/img/go_up1.gif";	
	}
	
	
}


function isTopHide()
{
	if (parent.fsOne == null) return false;
	var rows = parent.fsOne.rows.split(",");
	return rows[1] == 0;
}

function isSearchPanelHide()
{
	var searchPanel = document.getElementById("edit_area");
	if (searchPanel == null) return false;
	return searchPanel.style.display == "none";

}


function getTopString(isHide, isPagination)
{
	if (parent.fsOne == null) return null;
	var result = "";
	var rows = parent.fsOne.rows.split(",");
	if (isPagination)
	{
		if (isHide) result = rows[0] + ", 0, " + rows[2] + "," + rows[3];
		else result = "0,59,48,*";
		
	}
	else
	{
		if (isHide) result = rows[0] + ", 0, " + rows[2];
		else result = "0,59,*";
	}
	return result;
}

function hideTop(isHide, isPagination)
{
	var img = document.getElementById("imgTopUpDawnTop");
	if (parent.fsOne == null || img == null) return;
	
	var isNeedHide = false;
	var rows = parent.fsOne.rows.split(",");
	
	if (isHide != null)
		isNeedHide = isHide;
	else
	{
		isNeedHide = !isTopHide();
	}
	parent.fsOne.rows = getTopString(isNeedHide, isPagination);
	
	if (isNeedHide)
	{
		g_top_imgUpDwonPath1 = g_images.img_go_up1.src;
		g_top_imgUpDwonPath2 = g_images.img_go_up2.src;
		img.src = g_images.img_go_up1.src;
	}
	else
	{
		g_top_imgUpDwonPath1 = g_images.img_go_down1.src;
		g_top_imgUpDwonPath2 = g_images.img_go_down2.src;
		img.src = g_images.img_go_down1.src;
	}
}

function hideSearchPanel(isHide)

{	
	var searchPanel = document.getElementById("edit_area");
	var img = document.getElementById("imgSearchUpDawnTop");
	if (searchPanel == null || img == null) return;
	
	var isNeedHide = false;
	if (isHide != null)
	{
		isNeedHide = isHide;
	}
	else
	{
		isNeedHide = !isSearchPanelHide();
	}

	if (isNeedHide)
	{
		searchPanel.style.display = "none";
		g_search_imgUpDwonPath1 = g_images.img_go_down1.src;
		g_search_imgUpDwonPath2 = g_images.img_go_down2.src;
		img.src = g_images.img_go_down1.src;

	}
	else
	{
		searchPanel.style.display = "";
		g_search_imgUpDwonPath1 = g_images.img_go_up1.src;
		g_search_imgUpDwonPath2 = g_images.img_go_up2.src;		
		img.src = g_images.img_go_up1.src;

	}
}


function hideAll(isHide)
{
	var isNeedHide = false;
	if (isHide != null)
		isNeedHide = isHide;
	else
	 	isNeedHide = !(isTopHide() || isSearchPanelHide()); 
	hideTop(isNeedHide);
	hideSearchPanel(isNeedHide);
}