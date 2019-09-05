(function($) {
	
    $.getGuid = function (usesplit) {
        // 生成并返回一个GUID
        var guid = "";
        for (var i = 1; i <= 32; i++) {
            var n = Math.floor(Math.random() * 16.0).toString(16);
            guid += n;
            if (usesplit) {
                if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
                    guid += "-";
            }
        }
        return guid;
    };
    
    $.append = function (obj1,obj2){ 
        // 将obj2属性追加到obj1中，同名属性直接覆盖
    	  if (obj2) {
              for (var o in obj2)
                  if (typeof (obj2[o]) == 'object' && obj1[o]
  						&& typeof (obj1[o] == 'object') && !obj1[o].tagName)
                      $.append(obj1[o], obj2[o]);
                  else
                      obj1[o] = obj2[o];
          }
          return obj1;
    };
    
	$.dlg = function(url, options, sender) {
		 if (!options) options = {};
		 //{ content: 'url:user_edit.htm',width:750,height:350,lock: true, parent:this }
		 var opt = {
				 id : $.getGuid(),
			 content: 'url:'+url,
			  width : 750,
			 height : 350,
			   lock : true,
			 parent : this,
			 	 ok : function() {
						  var isClose = $(this.content)[0].save();
						  return isClose;
			 	 	  },
			 cancel : true
		 };
		 
		 $.append(opt, options);
		 
		 var dg = new $.dialog(opt);
		// dg.ShowDialog(); 
	};

})(jQuery);