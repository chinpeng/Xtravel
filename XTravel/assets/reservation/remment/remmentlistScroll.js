// JavaScript Document
var myScroll,
	pullDownEl, pullDownOffset,
	pullUpEl, pullUpOffset,
	generatedCount = 0;
var start = 0;
/**
 * 下拉刷新 （自定义实现此方法）
 * myScroll.refresh();		// 数据加载完成后，调用界面更新方法
 */
function pullDownAction () {
	setTimeout(function () {	// <-- Simulate network congestion, remove setTimeout from production!
	
		myScroll.refresh();		//数据加载完成后，调用界面更新方法   Remember to refresh when contents are loaded (ie: on ajax completion)
		
	}, 0);	// <-- Simulate network congestion, remove setTimeout from production!
}

/**
 * 滚动翻页 （自定义实现此方法）
 * 
 */
function pullUpAction () {
	
	var q = $("input.q_hidden").attr("value");
	var sorts = $("input.sorts_hidden").attr("value");
	var query_times = Number($("input.query_times").attr("value"));
	var start = query_times * pagesize;	
	var hasMore = Number($("input.hasMore").attr("value"));	
	
	if( hasMore == 1){
		var test = eval("("+window.comment.getCommentInfo()+")");	
		var jsondata = test.reviews;
	    			$.each(jsondata, function(i,item){	    			    
						var listArray = new Array();
						listArray.push("<li>");
						listArray.push("<div class='remment' ");						
						//listArray.push("image=\"{src:''}\" ");
						listArray.push("data=\"{name:'"+item.user_nickname+"',"+"imgUrl:'"+item.rating_img_url+"',"+"comment:'"+item.text_excerpt+"',"+"time:'"+item.created_time+"',"+"product_score:'"+item.product_rating+"',"+"decoration_score:'"+item.decoration_rating+"',"+"service_score:'"+item.service_rating+"'}\">");			
						listArray.push("</div>");
						listArray.push("<div class='clear'></div>");
						listArray.push("</li>");			
						$("ul.thelist").append(listArray.join(""));				
					})
	    		
				var query_times = Number($("input.query_times").attr("value"));
	    		var start = query_times * pagesize;
				query_times = query_times + 1;
		    	$("input.query_times").attr("value",String(query_times));
				var kk = 0;
	    		$(".remment").each(function(){	
	    			if( kk >= start){
	    				
	    				$(this).remment('init');
	    				//$(this).GoodList('bind');
	    			}		
	    			kk = kk + 1;
	    		});
				
	    		if(jsondata.length == pagesize){
	    			query_times = query_times + 1;
		    		$("input.query_times").attr("value",String(query_times));
	    		}else{
	    			$("input.hasMore").attr("value",0);
	    		}	
	    		myScroll.refresh();	
	}else{
		myScroll.refresh();		
	}
				
	/*var el, li, i;	
	var q = $("input.q_hidden").attr("value");
	var sorts = $("input.sorts_hidden").attr("value");
	var query_times = Number($("input.query_times").attr("value"));
	var start = query_times * pagesize;	
	var hasMore = Number($("input.hasMore").attr("value"));
	//更改起始值
	param.start = start;
		
	if( hasMore == 1){
		$.ajax({
			type : "post",	    
	    url : URL,	    	  
	    data : param,	    
	    error : function(jqXHR, textStatus, errorThrown) {
	    			console.log("加载失败。。。");
	            },
	    success : function(jsdata, textStatus, jqXHR) {	    	
	    	var data = {};
	    	if(jspKind == 3){
	    		data = eval("("+jsdata+")");	    		
	    	}else{
	    		data = eval(jsdata);	    		
	    	}	
	    		
	    			//加载数据
		    		$.each(data, function(i,item){						
						var listArray = new Array();						
						listArray.push("<li>");
						listArray.push("<div class='GoodList' ");						
						listArray.push("image=\"{src:'"+ECBase.albumPath+"/"+item.goods_image+"'}\" ");
						listArray.push("data=\"{introduce:'"+item.goods_name+"',"+"price:'"+item.goods_price+"',"+"gdid:'"+item.goods_id+"',"+"foucscount:'"+item.goods_foucscount+"',"+"salecount:'"+item.goods_salecount+"'}\">");			
						listArray.push("</div>");
						listArray.push("</li>");			
						$("ul.thelist").append(listArray.join(""));						
					});
	    		   		
	    			    			    		
	    		//重新初始化列表
	    		var kk = 0;
	    		$(".GoodList").each(function(){	
	    			if( kk >= start){
	    				
	    				$(this).GoodList('init');
	    				$(this).GoodList('bind');
	    			}		
	    			kk = kk + 1;
	    		});
	    		//alert("333");
	    		myScroll.refresh();		// 数据加载完成后，调用界面更新方法
	    		if(data.length == pagesize){
	    			query_times = query_times + 1;
		    		$("input.query_times").attr("value",String(query_times));
	    		}else{
	    			$("input.hasMore").attr("value",0);
	    		}	    		
	    		
	    	}
		});
	}else{
		myScroll.refresh();
	}	*/	
}

/**
 * 初始化iScroll控件
 */
function loaded() {
	pullDownEl = document.getElementById('pullDown');
	pullDownOffset = pullDownEl.offsetHeight;
	pullUpEl = document.getElementById('pullUp');	
	pullUpOffset = pullUpEl.offsetHeight;
	
	myScroll = new iScroll('wrapper', {
		scrollbarClass: 'myScrollbar', /* 重要样式 */
		useTransition: false, /* 此属性不知用意，本人从true改为false */
		topOffset: pullDownOffset,
		onRefresh: function () {
			if (pullDownEl.className.match('loading')) {
				pullDownEl.className = '';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '';
			} else if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '';
			}
		},
		onScrollMove: function () {
			
			if (this.y > 20 && !pullDownEl.className.match('flip')) {
				pullDownEl.className = 'flip';
				//pullDownEl.querySelector('.pullDownLabel').innerHTML = '松手开始更新...';
				this.minScrollY = 0;
			} else if (this.y < 50 && pullDownEl.className.match('flip')) {
				pullDownEl.className = '';
				//pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
				this.minScrollY = -pullDownOffset;
			} else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
				//alert("ddd");
				pullUpEl.className = 'flip';
				//pullUpEl.querySelector('.pullUpLabel').innerHTML = '松手开始更新...';
				this.maxScrollY = this.maxScrollY;
			} else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
				//alert("jjj");
				pullUpEl.className = '';
				//pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
				this.maxScrollY = pullUpOffset;
			}
		},
		onScrollEnd: function () {
			
			$("input.scrollY").attr("value",this.y);
			if (pullDownEl.className.match('flip')) {
				pullDownEl.className = 'loading';
				//pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';				
				pullDownAction();	// Execute custom function (ajax call?)
			} else if (pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				//pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';				
				pullUpAction();	// Execute custom function (ajax call?)
			}
		}
	});
	
	//setTimeout(function () { document.getElementById('wrapper').style.left = '0'; }, 800);
	document.getElementById('wrapper').style.left = '0';
}

//初始化绑定iScroll控件 
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
document.addEventListener('DOMContentLoaded', loaded, false); 