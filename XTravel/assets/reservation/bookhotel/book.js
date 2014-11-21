// JavaScript Document
(function($){  
	
	function parser(obj,type){
	  var optionString = $(obj).attr(type);
	  var option;
	  
	  if(isEmpty(optionString)){
		 return {};
	  }
		if(optionString.indexOf("{")==0&&optionString.indexOf("}")>0){
			option=eval("("+optionString+")");
		}else{
			option=eval("({"+optionString+"})");
		}
		return option;
	 };  
	
	function isEmpty(obj){
		if(null==obj||""==obj||"undefined"==obj||typeof obj=="undefined"){
			return true;
		}
		return false; 
	};
	
	function _init(jq){
		//获取data酒店信息
		var data = parser(jq,'data');
		
		/*<div class="br_left">
			<li class="br_left_name"><b>成都8090酒店</b></li>
			<li class="br_left_price">￥120</li>
		</div>
		<div class="br_right"><span>预定酒店</span></div>
		*/
		
		var left = "<div class='br_left'></div>";
		var right = "<div class='br_right'></div>";		
		var input = "<input type='hidden' value='"+ data.id +"' class='br_link'/>";		
		;
		$(jq).append(left).append(right).append(input);
		
		
		var liArray = new Array();
		liArray.push("<li class='br_left_name'><b>"+data.name+"</b></li>");
		liArray.push("<li class='br_left_price'><b>￥"+data.price+"</b></li>");	
		
		
		$(jq).find("div.br_left").empty().append(liArray.join(""));		
		$(jq).find("div.br_right").empty().append("<span>商户详情</span>");			
		
	}
	
	
	//绑定事件函数
	function bindListener(jq){
		var $content = $(jq).find("div[class=br_right]");		
		$content.bind('click',					   
			function(e){			
			 	//alert($(jq).find("input.br_link").attr("value"));
				var id = $(jq).find("input.br_link").attr("value");
				window.moreinfo.toBussinessDeatils(id);
		});		
	}
	
		
	function toGoodInformation(index){
		var $content = $("li[class='gdid']");
		var id = 0;				
		//alert("加载dddd几次？");
		$content.each(function(ix,element){
				//alert("index"+index+"||ix"+ix);
				if( (index - 1) == ix){
					id = Number($(element).html());		
					//alert("this"+id);
					/*if(window.navigator.platform.indexOf("iPhone", 0) > -1){						
						document.location = "objc://" + "goodsListCallBack" + ":/" + id;						
					} else {
						window.jsi.showDetail(id);
					}
					*/
				}
		});
	}
	
	$.fn.book = function(options){
		
		if( typeof options == 'string'){
			var method = $.fn.book.methods[options];
			if( method ){
				return method(this);
			}
		}
	};
	
	$.fn.book.defaults = {
			
		timeStamp:0,/*标识是否已经循环过，在绑定事件函数时，会遍历所有class属性相同的元素，因此用timeStamp标识使得函数执行一次*/
		bindtimes:0,
	}
	
	$.fn.book.methods={
			init:function(jq){				
				_init(jq);				
			},	
			bind:function(jq){
				bindListener(jq);
			}
	};  
	
})(jQuery);