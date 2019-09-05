var imgUpDwonPath1 = g_images.img_go_up1.src;
var imgUpDwonPath2 = g_images.img_go_up2.src;

/* hide and show some object */
function hideShow(id)
{
	var obj = document.getElementById(id);
	if (obj.style.display == "none")
		obj.style.display = "";
	else
		obj.style.display = "none";				
}

/* swap two images */
function swapImage(imgId, imgPath)
{
	var obj = document.getElementById(imgId);
	obj.src = imgPath;
}
