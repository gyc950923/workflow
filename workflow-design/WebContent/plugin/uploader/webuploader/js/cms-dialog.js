function showDlg(url, arguments, width, height, left, top,resizable)
{
	sFeatures = "dialogHeight:" + height + "px; dialogWidth:" + width +
			   "px;scroll:auto; status:no;resizable:" + resizable + "; help:no";
	if (left != null)
	{
		sFeatures += "; dialogLeft:" + left;
	}
	if (top != null)
	{
		sFeatures += "; dialogTop:" + top;
	}
	return window.showModalDialog(url, arguments, sFeatures);
}

function showModalDlg(url, arguments, width, height, left, top)
{
	return showDlg(url, arguments, width, height, left,"auto");
}

function showBigModalDlg(url, arguments, width, height, left, top)
{
	sFeatures = "dialogHeight:" + height + "px; dialogWidth:" + width +
			   "px; scroll:auto; status:no; resizable:no; help:no";
	if (left != null)
	{
		sFeatures += "; dialogLeft:" + left;
	}
	else
	{
		var iLeft=(window.screen.availWidth-10-width)/2;
		if(iLeft < 0){
			iLeft = 0;
		}
		sFeatures += "; dialogLeft:" + iLeft;
	}
	if (top != null)
	{
		sFeatures += "; dialogTop:" + top;
	}
	else
	{
		var iTop=(window.screen.availHeight-30-height)/2;
		if(iTop < 0){
			iTop = 0;
		}
		sFeatures += "; dialogTop:" + iTop;
	}

	return window.showModalDialog(url, arguments, sFeatures);
}

function showModalDlg(url, arguments, width, height, left, top,resizable)
{
	sFeatures = "dialogHeight:" + height + "px; dialogWidth:" + width +
			   "px;scroll:auto; status:no;resizable:" + resizable + "; help:no";
	if (left != null)
	{
		sFeatures += "; dialogLeft:" + left;
	}
	if (top != null)
	{
		sFeatures += "; dialogTop:" + top;
	}
	return window.showModalDialog(url, arguments, sFeatures);
}

function OpenWindow(url, target, width ,height, left, top, isFullscreen)
{
	var flag = "no";
	if (isFullscreen)	flag = "yes";
	sFeatures = "directories= no, width=" + width + ", height=" + height + ", left=" + left + ", top=" + top
		+ ", fullscreen=" + flag + ",";
	var pop = window.open(url, null, sFeatures);
	pop.focus();
}

function OpenParentWindow(url, target, width ,height, left, top, isFullscreen)
{
	var flag = "no";
	if (isFullscreen)	flag = "yes";
	sFeatures = "directories= no, width=" + width + ", height=" + height + ", left=" + left + ", top=" + top
		+ ", fullscreen=" + flag + ",";
	window.opener.open(null, null, sFeatures);
	var pop = window.open(url, null, sFeatures);
	pop.focus();
}