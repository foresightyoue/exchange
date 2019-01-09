//有确定、取消两个按钮弹出窗(有取消按钮)
function modalCancel(content){
    var oDiv=document.createElement("div");
    oDiv.className="mars1";
    oDiv.innerHTML=
        "<div id='container1'>"+
        "<p id='content1'>"+content+"</p>"+
        "<div class='flex'>"+
        "<div class='cancel1 modalBtn'>取消</div>"+
        "<div class='confirm1 modalBtn'>确定</div>"+
        "</div>"+
        "</div>";
    var oStyle=document.createElement("style");
    oStyle.innerHTML=
        ".mars1{"+"width: 100%;height: 100%;background-color: rgba(0, 0, 0, 0.6);position: fixed;top: 0;left:0;z-index: 1000;"+"}"+
        "#container1{"+"width: 80%;background: white;position: absolute;top: 50%;transform:translateY(-50%);left: 10%;z-index: 2;font-family: PingFang-SC-Medium;border-radius:3px;-webkit-border-radius:3px;margin: 0;"+"}"+
        "#content1{"+"width:100%;font-size: 1.24rem;text-align: center;line-height: 1.86rem;word-break: break-all;font-family: PingFang-SC-Medium;color: #4f4f4f;padding: 2.48rem .93rem;margin:0;box-sizing: border-box;-webkit-box-sizing: border-box;border-bottom:1px solid #ccc;"+"}"+
        ".flex{"+"display:flex;width:100%;font-size: 1.14rem;height:2.7rem;line-height: 2.7rem;"+"}"+
        ".modalBtn{"+" width: 50%;text-align: center;height:2.7rem;line-height: 2.7rem;background: white;color: #a0791a;cursor:pointer;font-size:1.14rem;"+"}"+
        ".confirm1{"+"border-radius:0 0 3px 0;-webkit-border-radius:0 0 3px 0;"+"}"+
        ".cancel1{"+"border-right: 1px solid #ccc;border-radius:0 0 0 3px;-webkit-border-radius:0 0 0 3px;"+"}";
    document.getElementsByTagName('html')[0].getElementsByTagName('head')[0].appendChild(oStyle);
    document.body.insertBefore(oDiv,document.body.children[0]);

   /*
     * $("body").on("click",".confirm1",function(){ $(".mars1").hide(); });
     * $("body").on("click",".cancel1",function(){ $(".mars1").hide(); });
     */
};

var coinCount1 = 4;
var coinCount2 = 4;
var total_buy = 0,total_sell = 0;

