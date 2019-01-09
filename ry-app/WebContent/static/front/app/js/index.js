var init = true ;
function timer(opj){
		$(opj).find('ul').animate({
			marginTop : "-2.71rem"
			},500,function(){
			$(this).css({marginTop : "0"}).find("li:first").appendTo(this);
		})
	};
	$(function(){
		//$(".list_conton").eq(0).show();
		var num = $('.notice_active').find('li').length;
		if(num > 1){
		   var time=setInterval('timer(".notice_active")',3500);
			$('.gg_more a').mousemove(function(){
				clearInterval(time);
			}).mouseout(function(){
				time = setInterval('timer(".notice_active")',3500);
			});
		}

		$(".news_ck").click(function(){
			location.href = $(".notice_active .notice_active_ch").children(":input").val();
		})

		var current = 0;
		$(".navigation ul li").on('click',function(){
			$(this).addClass('active').siblings().removeClass('active');
			current = (current+180)%360;
	        $(this).children('img.on').css('transform','rotate('+current+'deg)');

	        var forward =  $(this).attr("data-forward") ;
	        if(forward=='down'){
	        	$(this).attr("data-forward","up") ;
	        }else{
	        	$(this).attr("data-forward","down") ;
	        }
	        index.orderClick($(this)) ;
		});

	 /*	var $div_li =$(".list_bar span");
	    $div_li.click(function(){
	        $(this).addClass("active").siblings().removeClass("active");
	        var index =  $div_li.index(this);	
	        $(".list_conton").eq(index).show().siblings('.list_conton').hide();
	    })*/
		var $div_li =$(".list_bar span");
		   $div_li.click(function(){
			   var key = $(this).attr("data-key");
			   $($(".list_conton li[type="+key +"]")[0]).parent().parent().parent().show().siblings('.list_conton').hide()
		       $(this).addClass("active").siblings().removeClass("active");
		     /*   var index =  $div_li.index(this);   
		       $(".list_conton").eq(index).show().siblings('.list_conton').hide(); */
		   });

	    $(".munu ul li").on('click',function(){
			$(this).addClass('active').siblings().removeClass('active');
		});

		$(".affiche-tab ul li").on('click',function(){
			$(this).addClass('active').siblings().removeClass('active');
	            var index =  $(".affiche-tab ul li").index(this);
	            $(".affiche-cont").eq(index).show().siblings('.affiche-cont').hide();
		});

	    $(".list_bar p").on('click',function(){
	    	window.location.href="top5paihang.html";
	    });

	});


	$.post("/m/json2/indexStatis.html",{},function(data){
		$("#totalUser").html(data.totalUser) ;
	},"json") ;


