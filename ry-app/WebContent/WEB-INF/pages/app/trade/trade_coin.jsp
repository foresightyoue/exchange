<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
    String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path;
%>
<!DOCTYPE html>
<html style="font-size: 16px;">
<head>
<meta charset="utf-8" />
<meta name="viewport"
    content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
<base href="${basePath}"/>
<title>交易中心</title>
<link rel="stylesheet" href="/static/front/app/css/css.css" />
<link rel="stylesheet" href="/static/front/app/css/jquery.range.css" />
<link rel="stylesheet" href="/static/front/app/css/style.css" />
<link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
<!-- <link rel="stylesheet" href="/static/front/app/css/mobiscroll-2.13.2.full.min.css" /> -->
<script type="text/javascript"
    src="/static/front/app/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript"
    src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/jquery.range.js"></script>
<!-- <script type="text/javascript" src="/static/front/app/js/mobiscroll-2.13.2.full.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/mobi.js"></script> -->
<style type="text/css">
  body{
	       background: #fff !important;
	       font-family: "宋体";
	    }
		.proportion_bg ul li span{
			width: 1.7rem !important;
			height: 2.3rem !important;
			line-height: 2.3rem;
		}
		.marketNew-priceItem{
			display: flex;
			justify-content:space-between;
		}
		#marketDepthBuy .depth-des.market-font-buy,#marketDepthBuy .depth-price{
		    color:#03C087;
		}
		.depth-amount{
			color: #999;
		}
		.depth-des.market-font-sell,.depth-price{
		    color:#E76D42;
		}
		.record-info {
		    padding: 0.62rem;
		    text-align: center;
		}
		.centre-cont{
		    line-height: 1.5rem;
		}
		.now_price{
		    padding-left: .62rem;
		    /* padding-top: .5rem;
		    color: #999; */
		    box-sizing: border-box;
		    -webkit-box-sizing: border-box;
		    -moz-box-sizing: border-box;
		    -ms-box-sizing: border-box;
		    -o-box-sizing: border-box;
		    height: 2.5rem;
		    line-height: 2.5rem;
		    font-weight: bold;
		    color: #da2e22;
		}
		#lastprice{
		    font-size: 1.2rem;
		}
		.centre-right{
		    padding-bottom: .62rem;
		    width: 100%;
		    line-height: 2.85rem;
		}
		.centre_bi select{
		    color: #1f3f59;
		    font-weight: bold;
		    font-size: 1.1rem;
		    appearance: button !important;
		    -webkit-appearance: button !important;
		    padding-left: 0.2rem !important;
		    background: #fff !important;
		}
		.centre_bi select:focus {
		    border: 0;
		    outline: none;
		}
		.centre-tab{
		    border: 0;
		    margin:0;
		    padding:0.5rem;
		}
		.centre-tab ul li{
		    border-right: 0;
		    font-size: 1rem;
		}
		.centre-tab ul li.active{
		    background: #186ed5;
            color: #fff;
        }
        .centre-tab ul li:nth-child(2).active{
            /* background: #03C087; */
            background: transparent !important;
            color: #fa8221 !important;
            border: 1px solid #fa8221 !important;
        }
        .centre-tab ul li:nth-child(1).active{
            /* background: #E76D42 !important; */
            background: transparent !important;
            color: #1db677 !important;
            border: 1px solid #1db677 !important;
        }
        .centre-tab ul li a{
            width: 100%;
            height: 100%;
            display: inline-block;
        }
        .centre-tab ul .active a{
            color: white;
        }
        .exchange{ 
                font-size: 1rem;
                color: #808080;
        }
        .exchange .unit{ 
                font-size: .87rem;
                color: #808080;
        }
        .ml_10{
            margin-left: .4rem;
        }
        #tradebuyTurnover,#tradesellTurnover{
            width: 58%;
        }
        .centre-left{
            border-right: 0;
        }
        .pay-entrust a{
            background: #03C087 !important;
        }
        .pay-entrust a.active{
            background: #E76D42 !important;
        }
        #buy-errortips,#sell-errortips{
            padding-left: .62rem;
            font-size: 0.62rem;
            color: #f63152;
        }
        #entrustInfo{
            margin-bottom: 3.06rem;
        }
        .centre-cont{
            height: 13.5rem !important;
        }
        .unit{
            font-size: .7rem;
            color: #999;
        }
		#grade{
			float: right;
			margin-right: .62rem;
			border: 0;
			margin-top: -1rem;
			color: #999;
			-webkit-appearance: menulist-button;
			outline: none;
			width: 3rem;
			background-color: white;
			padding-left: 0;
		}
		.grade-tips{
		    float: right;
		    margin-top: -0.9rem;
		}
		
		/* 下拉样式列表 */
		.select_mars{
			width: 100%;
			height: 100%;
			height: 112%;
			position: absolute;
			left: 0;
			top: 0;
			background-color: rgba(0, 0, 0, .3);;
			display: none;
			z-index: 101;
			cursor: pointer;
		}
		.select_list{
			width: 90%;
			height: 100%;
			/* height: 94.5%; */
			height: 102%;
			position: absolute;
			left: 0;
			top: 0;
			background-color: white;
			z-index: 1000;
			padding-left: .62rem;
			box-sizing: border-box;
			-webkit-box-sizing: border-box;
			display: none;
		}
		.flexLayout1{
			display: flex;
			justify-content: space-between;
		}
		.select_listTitle{
			width: 70%;
			text-align: center;
			height: 2.5rem;
			line-height: 2.5rem;
		}
		.select_listTitle li{
			height: 100%;
		}
		.select_listTitle .navActive{
			border-bottom: 1px solid #186ed5;
			color: #186ed5;
		}
		.list_cont{
			border-top: 0 !important; 
			border-bottom: 0 !important;
		}
		.list_cont a{
			width: 100%;
			height: 100%;
			padding-right: .62rem;
			box-sizing: border-box;
			-webkit-box-sizing: border-box;
		}
		.select_details{
			border-top: 1px solid #E1E1E1 !important; 
			border-bottom: 1px solid #E1E1E1 !important;
		}
		.list_cont ul li {
			height: 2.5rem !important;
			line-height: 2.5rem !important;
		}
		.coin_choose{
			height: 2.5rem;
			line-height: 2.5rem;
			font-size: .87rem;
			text-align: center;
			color: #808080;
		}
		.tradeClick a div{
			font-size: .7rem;
		}
		.add {
			color: #E76D42;
		}
		.Minus {
			color: #03C087;
		}
		.centre-tab ul{
			width: 50%;
			padding: 0 0.6rem 0 0;
		}
		.centre_bi {
			border-bottom: 0;
		}
		.centre-tab ul li {
			border: 1px solid #E1E1E1 !important;
		}
		.flexLayout1{
			display: -webkit-box;
			display: -moz-box;
			display: -ms-flexbox;
			display: -webkit-flex;
			display: flex;
			justify-content: space-between;
		}
		.deal_entrust{
			width: 100%;
			padding: .62rem;
			border-top: .62rem solid #f5f5f5;
		}
		.deal_entrust a,.deal_entrust img{
			float: right;
		}
		.deal_entrust img{
			margin-top: 0.15rem; 
			margin-right: .4rem;
		}
		.now_entrust{
			font-size: 1rem;
			color: #666;
			font-weight: 600;
		}
		.old_entrust a{
			font-size: .87rem;
			color: #999;
		}
		/* nav{
			background-color: transparent !important;
		} */
