	

var pagesize = 3;//全局变量，每次请求的数目
var jspKind = 1;
var userid = -1;
var URL ="";
var param = {};
var cateid = -1;

function getArgs(){
    var args = {};
    var match = null;
    var search = decodeURIComponent(location.search.substring(1));    
    var reg = /(?:([^&]+)=([^&]+))/g;
    while((match = reg.exec(search))!==null){    	
        args[match[1]] = match[2];
    }
    return args;
}
function showData(){
	
	var q = "";//attrs[0];
	var sorts = "";// = attrs[1];	
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
	    		pagesize = jsondata.length;
				//初始化list界面
				$(".remment").each(function(){							
					$(this).remment('init');					
				});	 
				
				if(jsondata.length < pagesize){
					$("input.hasMore").attr("value",0);
				}
				//alert($("input.hasMore").attr("value"));	
				myScroll.refresh();				
				//alert(pagesize);
	
}	
	showData();