if(document.getElementById("coinCount1") != null){
	coinCount1 = document.getElementById("coinCount1").value;
}
if(document.getElementById("coinCount2") != null){
	coinCount2 = document.getElementById("coinCount2").value;
}
var trade = {
	check : 1,
	NumVerify : function(tradeType) {
		var coinshortName = $("#coinshortName").val();
		if (tradeType == 0) {
			var userCnyBalance = document.getElementById("tradebuyprice").value;
			if (userCnyBalance == "") {
				userCnyBalance = 0;
			}
			var tradebuyAmount = document.getElementById("tradebuyamount").value;
			if (tradebuyAmount == "") {
				tradebuyAmount = 0;
			}
			var tradeTurnover = util.accMul(userCnyBalance, tradebuyAmount);
			var tradecnymoney = Number(document.getElementById("toptradecnymoney").innerHTML);
			if (tradeTurnover > tradecnymoney) {
				util.showerrortips("buy-errortips", language["comm.error.tips.41"]);
				return;
			}
			document.getElementById("tradebuyTurnover").innerHTML = util.moneyformat(tradeTurnover, coinCount1);
			util.hideerrortips("buy-errortips");
		} else {
			var usersellCnyBalance = document.getElementById("tradesellprice").value;
			if (usersellCnyBalance == "") {
				usersellCnyBalance = 0;
			}
			var tradesellAmount = document.getElementById("tradesellamount").value;
			if (tradesellAmount == "") {
				tradesellAmount = 0;
			}
			var tradeTurnover = util.accMul(usersellCnyBalance, tradesellAmount);
			var trademtccoin = Number(document.getElementById("toptrademtccoin").innerHTML);
			if (tradesellAmount > trademtccoin) {
				util.showerrortips("sell-errortips", language["comm.error.tips.42"].format(coinshortName));
				return;
			}
			document.getElementById("tradesellTurnover").innerHTML = util.moneyformat(tradeTurnover, coinCount1);
			util.hideerrortips("sell-errortips");
		}
	},
	search : function(begindate, enddate) {
		var url = $("#recordType").val();
		window.location.href = url;
		
	},
	submitTradePwd : function() {
		/*
         * var tradePwd = document.getElementById("tradePwd").value; if
         * (tradePwd != "") { document.getElementById("tradePwd").value =
         * tradePwd; document.getElementById("isopen").value = "false"; }
         */
		// $('#tradepass').modal('hide');
		var tradeType = document.getElementById("tradeType").value;
		
		if($("#limitedType").val()=="0"){
			trade.submitTradeBtcForm(tradeType, false);
		}else{
			trade2.submitTradeBtcForm(tradeType, false);
		}
	},
	submitTradeBtcForm : function(tradeType, flag) {
		$("#limitedType").val(0) ;
		errorele = "";
		if (tradeType == 0) {
			errorele = "buy-errortips";
		} else {
			errorele = "sell-errortips";
		}
		
		var tradePassword = document.getElementById("tradePassword").value;
		var userid = document.getElementById("userid").value;
		var minBuyCount = document.getElementById("minBuyCount").value;
		var authGrade = document.getElementById("authGrade").value;
		var ffamount = document.getElementById("ffAmount").value;
		var buyCny = $("#buyCny").text();
		var sellCny = $("#sellCny").text();
		var zmount =  parseFloat(ffamount) +parseFloat(buyCny);
		var dmount = 1000.0;
		var zzmount = 3000.0;
		var ssmount = 5000.0;
		var fhasImgValidate = document.getElementById("fhasImgValidate").value;
		var fpostImgValidate = document.getElementById("fpostImgValidate").value;
		var faudit = document.getElementById("faudit").value;
		
		
		if(userid ==0 || userid=="0"){
			util.layerAlert("", "您还未登录，立即<a href='/m/user/login.html'>登录</a>", 2);
			return;
		}
		if(authGrade == 0 || authGrade == '0'){
			util.layerAlert("",language["trade.error.tips.12"],4);
			return;
		}
		if(authGrade == 1 || authGrade == '1'){
			if(tradeType == 0){
			   if(parseFloat(buyCny)> dmount && zmount > zzmount){
				  util.layerAlert("",language["trade.error.tips.13"],4);
				  return;
			   }
			}else if(tradeType == 1){
				if(parseFloat(sellCny) > dmount && (parseFloat(ffamount) +parseFloat(sellCny)) > zzmount){
					util.layerAlert("",language["trade.error.tips.13"],4);
					  return;
				}
				
			}
			if(tradeType == 1){
				util.layerAlert("",language["trade.error.tips.22"],4);
				return;
			}
		}
		if("false" == fpostImgValidate){
			util.layerAlert("",language["trade.error.tips.13"],4);
			return;
		}
		if("true" == fpostImgValidate && "false" == fhasImgValidate && faudit == 2){
			util.layerAlert("",language["trade.error.tips.20"],4);
			return;
		}
		/*if(authGrade == 2 || authGrade == '2'){
			if(tradeType == 0){
				if(parseFloat(buyCny)> ssmount){
					if("false" == fhasImgValidate){
						util.layerAlert("",language["trade.error.tips.20"],4);
						return;
					}
					util.layerAlert("",language["trade.error.tips.13"],4);
					return;
				}
			}else if(tradeType == 1){
				if(parseFloat(sellCny)> ssmount){
					if("false" == fhasImgValidate){
						util.layerAlert("",language["trade.error.tips.20"],4);
						return;
					}
					util.layerAlert("",language["trade.error.tips.13"],4);
					return;
				}
			}
		}*/
		/*if(authGrade == 3 || authGrade == '3'){
			if("false"== fhasBankValidate){
				util.layerAlert("",language["trade.error.tips.20"],4);
				return;
			}
		}*/
		if (tradePassword == "false") {
			util.layerAlert("", language["trade.error.tips.21"], 4);
			return;
		}
		var isTelephoneBind = document.getElementById("isTelephoneBind").value;
		if (isTelephoneBind == "false") {
			util.layerAlert("", language["trade.error.tips.11"], 2);
			return;
		}
		var symbol = document.getElementById("symbol").value;
		var coinName = document.getElementById("coinshortName").value;

		if (tradeType == 0) {
			var tradeAmount = document.getElementById("tradebuyamount").value;
			var tradeCnyPrice = document.getElementById("tradebuyprice").value;
		} else {
			var tradeAmount = document.getElementById("tradesellamount").value;
			var tradeCnyPrice = document.getElementById("tradesellprice").value;
		}
		var limited = 0;
		if (tradeType == 0) {
			var tradeTurnover = tradeAmount * tradeCnyPrice;
			if (document.getElementById("toptradecnymoney") != null && Number(document.getElementById("toptradecnymoney").innerHTML) < Number(tradeTurnover)) {
				util.showerrortips(errorele, language["comm.error.tips.41"]);
				return;
			} else {
				util.hideerrortips(errorele);
			}
		} else {
			if (document.getElementById("toptrademtccoin") != null && Number(document.getElementById("toptrademtccoin").innerHTML) < Number(tradeAmount)) {
				util.showerrortips(errorele, language["comm.error.tips.42"].format(coinName));
				return;
			} else {
				util.hideerrortips(errorele);
			}
		}
		var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
		if (!reg.test(tradeAmount)) {
			util.showerrortips(errorele, language["comm.error.tips.43"]);
			return;
		} else {
			util.hideerrortips(errorele);
		}
		if (parseFloat(tradeAmount) < parseFloat(minBuyCount)) {
			util.showerrortips(errorele, language["comm.error.tips.44"].format(minBuyCount, coinName));
			// layer.msg(language["comm.error.tips.44"]);
			return;
		} else {
			util.hideerrortips(errorele);
		}
		if (!reg.test(tradeCnyPrice)) {
			util.showerrortips(errorele, language["comm.error.tips.45"]);
			return;
		} else {
			util.hideerrortips(errorele);
		}
		if (parseFloat(tradeCnyPrice) <= 0) {
			util.showerrortips(errorele, language["trade.error.tips.4"]);
			return;
		} else {
			util.hideerrortips(errorele);
		}
		var total = util.moneyformat(util.accMul(tradeAmount, tradeCnyPrice), coinCount1);
		if (parseFloat(total) <= 0) {
			util.showerrortips(errorele, language["trade.error.tips.5"]);
			return;
		} else {
			util.hideerrortips(errorele);
		}
		var isopen = document.getElementById("isopen").value;
		if (isopen == "true" && flag) {
			document.getElementById("tradeType").value = tradeType;
			/*
             * $('#tradepass').modal({ backdrop : 'static', keyboard : false,
             * show : true });
             */
			trade.submitTradePwd();
			return;
		}
		var tradePwd = "";
		if (document.getElementById("tradePwd") != null) {
			tradePwd = util.trim(document.getElementById("tradePwd").value);
		}
		/*
         * if (tradePwd == "" && isopen == "true") {
         * util.showerrortips(errorele, language["comm.error.tips.46"]);
         * document.getElementById("isopen").value = true; return; } else {
         */
			util.hideerrortips(errorele);
		// }
		
		var fhasRealValidate = document.getElementById("fhasRealValidate").value;
		if(fhasRealValidate == "" || fhasRealValidate =="false" || fhasRealValidate == false){
			util.showerrortips(errorele, "请先提交实名认证.<a href='/user/realCertification.html'>前往实名认证</a>");
			return;
		}
		
		var url = "";
		if (tradeType == 0) {
			url = "/trade/buyBtcSubmit.html?random=" + Math.round(Math.random() * 100);
		} else {
			
			url = "/trade/sellBtcSubmit.html?random=" + Math.round(Math.random() * 100);
		}
		tradePwd = isopen == "true" ? "" : tradePwd;
		var param = {
			tradeAmount : tradeAmount,
			tradeCnyPrice : tradeCnyPrice,
			tradePwd : tradePwd,
			symbol : symbol,
			limited : limited
		};
		var btntext="";
		var btn = "";
		if(tradeType==0){
			btn = document.getElementById("buybtn");
			btntext = btn.innerHTML;
			btn.innerHTML = language["trade.error.tips.6"];
		}else{
			btn = document.getElementById("sellbtn");
			btntext = btn.innerHTML;
			btn.innerHTML = language["trade.error.tips.7"];
		}
		btn.disabled = true;		
		jQuery.post(url, param, function(data) {
			btn.disabled = false;
			btn.innerHTML = btntext;
			util.showerrortips(errorele, data.msg);
			if (data.resultCode == -2) {
				document.getElementById("isopen").value = "true";
			}else if(data.resultCode == 0) {
				util.showerrortips(errorele, language["trade.error.tips.8"]);
				if (tradeType == 0) {
					document.getElementById("tradebuyamount").value="";
					$("#tradebuyTurnover").html("0");
					location.reload();
				} else {
					var tradeAmount = document.getElementById("tradesellamount").value="";
					$("#tradesellTurnover").html("0");
					location.reload();
				}
				window.setTimeout(function() {
					$("#"+errorele).html("");
				}, 200);
			}
		}, "json");
	},
	cancelEntrustBtc: function(id) {
        util.layerConfirm(language["trade.error.tips.9"],
        function(result) {
    		var fhasRealValidate = document.getElementById("fhasRealValidate").value;
    		if(fhasRealValidate == "" || fhasRealValidate =="false" || fhasRealValidate == false){
    			util.layerAlert("", "请先提交实名认证.<a href='/user/realCertification.html'>前往实名认证</a>", 4);
    			return;
    		}
            var url = "/trade/cancelEntrust.html?random=" + Math.round(100 * Math.random()),
            param = {
    			id : id
    		};
            $.post(url, param,
            function(id) {
            	layer.msg(id.msg);
                null != id && (location.reload(true), layer.close(result))
            }).fail(function(){alert("error")})
        });
    },
    cancelAllEntrustBtc: function(id) {
        util.layerConfirm(language["trade.error.tips.10"],
                function(result) {
		    		var fhasRealValidate = document.getElementById("fhasRealValidate").value;
		    		if(fhasRealValidate == "" || fhasRealValidate =="false" || fhasRealValidate == false){
		    			util.layerAlert("", "请先提交实名认证.<a href='/user/realCertification.html'>前往实名认证</a>", 4);
		    			return;
		    		}
                    var url = "/trade/cancelAllEntrust.html?random=" + Math.round(100 * Math.random()),
                    param = {
            			id : id
            		};
                    $.post(url, param,
                    function(id) {
                    	layer.msg(id.msg);
                        null != id && (location.reload(true), layer.close(result))
                    })
                });
    },
	antoTurnover : function(price, money, mtcNum, tradeType) {
		if (tradeType == 0) {
			document.getElementById("tradebuyprice").value = util.moneyformat(price, coinCount1);
			document.getElementById("tradebuyamount").value = util.moneyformat(mtcNum, coinCount2);
			var tradeTurnover = util.moneyformat(util.accMul(price, mtcNum), coinCount1);
// console.log(tradeTurnover);
			var tradecnymoney = util.moneyformat(Number(document.getElementById("toptradecnymoney").innerHTML), coinCount1);
			document.getElementById("tradebuyTurnover").innerHTML = util.moneyformat(tradeTurnover, coinCount1);
			if (parseFloat(tradeTurnover) > parseFloat(tradecnymoney)) {
				util.showerrortips("buy-errortips", language["comm.error.tips.41"]);
				return;
			}
			
			util.hideerrortips("buy-errortips");
		} else {
			var coinName = document.getElementById("coinshortName").value;
			document.getElementById("tradesellprice").value = util.moneyformat(price, coinCount1);
			document.getElementById("tradesellamount").value = util.moneyformat(mtcNum, coinCount2);
			var tradeTurnover = util.accMul(price, mtcNum);
			var trademtccoin = util.moneyformat(Number(document.getElementById("toptrademtccoin").innerHTML), coinCount1);
			document.getElementById("tradesellTurnover").innerHTML = util.moneyformat(tradeTurnover, coinCount1);
			if (parseFloat(mtcNum) > parseFloat(trademtccoin)) {
				util.showerrortips("sell-errortips", language["comm.error.tips.42"].format(coinName));
				return;
			}
			
			util.hideerrortips("sell-errortips");
		}
	},
	entrustInfoTab : function(ele) {
		var type = ele.data().type;
		var title = "";
		var value = ele.data().value;;
		if (value == 0) {
			value = 1;
			title = language["comm.error.tips.47"] + "&nbsp;+";
			$("#fentrustsbody" + type).hide();
		} else {
			value = 0;
			title = language["comm.error.tips.48"] + "&nbsp;-";
			$("#fentrustsbody" + type).show();
		}
		ele.data().value = value;
		ele.html(title);
	},
	entrustLog : function(id) {
		var url = "/trade/entrustLog.html?random=" + Math.round(Math.random() * 100);
		var param = {
			id : id
		};
		jQuery.post(url, param, function(data) {
			if (data != null && data.result == true) {
				var modal = $("#entrustsdetail");
				modal.find('.modal-title').html(data.title);
				modal.find('.modal-body').html(data.content);
				modal.modal('show');
			}
		}, "json");
	},
	onPortion : function(portion, tradeType) {
		portion = util.accDiv(portion, 100);
		if (tradeType == 0) {
			var money = Number(document.getElementById("tradebuyprice").value);
			var tradecnymoney = Number(document.getElementById("toptradecnymoney").innerHTML);
			var mtcNum = util.accDiv(tradecnymoney, money);
			mtcNum = util.accMul(mtcNum, portion);
			var portionMoney = util.accMul(money, mtcNum);
			this.antoTurnover(money, portionMoney, mtcNum, tradeType);
		} else {
			var money = Number(document.getElementById("tradesellprice").value);
			var trademtccoin = Number(document.getElementById("toptrademtccoin").innerHTML);
			mtcNum = util.accMul(trademtccoin, portion);
			var portionMoney = util.accMul(money, mtcNum);
			this.antoTurnover(money, portionMoney, mtcNum, tradeType);
		}
	},
	lastprice : 0,
	fristprice : true,
	autoRefresh : function(page) {
	    
		var symbol = document.getElementById("symbol").value;
		var coinshortName = $("#coinshortName").val();
		var marketPrice = $("#lastprice").text();
		if(marketPrice == null || marketPrice == ""){
			marketPrice = 0;
		}else{
			marketPrice = marketPrice.substring(0,marketPrice.length -1).trim();
		}
		var url = util.globalurl.market;
		var url = "/m/real/marketforapp2.html?symbol={0}&buysellcount={1}&successcount={2}&marketPrice="+marketPrice;
		var buysellcount = 10;
		var successcount = 3;
		var arr_buy = [],arr_sell = [];

		var url = url.format(symbol, buysellcount, successcount) + "&random=" + Math.round(Math.random() * 100);
		$.get(url, function(data) {
			$.each(data.buys, function(key, value) {
				$(".buybox li[page!=" + page + "]").hide();
				$(".buybox li[page=" + page + "]").show()
				if (key >= buysellcount * page || key < (page-1) * buysellcount) {
					return;
				}
				var buyele = $("#buy" + key);
				arr_buy.push(value.amount);
				if (buyele.length == 0) {
					$(".buybox").append('<li style="background:url(/static/front/app/images/buyBg.png) no-repeat scroll right;background-size:'+Number(value.amount)+'% 100%;" page = '+ page +' id="buy' + key + '"  data-type="1" data-money="' + util.moneyformat(Number(value.price), coinCount1) + '" data-num="' + util.moneyformat(Number(value.amount), coinCount2) + '">' + '<span class="buy">'+ (key + 1) + '</span>' + '<span>' + util.moneyformat(Number(value.price), coinCount1) + '</span>' + '<span>' + util.moneyformat(Number(value.amount), coinCount2)+ '</span>' + '</li>');
				} else {
					buyele.data().money = util.moneyformat(Number(value.price), coinCount1);
					buyele.data().num = util.moneyformat(Number(value.amount), coinCount2);
					buyele.children()[1].innerHTML = util.moneyformat(Number(value.price), coinCount1);
					buyele.children()[2].innerHTML = util.moneyformat(Number(value.amount), coinCount2);
					// buyele.children()[3].innerHTML =
                    // util.moneyformat(Number(value.amount)*Number(value.price),
                    // coinCount1);
					
					buyele.css({"background-size": (Number(value.amount)/total_buy)*100+"% 100%"});
				}
			});
			for ( var i = data.buys.length; i < buysellcount; i++) {
				$("#buy" + i).remove();
			}
			$.each(data.sells, function(key, value) {
				$(".sellbox li[page!=" + page + "]").hide();
				$(".sellbox li[page=" + page + "]").show();
				if (key >= buysellcount * page || key < (page-1) * buysellcount) {
					return;
				}
				var sellele = $("#sell" + key);
				arr_sell.push(value.amount);
				if (sellele.length == 0) {
					$(".sellbox").prepend('<li style="background:url(/static/front/app/images/sellBg.png) no-repeat scroll right;background-size:'+Number(value.amount)+'% 100%;" page = '+ page +' id="sell' + key + '" data-type="0" data-money="' + util.moneyformat(Number(value.price), coinCount1) + '" data-num="' + util.moneyformat(Number(value.amount), coinCount2) + '">' + '<span class="sell">'+ (key + 1) + '</span>' + '<span>' + util.moneyformat(Number(value.price), coinCount1) + '</span>' + '<span>' + util.moneyformat(Number(value.amount), coinCount2)+ '</span>' + '</li>');
				} else {
					sellele.data().money = util.moneyformat(Number(value.price), coinCount1);
					sellele.data().num = util.moneyformat(Number(value.amount), coinCount2);
					sellele.children()[1].innerHTML = util.moneyformat(Number(value.price), coinCount1);
					sellele.children()[2].innerHTML = util.moneyformat(Number(value.amount), coinCount2);
					// sellele.children()[3].innerHTML =
                    // util.moneyformat(Number(value.amount)*Number(value.price),
                    // coinCount1);
					
					sellele.css({"background-size": (Number(value.amount)/total_sell)*100+"% 100%"});
				}
			});
			total_buy = Math.max.apply(null,arr_buy);
			total_sell = Math.max.apply(null,arr_sell);
			
			for ( var i = data.sells.length; i < buysellcount; i++) {
				$("#sell" + i).remove();
			}
			var loghtml = "";
			$.each(data.trades, function(key, value) {
				if (key >= successcount) {
					return;
				}
				loghtml += '<li class="list-group-item clearfix">' + '<span class="col-xs-2 padding-clear">' + value.time + '</span>' + '<span class="col-xs-5 text-right padding-clear">' + util.moneyformat(Number(value.price), coinCount1) + '</span>' + '<span class="col-xs-5 text-right padding-clear ' + (value.en_type == 'ask' ? "greentips" : "redtips") + '">' + util.moneyformat(Number(value.amount), coinCount2) + '</span>' + '</li>';
			});
			$("#logbox").html("").append(loghtml);
			if (trade.fristprice) {
				if (data.buys.length <= 0) {
					$("#tradesellprice").val(util.moneyformat(Number(data.p_new), coinCount1));
				} else {
					$("#tradesellprice").val(data.buys[0].price);
				}
				if (data.sells.length <= 0) {
					$("#tradebuyprice").val(util.moneyformat(Number(data.p_new), coinCount1));
				} else {
					$("#tradebuyprice").val(data.sells[0].price);
				}
				trade.fristprice = false;
				getCny();
			}
			$(".buysellmap").on("click", function() {
				var data = $(this).data();
				var type = data.type;
				var money = data.money;
				var num = data.num;
				var tradeAmount = "";
				if (type == 0) {
					tradeAmount = document.getElementById("tradebuyamount").value;
				} else {
					tradeAmount = document.getElementById("tradesellamount").value;
				}
				if (tradeAmount == "") {
					tradeAmount = 0;
				}
				trade.antoTurnover(money, 0, num, type);
			});

			var p_new = util.moneyformat(Number(data.p_new), coinCount1);
			$("#el").html(p_new);
			$("#now_low").html(Number(data.low));
			$("#now_high").html(Number(data.high));
			$("#now_total").html(Number(data.vol));

			$("#marketPrice").html(p_new);
			
			if (data.roseStatus == 1) {
				$("#lastprice").html(p_new+"<img alt='' src='/static/front/app/images/trend_up.png'>");
				$("#lastprice").parent().removeClass("redtips").addClass("greentips");
			} else if (data.roseStatus == -1) {
				$("#lastprice").html(p_new+"<img alt='' src='/static/front/app/images/trend_down.png'>");
				$("#lastprice").parent().removeClass("greentips").addClass("redtips");
			}else{
                $("#lastprice").html(p_new);
                $("#lastprice").parent().removeClass("redtips");
			}
			trade.lastprice = p_new;
		}, "json");
		url = "/real/userassets.html?random=" + Math.round(Math.random() * 100);
		$.get(url, {
			symbol : symbol
		}, function(data) {
			if (data != "") {
				$("#toptradecnymoney").html(util.moneyformat(Number(data.availableCny), coinCount1));
				$("#toptrademtccoin").html(util.moneyformat(Number(data.availableCoin), coinCount1));
				$("#toptradelevercny").html(util.moneyformat(Number(data.frozenCny), coinCount1));
				$("#toptradelevercoin").html(util.moneyformat(Number(data.frozenCoin), coinCount1));
			}
		}, "json");

		var fentruststitle = $(".fentruststitle");
		var displaytype0, displaytype1, displayvalue0, displayvalue1;
		$.each(fentruststitle, function(index, key) {
			key = $(key);
			if (index == 0) {
				displaytype0 = key.data('type');
				displayvalue0 = key.data('value');
			}
			if (index == 1) {
				displaytype1 = key.data('type');
				displayvalue1 = key.data('value');
			}
		});
		
		window.setTimeout(function() {
			trade.autoRefresh($("#grade").val());
		}, 10000);
	}
};