.centre-cont ul li span{ 
    -webkit-box-flex: 1;
    -moz-box-flex: 1; 
    -webkit-flex: 1; 
    -ms-flex: 1; 
    flex: 1; 
    display: block; 
    width: 0%;
    height: 1rem;
    line-height: 1rem;
    color: white;
    text-align: center;
    margin-top: .3rem;
}
.centre-cont ul li span.sell{ 
    background-color: #fa8221  !important;;

}
.centre-cont ul li span.buy{ 
    background-color: #1db677  !important;;

}
.centre-cont ul li span:nth-child(2){ 
    -webkit-box-flex: 5; 
    -moz-box-flex: 5; 
    -webkit-flex: 5; 
    -ms-flex: 5; 
    flex: 5; 
    display: block; 
    width: 0%; 
    text-align: left;
    font-weight: bold;
    color: black;
    padding-left: .5rem;
}
.centre-cont ul li span:nth-child(3){ 
    -webkit-box-flex: 5; 
    -moz-box-flex: 5; 
    -webkit-flex: 5; 
    -ms-flex: 5; 
    flex: 5; 
    display: block; 
    width: 0%; 
    text-align: right;
    font-weight: bold;
    color: #999;
}
.percentage_unit{
    position: absolute;
    top: -2.55rem;
    left: 2.8rem;
    color: #999;
}
.greentips{
   color:#0aa623;
}
.redtips{
   color:#da2e22;
}
.imgMars{
    width: 3rem;
    height: 3rem;
    position: absolute;
    left: 0;
    top: 0;
    cursor: pointer;
}
.markerMars{
    width: 30%;
    height: 20%;
    position: fixed;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    z-index: 100;
    display: none;
    background-color: #00000075;
    border-radius: 5px;
    -webkit-border-radius: 5px;
}
.restore{
	background:url('/static/front/app/images/star_normal.png') no-repeat center;
	background-size:1.5rem auto;
}
.restore.active{
	background:url('/static/front/app/images/star_active.png') no-repeat center;
	background-size:1.5rem auto;
}
.cont-padding{
	top:3rem;
}
.zIndex{
	z-index:55;
}
.Personal-title span em{
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top:0.65rem;
            }
.Personal-title span em i:last-child{
    left:0.22rem;
}
    </style>
</head>
<body style="padding-top: 0;">
<nav class='zIndex'>
     <div class="Personal-title">
         <span>
             <a href="/m/index.html">
                 <em>
                     <i></i>
                     <i></i>
                 </em>
                 <strong><!-- 返回 --></strong>
             </a>
         </span>行情
     </div>