var index={
		refreshMarket:function(){
			var url =  "/real/indexmarket.html?random=" + Math.round(Math.random() * 100);
			$.post(url,{},function(data){
				var totalAmount = 0 ;
				$.each(data,function(key,value){
					totalAmount+=Number(value.amt);
					$("#"+key+"_total").html(Number(value.total).toFixed(4));
					$("#"+key+"_total_1").html(Number(value.total).toFixed(4));
					$("#"+key+"_amt").html(Number(value.amt));
					$("#"+key+"_max").html(Number(value.max));
					var price = Number(value.high);
					//var kai = Number(value.low)
					var kai = Number(value.kai)
					var symbol = value.symbol;
					//var rose = Math.round(util.accDiv((price-kai),(price+kai))*10000)/10000;
					var rose = Math.round(value.rose * 10000) / 10000 * 100;
//					if(!rose>0){
//					    rose=0;
//					}else{
//						rose = (rose).toFixed(2);
//					}
					if (isNaN(rose)) {
						rose = 0;
					}
					//if (String(rose).split(".")[1] != undefined && String(rose).split(".")[1].length>6) {
						rose = rose.toFixed(2)
					//}
					if(Number(value.rose)>=0){
						
						$("#"+key+"_rose").removeClass("add").addClass("Minus").html('+'+rose+'%');
						$("#"+key+"_rose_1").removeClass("add").addClass("Minus").html('+'+rose+'%');
						$("#"+key+"_price").html(/*value.symbol+*/value.price);
						$("#"+key+"_price_1").html(/*value.symbol+*/value.price);
						
					}else{
						$("#"+key+"_rose").removeClass("Minus").addClass("add").html(rose+'%');
						$("#"+key+"_rose_1").removeClass("Minus").addClass("add").html(rose+'%');
						$("#"+key+"_price").html(/*value.symbol+*/value.price);
						$("#"+key+"_price_1").html(/*value.symbol+*/value.price);
					}
					$("#"+key+"_price").next("#convertInto_RMB").text("￥"+ (value.fpc * value.price).toFixed(2));
					$("#"+key+"_price_1").next("#convertInto_RMB_1").text("￥"+ (value.fpc * value.price).toFixed(2));
					if(Number(value.rose7)>=0){
						$("#"+key+"_rose7").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html('+'+value.rose7+'%');
					}else{
						$("#"+key+"_rose7").removeClass("text-danger").removeClass("text-success").addClass("text-success").html(value.rose7+'%');
					}

				/*$.plot($("#"+key+"_plot"), [{shadowSize:0, data:value.data}],{ grid: { borderWidth: 0}, xaxis: { mode: "time", ticks: false}, yaxis : {tickDecimals: 0, ticks: false},colors:['#e55600']});*/
				});

				$("#totalAmount").html(totalAmount.toFixed(0));
				if(init==true){
					$(".navigation ul li").eq(0).trigger("click");
					init =false ;
				}
			});
		/*	window.setTimeout(function() {
				index.refreshMarket();
			}, 5000);*/
		},
		orderClick:function($this){
			var forward = $this.attr("data-forward") ;
			var name = $this.attr("data-name") ;
			var uls = $this.parent().parent().parent().find('.list_contul') ;
			var lis =uls.find('.tradeClick') ;

			lis.sort(function (item1, item2) {

				var did1 = item1.getAttributeNode("data-id").value;
				var did2 = item2.getAttributeNode("data-id").value ;
				if(name == '1'){
					var price1 = Number($("#"+did1+"_price").html()) ;
					var price2 = Number($("#"+did2+"_price").html()) ;
					if(price1 > price2) {
				        return forward=='up'?1:-1; // 如果是降序排序，返回-1。
				    }else if(price1 === price2) {
				        return 0;
				    }else {
				        return  forward=='up'?-1:1; // 如果是降序排序，返回1。
				    }

				}else if(name == '2'){
					var price1 = Number($("#"+did1+"_total").html()) ;
					var price2 = Number($("#"+did2+"_total").html()) ;
					if(price1 > price2) {
				        return forward=='up'?1:-1; // 如果是降序排序，返回-1。
				    }else if(price1 === price2) {
				        return 0;
				    }else {
				        return  forward=='up'?-1:1; // 如果是降序排序，返回1。
				    }

				}else if(name == '3'){
					var price1 = Number($("#"+did1+"_rose").html()) ;
					var price2 = Number($("#"+did2+"_rose").html()) ;
					if(price1 > price2) {
				        return forward=='up'?1:-1; // 如果是降序排序，返回-1。
				    }else if(price1 === price2) {
				        return 0;
				    }else {
				        return  forward=='up'?-1:1; // 如果是降序排序，返回1。
				    }

				}


		    });
			uls.html('');
			uls.append(lis);

		}
};


var parseUpDown = function (el) {
	 var num = parseFloat($.text([el]).replace('￥', ''), 10);
	    //console.log(num);
	    return num;
}

var parseNum = function (el) {
    var num = parseFloat($.text([el]).replace(/,/g, ''), 10);
    //console.log(num);
    return num;
}

var parsePercent = function (el) {
    var num = parseFloat($.text([el]).replace('%', ''), 10) * ($(el).hasClass('minus') ? -1 : 1);
    //console.log(num);
    return num;
}

var setting = {
       'sbtn1': parseUpDown,
       'sbtn2': parseNum,
       'sbtn3': parseNum,
       'sbtn4': parseNum,
       'sbtn5': parsePercent,
       'sbtn6': parsePercent
   };

$(function(){
	index.refreshMarket();
});