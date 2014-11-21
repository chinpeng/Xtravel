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
		//获取data信息
		var data = parser(jq,'data');
		//获取image品图片
		//var image = parser(jq,'image');		
		
		var left = "<div class='r_picture'></div>";
		var right = "<div class='r_content'></div>";		
		$(jq).append(left).append(right);
		
		/*
		<div class="r_picture">		
			<div class="r_p_img"><img  src="img.jpg"/></div>
			<div class="r_p_name"><span>沙拉沙沙拉沙拉</span>拉</div>
		</div>
		<div class="r_content">
			<div class="r_c_title">很漂亮</div>
			<div class="r_c_comment">很漂亮 我很喜欢 很特别很漂亮 我很喜欢 很特别很漂亮 我很喜欢 很特别</div>
			<div class="r_c_time">2014年08月入住 来自去哪网</div>		
		</div>
		*/
		// 添加r_picture内容
		var imageArray = new Array();
		imageArray.push("<div class='r_p_img'><img src='img.jpg'/></div>");
		imageArray.push("<div class='r_p_name'><span>"+data.name+"</span></div>");
		//alert($(jq).find("div[class='remment_left']"));
		$(jq).find("div[class='r_picture']").append(imageArray.join(""));
		
				
		// 添加<div class="r_content"></div>
		var urArray = new Array();
		urArray.push("<div class='r_c_title'><img src='"+data.imgUrl+"'/>");
		//urArray.push("<div class='clear'></div>");
		urArray.push("<span class='item'>口味</span> <span class ='product_score'>"+data.product_score+"</span><span|</span>");
		urArray.push("<span class='item'>环境</span> <span class ='decoration_score'>"+data.decoration_score+"</span><span|</span>");
		urArray.push("<span class='item'>服务</span> <span class ='service_score'>"+data.service_score+"</span><span|</span>");
		urArray.push("</div>");	
		urArray.push("<div class='r_c_comment'>"+data.comment+"</div>");	
		urArray.push("<div class='r_c_time'>"+data.time+"</div>");	
		
		$(jq).find("div[class='r_content']").append(urArray.join(""));	

		
	}
	
	
	
	$.fn.remment = function(options){
		
		if( typeof options == 'string'){
			var method = $.fn.remment.methods[options];
			if( method ){
				return method(this);
			}
		}

	};
	
	$.fn.remment.defaults = {
			
		timeStamp:0,/*标识是否已经循环过，在绑定事件函数时，会遍历所有class属性相同的元素，因此用timeStamp标识使得函数执行一次*/
		bindtimes:0,
	}
	
	$.fn.remment.methods={
			init:function(jq){				
				_init(jq);				
			},	
			bind:function(jq){
				bindListener(jq);
			}
	};  
	
})(jQuery);