$(function() {
	if(document.getElementById("tradesellTurnover") != null){
		document.getElementById("tradesellTurnover").value=0;
	}
	if(document.getElementById("tradebuyTurnover") != null){
		document.getElementById("tradebuyTurnover").value=0;
	}
	if(document.getElementById("tradebuyamount") != null){
		/* document.getElementById("tradebuyamount").value=0; */
	}
	if(document.getElementById("tradesellamount") != null){
		/* document.getElementById("tradesellamount").value=0; */
	}

	$("#tradebuyprice").on("keyup", function() {
		trade.NumVerify(0);
	}).on("keypress", function(event) {
		return util.VerifyKeypress(this, event, coinCount1);
	}).on("click", function() {
		this.focus();
// this.select();
	});
	$("#tradesellprice").on("keyup", function() {
		trade.NumVerify(1);
	}).on("keypress", function(event) {
		return util.VerifyKeypress(this, event, coinCount1);
	}).on("click", function() {
		this.focus();
// this.select();
	});
	$("#tradebuyamount").on("keyup", function() {
		trade.NumVerify(0);
	}).on("keypress", function(event) {
		return util.VerifyKeypress(this, event, coinCount2);
	}).on("click", function() {
		this.focus();
// this.select();
	});
	$("#tradesellamount").on("keyup", function() {
		trade.NumVerify(1);
	}).on("keypress", function(event) {
		return util.VerifyKeypress(this, event, coinCount2);
	}).on("click", function() {
		this.focus();
// this.select();
	});
	/*
     * $("#buybtn").on("click", function() { trade.submitTradeBtcForm(0, true);
     * });
     */
	$("#buybtn").on("click", function() {
		Alert("提示","确定购买吗",null,function(){
			trade.submitTradeBtcForm(0, true);
	     },true);
        
});
	$("#sellbtn").on("click", function() {
		Alert("提示","确定卖出吗",null,function(){
			trade.submitTradeBtcForm(1, true);
		},true);
	});
	$("#modalbtn").on("click", function() {
		trade.submitTradePwd();
	});
	$(".tradecancel").on("click", function() {
		var id = $(this).data().value;
		trade.cancelEntrustBtc(id);
	});
	$(".allcancel").on("click", function() {
		var id = $(this).data().value;
		trade.cancelAllEntrustBtc(id);
	});
	$(".tradelook").on("click", function() {
		var id = $(this).data().value;
		trade.entrustLog(id);
	});
	$(".buysellmap").on("click", function() {
		var data = $(this).data();
		var type = data.type;
		var money = data.money;
		var num = data.num;
		var tradeAmount = "";
		if (type == 0) {
			tradeAmount = document.getElementById("tradebuyamount").value;
		} else {
			tradeAmount = document.getElementById("tradesellamount").value;
		}
		if (tradeAmount == "") {
			tradeAmount = 0;
		}
		trade.antoTurnover(money, 0, num, type);
	});
	$(".fentruststitle").on("click", function() {
		trade.entrustInfoTab($(this));
	});
	
	$("#buyslider").on("change", function(e, val) {
		trade.onPortion(val, 0);
	});
	$("#sellslider").on("change", function(e, val) {
		trade.onPortion(val, 1);
	});
	
	if(document.getElementById("tradesellTurnover") != null){
		trade.autoRefresh($("#grade").val());
	}
	
	$('#tradepass').on('shown.bs.modal', function (e) {
		util.callbackEnter(trade.submitTradePwd);
	});
	$('#tradepass').on('hidden.bs.modal', function (e) {
		document.onkeydown=function(){};
	});
	$("#recordType").change(function() {
		trade.search();
	});

});

$(".databtn").click(function(){
	if($(this).data("type")==1){
		$("#buypricediv").show() ;
		$("#buymarketdiv").hide() ;
		$(this).addClass("datatime-sel") ;
		$(this).siblings().removeClass("datatime-sel") ;
	}else if($(this).data("type")==2){
		$("#buypricediv").hide() ;
		$("#buymarketdiv").show() ;
		$(this).addClass("datatime-sel") ;
		$(this).siblings().removeClass("datatime-sel") ;
	}else if($(this).data("type")==3){
		$("#sellpricediv").show() ;
		$("#sellmarketdiv").hide() ;
		$(this).addClass("datatime-sel") ;
		$(this).siblings().removeClass("datatime-sel") ;
	}else if($(this).data("type")==4){
		$("#sellpricediv").hide() ;
		$("#sellmarketdiv").show() ;
		$(this).addClass("datatime-sel") ;
		$(this).siblings().removeClass("datatime-sel") ;
	}
}) ;