</nav>
<section class="cont-padding dealCenter">
    <div class="centre_bi clearfix">
        <dl>
            <dt>
                <%-- <img src="${ftrademapping.fvirtualcointypeByFvirtualcointype2.furl }"/> --%>
                <button class="imgMars"></button>
                <img src="/static/front/app/images/select-icon.png" class="select_img"/>
                <span>
                    <select id="choose" style="width: 9rem;" disabled="disabled">
                        <c:forEach var="vv" varStatus="vs" items="${fMap }">
                            <c:forEach var="v" items="${vv.value }">
                                <option ${v.fid==ftrademapping.fid?'selected':'' } value="${v.fid}">${v.fvirtualcointypeByFvirtualcointype2.fShortName }/${v.fvirtualcointypeByFvirtualcointype1.fShortName }</option>
                            </c:forEach>
                        </c:forEach>
                    </select>
                </span>
               
            </dt>
            <dd style="width: 2rem;text-align: right;cursor: pointer;" class='restore'></dd>
            <dd style="width: 2rem;text-align: right;cursor: pointer;" class='chartPage'><img src="/static/front/app/images/chart1.png"></dd>
        </dl>
    </div>
    <div class="select_mars"></div>
	<div class="select_list">
		<div class="coin_choose">币种选择</div>
		<ul class="select_listTitle flexLayout1">
			<c:forEach var="coin" items="${coins}">
                <li id="${coin.fShortName}_market" class="trade-tab ${coin.fShortName == 'BTC'?'navActive':'' }" data-key="${coin.fShortName}"  data-max="3" >${coin.fShortName}区</li>
            </c:forEach>
		</ul>
		<c:forEach var="vv" items="${fMap }" varStatus="vn">
			<div class="list_conton" style="display: ${vn.index == 0 ? 'block' : 'none'};">
				<div class="list_cont select_details">
					<ul class="list_cont" id="list_cont" style="padding-left: 0;">
						<c:forEach items="${vv.value }" var="v" varStatus="vs">
								<li class="tradeClick" data-id="${v.fid }" type="${v.fvirtualcointypeByFvirtualcointype1.fShortName }">
									<a href="${oss_url}/m/trade/coin.html?coinType=${v.fid }&tradeType=0" class="flexLayout1">
										<div class="coin_specificName fontWeight" style="font-weight: 600;">${v.fvirtualcointypeByFvirtualcointype2.fShortName }</div>		
										<div id="${v.fid }_price" class="fontWeight">0</div>	
										<div id="${v.fid }_rose" class="fid-num Minus">0</div>	
									</a>
								</li>
						</c:forEach>
					</ul>	
				</div>
			</div>
		</c:forEach>
	</div>
    <div class="forbid">
        <div class="centre-tab">
            <ul>
                <li class="active fw" style="margin-right: .8rem;">买入</li>
                <li class="fw">卖出</li>
                <!-- <li class="wodeweituo">当前委托</li>
                <li class="history"><a id="historyTrade">历史委托</a></li> -->
            </ul>
        </div>
        <div class="centre-left fr" style="margin-top: -3rem;">
            <div class="centre-title">
                <ul>
                    <li>委托</li>
                    <li>单价</li>
                    <li>委托量</li>
                </ul>
            </div>
            <div class="centre-cont" style="padding: 0 .62rem;">
                <!-- <ul class="sellbox" id="marketDepthSell">
                </ul> -->
                <span style="color:#fa8221">卖出</span>
                 <ul class="sellbox">
                </ul>
            </div>
            <!-- <div class="now_price" style="padding-top: 0;"><span id="lastprice"></span> &nbsp;&nbsp;&nbsp;&nbsp;≈<span id="marketPriceCny"></span></div> -->
            <!-- <div class="now_price"></div>   -->
            <div class="centre-cont">
               <!--  <ul class="sellbox" id="marketDepthBuy">
                </ul> -->
                <span style="color:#1db677">买入</span>
                 <ul class="buybox">
                </ul>               
            </div>
               <select id="grade" style="display: none;">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
               </select>
               <!-- <span class="grade-tips">深度：</span> -->
           <!--  <div class="centre-cont">
            </div> -->
        </div>
       	<div class="now_price" style="padding-top: 0;"><span id="lastprice"></span> <!-- &nbsp;&nbsp;&nbsp;&nbsp;≈<span id="marketPriceCny"></span> --></div>
        <div class="contre-tab-con" style="display: block;">
            <div class="centre-con">
                <div class="centre-right">
                    <div class="centre-bg">
                        <p>${coin1.fShortName }可用 <em id="toptradecnymoney">0.00</em></p>
                    </div>
                   <!--  <div class="centre">买入价格</div> -->
                    <div class="centre-input">
                        <input type="text" id="tradebuyprice" class="fw" value="0.000">
                        <em>${coin1.fShortName }</em>
                    </div>
                    <input type="hidden" id="buy-price">
                    <input type="hidden" id="sell-price">
                    <div class="centre exchange">≈<span id="buyCny" class="ml_10"></span><span class="unit">CNY</span></div>
                    <div class="centre-input">
                        <input type="text" id="tradebuyamount" class="fw" placeholder="数量">
                        <em>${coin2.fShortName }</em>
                    </div>
                    <span id="buy-errortips" class="text-color1 trade-error"></span>
                    <div class="proportion" style="position: relative;">
                        <input type="range" id="proportionBuy" value="0" />
                        <!-- <span class="percentage_unit">%</span> -->
                    </div>
                    <div class="centre clearfix" style="width: 115%;">交易额<input type="text" id="tradebuyTurnover" readonly="readonly"><span class="unit">${coin1.fShortName }</span></div>
                    <!-- <div class="centre-input">
                        <input type="text" id="tradebuyTurnover" placeholder="">
                        <em>CNY</em>
                    </div> -->
                    <div class="pay-entrust" id="buybtn">
                        <a href="javascript: void(0);" class="fw" style="font-size: 1rem;">买入${coin2.fShortName}</a>
                    </div>
                </div>
                
            </div>
        </div>
        <div class="contre-tab-con">
            <div class="centre-con">
                
                <div class="centre-right">
                <%-- <div class="now_price" style="padding-top: 0;"><span id="el">${price}</span> 
            <!-- &nbsp;&nbsp;&nbsp;&nbsp;≈<span id="marketPriceCny"></span> --></div> --%>
                    <div class="centre-bg">
                        <p>${coin2.fShortName }可用 <em id="toptrademtccoin">0.00</em></p>
                    </div>
                    <!-- <div class="centre">卖出价格</div> -->
                    <div class="centre-input">
                        <input type="text" id="tradesellprice"  class="fw" placeholder="">
                        <em>${coin1.fShortName }</em>
                    </div>
                    <div class="centre exchange">≈<span id="sellCny" class="ml_10">36.156</span><span class="unit">CNY</span></div>
                    <div class="centre-input">
                        <input type="text" id="tradesellamount" class="fw" placeholder="数量">
                        <em>${coin2.fShortName }</em>
                    </div>
                    <span id="sell-errortips" class="text-color1 trade-error"></span>
                    <div class="proportion" style="position: relative;">
                        <input type="range" id="proportionSell" value="0" />
                        <!-- <span class="percentage_unit">%</span> -->
                    </div>
                    <div class="centre clearfix" style="width:115%">交易额<input type="text" id="tradesellTurnover" readonly="readonly"><span class="unit">${coin1.fShortName }</span></div>
                    <%-- <div class="centre-input">
                        <input type="text" id="tradesellTurnover" readonly="readonly">
                        <em>${coin1.fShortName }</em>
                    </div> --%>
                    <div class="pay-entrust" id="sellbtn">
                        <a href="javascript: void(0);" class="active fw" style="font-size: 1rem;">卖出${coin2.fShortName }</a>
                    </div>
                </div>
                </div>
            </div>
            <!--  <div class="hint border-bottom">数字货币交易具有极高的风险，投资需谨慎！</div> -->
        </div>
        <div class="contre-tab-con" id="contre-tab-con" style="display: block;">
        	<div class="flexLayout1 deal_entrust">
        		<div class="now_entrust">当前委托</div>
        		<div class="old_entrust">
        			<a id="historyTrade" style="float: right;">全部</a>
        			<img alt="" src="${cdn }/static/front/app/images/all_entrust.png">
        		</div>
        	</div>
            <div class="record">
            	<div id="entrustInfo" style="width: 64%; float: left;"></div>
            </div>
        </div>
    </section>
    <article>
        <div class="popup">
            <div class="popup-cont">
                <h2>请先登录再进行此操作</h2>
                <p>
                    <a href="javascript:;">确定</a>
                </p>
            </div>
        </div>
    </article>
    <div class="markerMars"></div>
    <input id="coinshortName" type="hidden" value="${coin2.fShortName }">
    <input id="symbol" type="hidden" value="${ftrademapping.fid }">
    <input id="isopen" type="hidden" value="${needTradePasswd }">
    <input id="tradeType" type="hidden" value="0">
    <input id="userid" type="hidden" value="${userid }">

    <input id="tradePassword" type="hidden" value="${isTradePassword }">
    <input id="isTelephoneBind" type="hidden" value="${isTelephoneBind }">


	<!-- coinCount1  为委托单价小数点位数，coinCount2 为委托量小数点位数 -->
	<%-- <input id="coinCount1" type="hidden" value="${ftrademapping.fcount1 }">
	<input id="coinCount2" type="hidden" value="${ftrademapping.fcount2 }"> --%>
	<input id="coinCount1" type="hidden" value="6">
	<input id="coinCount2" type="hidden" value="3">

	<input id="minBuyCount" type="hidden"
		value="<fmt:formatNumber value="${ftrademapping.fminBuyCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="8" />">
	<input id="limitedType" type="hidden" value="0">
	<input id="fhasRealValidate" type="hidden"
		value="${fuser.fhasRealValidate }">
	<input id=fpostImgValidate type="hidden" value="${fuser.fpostImgValidate }">
	<input id="fhasImgValidate" type="hidden" value="${fuser.fhasImgValidate }">
    <input id="fhasBankValidate" type="hidden" value="${fuser.fhasBankValidate}">
	<input id="authGrade" type="hidden" value="${fuser.authGrade}"/>
	<input id="faudit" type="hidden" value="${fuser.faudit}"/>
	<input id="ffAmount" type="hidden" value="<fmt:formatNumber value="${ffAmount}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="8"/>">
	<%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
<input type="hidden" value="0" id="fcurrentCNY">
<input type="hidden" value="0" id="inputTradebuyamount">
<input type="hidden" value="0" id="inputTradesellamount">
</body>
<script type="text/javascript" src="/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="/static/front/app/js/trade.js?r=<%=new java.util.Date().getTime()%>"></script>
<script type="text/javascript" src="/static/front/app/js/alert.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<!-- <script type="text/javascript" src="/static/front/app/js/index.js"></script>-->
<script>
	/* 选择币种和法币区 */
	var userId = '${fuser.floginName}';
	if(userId != ''){
		$.post("/m/api/market/trademap/favairate1.html",{"mId":'${coinType}'},function(obj){
			if(JSON.parse(obj).data == 1){
				if(!$('.restore').hasClass('active')){
					$('.restore').toggleClass('active');
				}
			}
		});
	}
	var $div_li =$(".select_listTitle li");
	$div_li.click(function(){
		var key = $(this).attr("data-key");
		$($(".list_conton li[type="+key +"]")[0]).parent().parent().parent().show().siblings('.list_conton').hide()
		$(this).addClass("navActive").siblings().removeClass("navActive");
	});
	$("body").on("click",".select_img,.imgMars",function(){
		$(".select_mars").show().next().show();
		$("body").css("overflow","hidden");
		$(".select_list").on("click",".list_cont a",function(){
			$(".select_mars").hide().next().hide();
			$("body").css("overflow","auto");
		})
		$("body").on("click",".select_mars",function(){
			$(this).hide().next().hide();
			$("body").css("overflow","auto")
		})
		/* if(Number(value.rose)>=0){
			$("#"+key+"_rose").removeClass("add").addClass("Minus").html('+'+rose+'%');
			$("#"+key+"_price").removeClass("add").addClass("Minus").html(value.price);
		}else{
			$("#"+key+"_rose").removeClass("Minus").addClass("add").html(rose+'%');
			$("#"+key+"_price").removeClass("Minus").addClass("add").html(value.price);
		} */
	})
	$('.restore').click(function(){
		if($(this).hasClass('active')){
			//像后台通信
			$.post("/m/api/market/trademap/remvefavairate.html",{"mId":'${coinType}'},function(obj){
	    		if(JSON.parse(obj).status == 300){
	    			layer.msg("请先登录！");
	    		}else if(JSON.parse(obj).status == 200){
					$('.restore').toggleClass('active');
	    			layer.msg("取消收藏成功！");
	    		}
	    	});
		}else{
			//向后台通信;
			$.post("/m/api/market/trademap/favairate/set.html",{"mId":'${coinType}',"coin_coin":'${tradeType}',"userId":'${fuser.floginName}'},function(obj){
	    		if(obj.status == 300){
	    			layer.msg("请先登录！");
	    		}else if(obj.status == 200){
					$('.restore').toggleClass('active');
	    			layer.msg("收藏成功！");
	    		}
	    	});
		}
	})
		   
		   
	function changeC(value) {
		window.location.href = "/m/trade/coin.html?coinType=" + value
				+ "&tradeType=0";
	}
	function rangeChange(val,node){
		var fcurrentCNY = $("#fcurrentCNY").val();
        var persent = val/100;
        var totalPrice;
        if (node == "proportionBuy"){
            var toptradecnymoney = $("#toptradecnymoney").text(); 
            var tradebuyprice = $("#tradebuyprice").val();
            var tradebuyTurnover = Number(toptradecnymoney).mul(persent);
            $("#tradebuyTurnover").val(tradebuyTurnover.toFixed(6)); 
            $("#tradebuyamount").val((Number(tradebuyTurnover).div(tradebuyprice)).toFixed(6));
            var total = $("#tradebuyTurnover").val();
            var fny1 =(fcurrentCNY * total ).toFixed(2);
            $("#buyCny").text(fny1);
        }else{
            var toptrademtccoin = $("#toptrademtccoin").text();
            var tradesellprice = $("#tradesellprice").val();
            var tradesellTurnover = Number(toptrademtccoin).mul(persent);
            $("#tradesellamount").val(tradesellTurnover.toFixed(6)); 
            //$("#tradesellamount").val((Number(tradesellTurnover).div(tradesellprice)).toFixed(6));
            $("#tradesellTurnover").val((Number(tradesellTurnover).mul(tradesellprice)).toFixed(6));
            var total =$("#tradesellTurnover").val();
            var fny1 =(fcurrentCNY * total ).toFixed(2);
            $("#sellCny").text(fny1);
        }
    }
    $(function() {
        $('#proportionBuy').jRange({
            from: 0,
            to: 100,
            step: 1,
            scale: [0,25,50,75,100],
            format: '%s',
            width: "100%",
            showLabels: true,
            showScale: true,
            theme: "theme-green",
            onstatechange: function(value){
                rangeChange(value,"proportionBuy");
            }
        });
        $('#proportionSell').jRange({
            from: 0,
            to: 100,
            step: 1,
            scale: [0,25,50,75,100],
            format: '%s',
            width: "100%",
            showLabels: true,
            showScale: true,
            theme: "theme-red",
            onstatechange: function(value){
                rangeChange(value,"proportionSell");
            }
        });
        
        $(".classification-tab ul li").on('click', function() {
            $(this).addClass('active').siblings().removeClass('active');
            $(".classification").hide();
            $(".pay-centre p strong").html($(this).html());
        });
        $(".munu ul li").on('click', function() {
            $(this).addClass('active').siblings().removeClass('active');
        });
        /* $(".record").on('click',function() {
            window.location.href = "weituoguanli_xiangxi.html?type=0&symbol=1&tradeType=0";
        }); */
        $(".centre_bi dl .chartPage").on('click', function() {
            // $(".markerMars").show(); 
            layer.load(1);
            window.location.href = "/m/market/trademarket.html?symbol=${ftrademapping.fid}";
        });
        
        var timer;
        $(".centre-tab ul li:not('.wodeweituo')")
                .on(
                        'click',
                        function() {
                            $(this).addClass('active').siblings().removeClass(
                                    'active');
                            var index = $(".centre-tab ul li").index(this);
                            $(".contre-tab-con").eq(index).show().siblings(
                                    '.contre-tab-con').hide();
                            $(".centre-left").show();
                            $("#contre-tab-con").show();
                            window.clearTimeout(timer);
                        });

        $(".centre-tab ul").on(
                "click",
                ".wodeweituo",
                function() {
                	$(this).addClass('active').siblings().removeClass(
                    'active');
                    $("#contre-tab-con").show().prev().find(
                            '.contre-tab-con,.hint').hide();
                    $(".centre-left").hide();
                    timer = window.setTimeout(function() {
                        getWeiTouCon();
                    }, 3000);
                })
        /*  $(".centre-tab ul").on("click",".history",function(){
            $(".centre-left").hide();
            $(".centre-right").hide();
         }) */

        /* $(".pay-entrust").on('click',function(){
            $(".popup").show();
        }); */
        $(".popup-cont p").on('click',function(){
            $(".popup").hide();
            window.location.href="/m/user/login.html";
        });
        $(".popup").on('click',function(){
            $(this).hide();
        });
        $(".proportion_bg ul li").on('click',function(){
            $(this).addClass('active').siblings().removeClass('active');
        });
        $("#tradebuyprice,#tradesellprice").on("keyup",function(){
            var id = $(this).attr("id");
            var value = isNaN($(this).val())?0:$(this).val();
            console.log(value,'tradebuyprice');
            
            var fcurrentCNY = $("#fcurrentCNY").val();

            if(id == "tradebuyprice"){
                $("#buyCny").text((Number(value).mul(fcurrentCNY)).toFixed(6));
                var total = Number(value).mul($("#tradebuyamount").val());
                total = isNaN(total)?"0":total;
                $("#tradebuyTurnover").val(total);
            }else {
                $("#sellCny").text((Number(value).mul(fcurrentCNY)).toFixed(6));
                var total = Number(value).mul($("#tradesellamount").val());
                total = isNaN(total)?"0":total;
                $("#tradesellTurnover").val(total);
            }
            
        });
        $("#tradebuyamount,#tradesellamount").on("keyup",function(){
            var id = $(this).attr("id");
            var value = isNaN($(this).val())?0:$(this).val();
            var fcurrentCNY = $("#fcurrentCNY").val();
            if(id == "tradebuyamount"){
                var total = Number(value).mul($("#tradebuyprice").val());
                total = isNaN(total)?"0":total;
                $("#tradebuyTurnover").val(total);
                $("#inputTradebuyamount").val(value);
                var fny1 = (fcurrentCNY * total).toFixed(2);
                $("#buyCny").text(fny1);
            }else {
                var total = Number(value).mul($("#tradesellprice").val());
                total = isNaN(total)?"0":total;
                $("#tradesellTurnover").val(total);
                $("#inputTradesellamount").val(value);
                var fny1 =(fcurrentCNY * total).toFixed(2);
                $("#sellCny").text(fny1);
            }
            
        });
        $(".proportion_bg ul li").on('click', function() {
            $(this).addClass('active').siblings().removeClass('active');
        });
        
        $("#historyTrade").on("click",function(){
            var symbol = $("#choose").val();
            this.href="/m/trade/trade_entrust1.html?status=0&&symbol="+symbol;
        });
        $(".buybox").on("click","li",function(){
            var money = $(this).attr("data-money");
            var fcurrentCNY = $("#fcurrentCNY").val();
            var tradesellTurnover = $("#tradesellTurnover").val();
            $("#tradesellamount").val((Number(tradesellTurnover).div(money)).toFixed(6))
            money = Number(money).toFixed(6);
            $("#tradesellprice").val(money);
            $("#tradebuyprice").val(money);
            $("#sellCny").text((Number(money).mul(fcurrentCNY)).toFixed(6));
        });
        $(".sellbox").on("click","li",function(){
            var money = $(this).attr("data-money");
            var tradebuyTurnover =  $("#tradebuyTurnover").val();
            var fcurrentCNY = $("#fcurrentCNY").val();
            $("#tradebuyamount").val((Number(tradebuyTurnover).div(money)).toFixed(6))
            money = Number(money).toFixed(6);
            $("#tradebuyprice").val(money);
            $("#tradesellprice").val(money);
            $("#buyCny").text((Number(money).mul( fcurrentCNY)).toFixed(6));
        });
    });
    
    window.setTimeout(function() {
    	getWeiTouCon();
    }, 3000);
    
    //获取当前委托内容
    function getWeiTouCon(){
    	var symbol = document.getElementById("symbol").value;
    	var url = "/m/trade/entrustInfo.html?symbol=" + symbol + "&type=0&tradeType=0&random=" + Math.round(Math.random() * 100);
        var hidden0 = $("#fentrustsbody0").is(":hidden") ;
        var hidden1 = $("#fentrustsbody1").is(":hidden") ;
        $("#entrustInfo").load(url, null, function(data) {
            
            
            $(".fentruststitle").on("click", function() {
                trade.entrustInfoTab($(this));
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
            
            if(hidden0 == true ){
                $(".fentruststitle").eq(0).click();
            }
            if(hidden1 == true ){
                $(".fentruststitle").eq(1).click();
            }
            
        });
    }
    
    function getCny (){
       var fcurrentCNY;
       $.getJSON("/json/fcurrentCNY.html",{"symbol":"${coin1.fShortName}"},function(data){
           fcurrentCNY = data.fcurrentCNY;
           
           var tradebuyprice = $("#tradebuyprice").val();
           
           var tradesellprice = $("#tradesellprice").val();
        //    var tradebuyTurnover = $("#tradebuyTurnover").val();
        //    var tradesellTurnover = $("#tradesellTurnover").val();
           var buyCny = (Number(tradebuyprice).mul(fcurrentCNY)).toFixed(6);
        //    var buyCny =(Number(tradebuyTurnover).mul(fcurrentCNY)).toFixed(6);
           var sellCny = (Number(tradesellprice).mul(fcurrentCNY)).toFixed(6);
        //    var sellCny =(Number(tradesellTurnover).mul(fcurrentCNY)).toFixed(6);
           buyCny = isNaN(buyCny)?0:buyCny;
           sellCny = isNaN(sellCny)?0:sellCny;
           $("#fcurrentCNY").val(fcurrentCNY);
           $("#buyCny").text(buyCny);
           $("#sellCny").text(sellCny);
          $("#marketPriceCny").text(Number($("#marketPrice").val()).mul(fcurrentCNY) + "CNY");
           
       }); 
       
    }
    
    
       /* function getNewPrice(){
           var p_new = $("#el").text().length != 0?$("#el").text().substring(0,$("#el").text().length-1).trim():"${price}";
           $.getJSON("/json/fNewPrice.html",{"symbol":"${ftrademapping.fid}","p_new":p_new},function(data){
        	   if(data.num == 1){ 
                 $("#el").html(data.p_new + " ↑");
                 $("#el").css("color","#009900"); 
        	   }else{
        		   $("#el").html(data.p_new +" ↓");
                   $("#el").css("color","#da2e22"); 
        	   }
           })
    } */
   setInterval(getCny,5000);
   //setInterval(getNewPrice,5000);
    
    function maxBuy() {
        var toptradecnymoney = $("#toptradecnymoney").html();
        var tradebuyprice = $("#tradebuyprice").val();
        var maxBuy = (toptradecnymoney / tradebuyprice).toFixed(4);
        $("#maxBuy").html(maxBuy);
        layer.msg(maxBuy);
    }
    function maxSell() {
        var toptrademtccoin = $("#toptrademtccoin").html();
        var tradesellprice = $("#tradesellprice").val();
        var maxSell = (Number(toptrademtccoin).mul( tradesellprice)).toFixed(4);
        $("#maxSell").html(maxSell);
        layer.msg(maxSell);
    }
    //乘法   
    Number.prototype.mul = function (arg)   
    {   
        var m=0,s1=this.toString(),s2=arg.toString();   
        try{m+=s1.split(".")[1].length}catch(e){}   
        try{m+=s2.split(".")[1].length}catch(e){}   
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)   
    }   

    //除法   
    Number.prototype.div = function (arg){   
        var t1=0,t2=0,r1,r2;   
        try{t1=this.toString().split(".")[1].length}catch(e){}   
        try{t2=arg.toString().split(".")[1].length}catch(e){}   
        with(Math){   
            r1=Number(this.toString().replace(".",""))   
            r2=Number(arg.toString().replace(".",""))   
            return (r1/r2)*pow(10,t2-t1);   
        }   
    }
$(function(){
    var total_buy = 0,total_sell = 0;
    
    var heights = $(window).height();
    var widths = $(window).width();
    var bili = heights/widths;
    var bili1 = (heights+20)/widths;
    var u = navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);  //ios终端
    var iPhone = u.indexOf('iPhone') > -1;  //是否为iPhone或者QQHD浏览器
    var iPad = u.indexOf('iPad') > -1;
    //alert(bili1);
    if(isAndroid){
        if(bili>1.77866){
            $("html").css("font-size","15.5px !important");
            $("#tradebuyTurnover,#tradesellTurnover").css("width","55%")
            $(".select_mars").css("height","115%");
            $(".select_list").css("height","115%");
        }else if (1.77777<bili<1.77866) {
            $("html").css("font-size","14px");
            $(".select_mars").css("height","115%");
            $(".select_list").css("height","115%");
        }
    }else{
        if(bili1>1.77866){
            $("html").css("font-size","16px !important");
        }else if((bili1<1.77866)) {
            $("html").css("font-size","18px");
        }
    }
    
    $("#grade").on("change",function(){
        var page = this.value;
        var symbol = document.getElementById("symbol").value;
        var coinshortName = $("#coinshortName").val();
        var url = util.globalurl.market;
        var url = "/real/market2.html?symbol={0}&buysellcount={1}&successcount={2}";
        var buysellcount = 5;
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
                ;
                if (buyele.length == 0) {
                    $(".buybox").append('<li style="background:url(/static/front/app/images/buyBg.png) no-repeat scroll right;background-size:'+Number(value.amount)+'% 100%;" page = '+ page +' id="buy' + key + '"  data-type="1" data-money="' + util.moneyformat(Number(value.price), coinCount1) + '" data-num="' + util.moneyformat(Number(value.amount), coinCount2) + '">' + '<span class="buy">' + '买' + (key + 1) + '</span>' + '<span>' + util.moneyformat(Number(value.price), coinCount1) + '</span>' + '<span>' + util.moneyformat(Number(value.amount), coinCount2)+ '</span>' + '</li>');
                } else {
                    buyele.data().money = util.moneyformat(Number(value.price), coinCount1);
                    buyele.data().num = util.moneyformat(Number(value.amount), coinCount2);
                    buyele.children()[1].innerHTML = util.moneyformat(Number(value.price), coinCount1);
                    buyele.children()[2].innerHTML = util.moneyformat(Number(value.amount), coinCount2);
                    //buyele.children()[3].innerHTML = util.moneyformat(Number(value.amount)*Number(value.price), coinCount1);
                    
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
                if (sellele.length == 0) {
                    $(".sellbox").prepend('<li style="background:url(/static/front/app/images/sellBg.png) no-repeat scroll right;background-size:'+Number(value.amount)+'% 100%;" page = '+ page +' id="sell' + key + '" data-type="0" data-money="' + util.moneyformat(Number(value.price), coinCount1) + '" data-num="' + util.moneyformat(Number(value.amount), coinCount2) + '">' + '<span class="sell">' + '卖' + (key + 1) + '</span>' + '<span>' + util.moneyformat(Number(value.price), coinCount1) + '</span>' + '<span>' + util.moneyformat(Number(value.amount), coinCount2)+ '</span>' + '</li>');
                } else {
                    sellele.data().money = util.moneyformat(Number(value.price), coinCount1);
                    sellele.data().num = util.moneyformat(Number(value.amount), coinCount2);
                    sellele.children()[1].innerHTML = util.moneyformat(Number(value.price), coinCount1);
                    sellele.children()[2].innerHTML = util.moneyformat(Number(value.amount), coinCount2);
                    //sellele.children()[3].innerHTML = util.moneyformat(Number(value.amount)*Number(value.price), coinCount1);
                    
                    sellele.css({"background-size": (Number(value.amount)/total_sell)*100+"% 100%"});
                }
            });
            total_buy = Math.max.apply(null,arr_buy);
            total_sell = Math.max.apply(null,arr_sell);
            
            for ( var i = data.sells.length; i < buysellcount; i++) {
                $("#sell" + i).remove();
            }
        });
    });
    var url=location.href;
    var symbol;
    
   /*  if(url.substring(url.indexOf("symbol")).indexOf("&")>-1){
        symbol=url.substring(url.indexOf("symbol=")+7,url.indexOf("symbol=")+url.substring(url.indexOf("symbol")).indexOf("&"));
    }else{
        symbol=url.substring(url.indexOf("symbol=")+7);
    } */
    
    symbol = url.split("coinType=")[1].split("&")[0];
    $("[value="+symbol+"]").attr("selected",true);
     
})
function iphoneX(type){
	if(type==iphoneX){
		$(".munu").css("height","4.5rem");
		$(".select_mars").css("height","124%");
		$(".select_list").css("height","124%");
	}else{
		$(".munu").css("height","3.06rem");
	}
}
</script>
</html>
