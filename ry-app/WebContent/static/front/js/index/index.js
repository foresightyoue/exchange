var login={
		indexLoginOnblur:function () {
			
			
			
		    var uName = document.getElementById("indexLoginName").value;
		    if (!util.checkEmail(uName) && !util.checkMobile(uName)) {
		        util.showerrortips("indexLoginTips",language["comm.error.tips.1"]);
		    } else {
		    	util.hideerrortips("indexLoginTips");
		    }
		},
		loginIndexSubmit:function () {
		    util.hideerrortips("indexLoginTips");
		    var url = "/user/login/index.html?random=" + Math.round(Math.random() * 100);
		    var uName = document.getElementById("indexLoginName").value;
		    var pWord = document.getElementById("indexLoginPwd").value;
		    /* var imgCode = document.getElementById("index-imgCode").value; */
		    var longLogin = 0;
		    if (util.checkEmail(uName)) {
		        longLogin = 1;
		    }
		    if (!util.checkEmail(uName) && !util.checkMobile(uName)) {
		    	util.showerrortips("indexLoginTips", language["comm.error.tips.1"]);
		        return
		    }
		    if (pWord == "") {
		    	util.showerrortips("indexLoginTips", language["comm.error.tips.2"]);
		        return;
		    } else if (pWord.length < 6) {
		    	util.showerrortips("indexLoginTips", language["comm.error.tips.3"]);
		        return;
		    }
		    /*
             * if(imgCode == ""){ util.showerrortips("indexLoginTips",
             * language["comm.error.tips.28"]); return; }
             */
		    var param = {
		        loginName: uName,
		        password: pWord,
		        type: longLogin
		        /* imgCode: imgCode */
		    };
		    verify(function(){
			    jQuery.post(url, param, function (data) {
			        if (data.code == 0) {
			            if (document.getElementById("forwardUrl") != null && document.getElementById("forwardUrl").value != "") {
			                var forward = document.getElementById("forwardUrl").value;
			                forward = decodeURI(forward);
			                window.location.href = forward;
			            } else {
			                var whref = document.location.href;
			                if (whref.indexOf("#") != -1) {
			                    whref = whref.substring(0, whref.indexOf("#"));
			                }
			                window.location.href = whref;
			            }
			        } else if (data.code <0) {
			        	util.showerrortips("indexLoginTips", data.msg);
			            document.getElementById("indexLoginPwd").value = "";
			        }
			    },"json");
		    });
		},
		refreshMarket:function(){
			
			
			var url="/real/indexmarket.html?random=" + Math.round(Math.random() * 100);
// var market =document.getElementById("#BTC_market").innerText;
// alert(market);
// if(market=="BTC"){
// $("#marketinput").val(market);
// }else if(market=="ETH"){
// $("#marketinput").val(market);
// }
			
			$(function(){
				
// var
// areas=document.getElementsByClassName("active")[0].dataset.key.toLowerCase();
// $("#area").val(areas)
				
				
// $.ajax({
// url:"http://api.zb.com/data/v1/ticker?market="+areas+"_qc",
// type:"get",
// data:{},
// dataType:"json",
// success:function(data){
//					
// var lasts=data.ticker.last;
// $("#last").val(data.ticker.last)
//					
					
					
					$.post(url,{},function(data){
						$.each(data,function(key,value){
							$("#"+key+"_total").html(Number(value.total));
							$("#"+key+"_amt").html(Number(value.amt));
							$("#"+key+"_max").html(Number(value.max));
							$("#"+key+"_high").html(Number(value.high));
							$("#"+key+"_low").html(Number(value.low));
							var price = Number(value.high);
							// var kai = Number(value.low);
							var kai = Number(value.kai);
							// var rose =
                            // Math.round(util.accDiv((price-kai),(price+kai))*10000)/10000;
							// var rose =
                            // Math.round(util.accDiv((price-kai),(kai))*10000)/10000;
							var rose = Math.round(value.rose * 10000) / 10000 * 100;
							// if(!rose>0){
							    // rose=0;
							// }else{
								// rose = (rose).toFixed(2);
							// }
							if (isNaN(rose)) {
								rose = 0;
							}
							if (String(rose).split(".")[1] != undefined && String(rose).split(".")[1].length>6) {
								rose = rose.toFixed(6)
							}
							if(rose>=0){
								/*
                                 * $("#"+key+"_rose").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html('+'+rose+'%');
                                 * $("#"+key+"_price").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html(value.price);
                                 */
								$("#"+key+"_rose").removeClass("text-success").removeClass("text-danger").addClass("text-success").html('+'+rose+'%');
								$("#"+key+"_price").removeClass("text-success").removeClass("text-danger").addClass("text-success").html(value.price);

								// $("#"+key+"_price").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html(value.price+'<div
								// id="last" style=" margin-top: -50px;
								// ">'+value.fpc+'</div>');

								var rate = Number($("#rate").val());
		                        $("#"+key+"_priceCNY").removeClass("text-success").removeClass("text-danger").addClass("text-success").html(value.price * rate);
							}else{
		                        var rate = Number($("#rate").val());
								$("#"+key+"_rose").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html(rose+'%');
								$("#"+key+"_price").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html(value.price);
		                        var rate = Number($("#rate").val());
		                        $("#"+key+"_priceCNY").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html(value.price *  rate);
							}
							
							if(Number(value.rose7)>=0){
								$("#"+key+"_rose7").removeClass("text-danger").removeClass("text-success").addClass("text-success").html('+'+value.rose7+'%');
							}else{
								$("#"+key+"_rose7").removeClass("text-danger").removeClass("text-success").addClass("text-danger").html(value.rose7+'%');
							}
// $.plot($("#"+key+"_plot"),
// [{shadowSize:0,
// data: value.fli }],
// { grid: { borderWidth: 0},
// xaxis: {mode: "time", ticks: false},
// yaxis : {tickDecimals:0, ticks: false},
// colors:['#a02e2a']});
							// alert(value.fli);
							if(value.fli.length==0){
								$.plot($("#"+key+"_plot"), [{shadowSize:0,data: [[3, 1], [5, 1], [7, 1]] }],{ grid: { borderWidth: 0}, xaxis: { mode: "time", ticks: false}, yaxis : {tickDecimals: 0, ticks: false},colors:['#a02e2a']});
							}else{
							$.plot($("#"+key+"_plot"), [{shadowSize:0,data: value.fli }],{ grid: { borderWidth: 0}, xaxis: { mode: "time", ticks: false}, yaxis : {tickDecimals: 0, ticks: false},colors:['#a02e2a']});
							}
						});
					});
					
// }
// });
			});
			

			
			
			
		/*	window.setTimeout(function() {
				login.refreshMarket();
			}, 60000);*/
		},	
		loginerror:function(){
			var errormsg =$("#errormsg").val();
			if(errormsg!="" && errormsg!="/"){
				util.showerrortips("", errormsg);
			}
		},
	    switchCoin: function() {
	        $(".currency").on("click",
	        function() {
	            var a = $(this);
	            var type = a.data().type;
	            var max = 3;
	            for(var i=1;i<=max;i++){
	            	if(i == type){
	            		 $("."+i+"_qu").removeClass("hide");
	            		 $("#"+i+"_qu_t").addClass("selectTag").removeClass("currency");
	            	}else{
	            		 $("."+i+"_qu").addClass("hide");
	            		 $("#"+i+"_qu_t").addClass("currency").removeClass("selectTag");
	            	}
	            	
	            }
	        })
	    }
};



var parseUpDown = function (el) {
	 var num = parseFloat($.text([el]).replace('￥', ''), 10);
	    // console.log(num);
	    return num;
}

var parseNum = function (el) {
    var num = parseFloat($.text([el]).replace(/,/g, ''), 10);
    // console.log(num);
    return num;
}

var parsePercent = function (el) {
    var num = parseFloat($.text([el]).replace('%', ''), 10) * ($(el).hasClass('minus') ? -1 : 1);
    // console.log(num);
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
	login.loginerror();
	$("#indexLoginPwd").on("focus",function(){
		login.indexLoginOnblur();
		util.callbackEnter(login.loginIndexSubmit);
	});
	$("#loginbtn").on("click",function(){
		login.loginIndexSubmit();
	});
	login.switchCoin();
	var v = $('#alert').val();
    if(v == 1){
    	var modal = $("#msgdetail");
    	modal.modal('show');
    }
	
	login.refreshMarket();
	$(".homepage-lump .col-sm-2").hover(function(){
		$(this).addClass("current")
		},function(){
			$(this).removeClass("current")
			});
	
    $("table[name='main-table']").each(function () {
        $(this).find('.sort').each(function (index, el) {
            var $th = $(this);
            var inverse = false;
            var thIndex = $th.parent().index();
            index++;

            $th.click(function () {
                $th.closest("table").find('td').filter(function () {
                    return $(this).index() === thIndex;
                }).sortElements(function (a, b) {
                    return setting['sbtn' + index](a) > setting['sbtn' + index](b) ?
                      inverse ? -1 : 1
                      : inverse ? 1 : -1;
                }, function () {
                    return this.parentNode;
                });
                inverse = !inverse;
            });
        });
    });

    $(".flexslider").flexslider({
        directionNav: true,
        pauseOnAction: false
    });
    
    $(".trade-tab").on("click",function() {
        var a = $(this);
        var key = a.data().key;
        $(".market-con").css('display','none'); 
        $("."+key+"_market_list").css('display','block'); 
        $(".trade-tab").removeClass("active");
        $("#"+key+"_market").addClass("active");
    });
    
   
    
    
    
    
});