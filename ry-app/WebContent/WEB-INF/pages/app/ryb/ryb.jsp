<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
<base href="${basePath}" />
<title>瑞银宝</title>
<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
<link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/rem.js"></script>
<script type="text/javascript" src="/static/front/app/js/vue.js"></script>
<style>

.content>.head>.right {
	font-size: 0.800rem;
}

.right_arrow {
	border-top: 0.156rem #e34a06 solid;
	border-right: 0.156rem #e34a06 solid;
	display: block;
	margin-top: 0.156rem;
	margin-left: -0.209rem;
	width: 0.585rem;
	height: 0.585rem;
	transform: rotate(45deg);
	-ms-transform: rotate(45deg);
	-moz-transform: rotate(45deg);
	-webkit-transform: rotate(45deg);
	-o-transform: rotate(45deg);
}

.content>.main>.announcement {
	line-height: 1.667rem;
	background-color: #ffffff;
	padding-left: 1rem;
	font-size: 0.734rem;
	color: #333333;
	margin-top: 0.332rem;
	margin-bottom: 0.367rem;
	white-space: nowrap;
	overflow: hidden;
	padding-right: 1rem;
}

.content>.main>.announcement>.notice_active {
	display: inline-block;
	overflow: hidden;
	font-size: 0;
	height: 1.667rem;
	vertical-align: middle;
	width:85%;
	overflow:hidden;
}

.content>.main>.announcement>.notice_active li {
	line-height: 1.667rem;
	list-style-type: none;
	overflow: hidden;
	height: 1.667rem;
    text-overflow:ellipsis;
    white-space:nowrap;
}

.content>.main>.announcement>.notice_active li a {
	font-size: 0.734rem;
	color: #333333;
}

.content>.main>.area {
	display: flex;
	background-color: #FFFFFF;
	align-items:center;
}

.content>.main>.area>.text {
	writing-mode: vertical-lr;
	writing-mode: tb-lr;
	text-align: center;
	line-height: 3.964rem;
	font-size: 1rem;
	color: #333333;
}

.content>.main>.area>.line {
	width: 1px;
	height: 8.667rem;
	background-color: #999999;
	margin: auto 0;
}

.content>.main>.area>.data {
	padding: 1.167rem 1.964rem 1.433rem 1.332rem;
	flex-grow: 1;
}

.content>.main>.area>.data>.ry {
	display: flex;
	font-size: 0.800rem;
	color: #eba51d;
	padding-left:2%;
	align-items:center;
    position:relative;
}
.content>.main>.area>.data>.ry>.rtArrowBox{
    position:absolute;
    display:flex;
    right:3%;
}
.content>.main>.area>.data>.ry>.text {
	margin-right: 0.734rem;
	font-size:1rem;
}

.content>.main>.area>.data>.ry>.num {
	font-size: 0.933rem;
	margin-right:32%;
}

.content>.main>.area>.data>.space {
	height: 0.632rem;
}

.content>.main>.area>.data>.info {
	display: flex;
	font-size: 0.800rem;
	color: #333333;
	align-items:Center;
}
.content>.main>.area>.data>.info>.container {
	border: solid 0.035rem #efefef;
	display: flex;
	justify-content: space-around;
	flex: 80%;
	height: 4.2rem;
    align-items: center;
    border-radius:0.3rem;
}
.content>.main>.area>.data>.info>.container>div{
	display:flex;
	flex-flow:wrap column;
	justify-content:space-evenly;
	align-items:center;
	height:100%;
}
.content>.main>.area>.data>.info>.space {
	flex-grow: 1;
}
.content>.main>.area>.data>.info>.space:nth-child(5) {
	flex-grow: 2;
}
.content>.main>.line {
	line-height: 3.333rem;
    font-size: 1rem;
    color: #333333;
    text-align: center;
    background-color: #FFFFFF;
    margin-top: 6px;
    display: block;
}

.content>.main>.block {
	border-radius: 0.3rem;;
	margin: 0.332rem auto 0.6rem;;
	font-size: 0.800rem;
	color: #333333;
	width:94%;
}

.content>.main>.block:first-child {
	margin-top: 0.667rem;
}

.content>.main>.block>.row {
	height: 3.433rem;
	display: flex;
	flex-direction: column;
	padding-left: 0.2734rem;
	position: relative;
}

.content>.main>.block>.row.important {
	background-color: #492c22;
	color: #ffffff;
	border-radius: 0 0 0.265rem 0.265rem;
}

.content>.main>.block>.row>.title {
	font-size: 1rem;
}

.content>.main>.block>.row>.title.important {
	color: #e34a06;
}

.content>.main>.block>.row>.space {
	flex-grow: 1;
}

.content>.main>.block>.row>.important_text {
	color: #eba51d;
}

.content>.main>.block>.row.button:after{
	content: "详情";
	background-color: #EBA51D;
	padding:0.265rem 0.601rem 0.234rem 0.632rem;
	right: 1rem;
	top: 50%;
	transform:translate(0,-50%);
	position: absolute;
	border-radius: 0.332rem;
	text-align: right;
	color: #FEFEFE;
	display: block;
}
</style>
</head>
<body>
	<div id="data" class="content">
		<div class="head">
			<a href="/m/personal/assets.html?menuFlag=assets" class="left"> 
				<i class="left_arrow"></i>
			</a>
			<a href="javaScript:layer.msg('Please add links.')" class="right">账单详情</a>
			<div class="text_center">瑞银宝</div>
		</div>
		<div class="main">
			<div class="announcement">
				公告：
				<div class="notice_active swiper-container">
					<ul class="swiper-wrapper">
						<c:forEach items="${requestScope.constant['news']}" var="v">
							<li class="notice_active_ch swiper-slide"><a href="${oss_url}/m${v.url }">${v.ftitle }</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="area">
				<div class="data">
					<div class="ry">
						<div style="width: 4.5rem;" class="text">收益区</div>
						<div class="text">RYH</div>
						<div id="ryhTotal" class="num">0.000000</div>
                        <div v-on:click="tozc()"  class='rtArrowBox'>
                            <div  class="right_arrow"></div>
                            <div class="right_arrow"></div>
                        </div>
						
					</div>
					<div class="space"></div>
					<div class="info">
						<div class="container">
							<div>锁仓<span id="ryhLock">0.000000</span></div>
							<div>自由<span id="ryhFlow">0.000000</span></div>
							<div>未领取<span id="ryhUnget">0.000000</span></div>
						</div>
					</div>
					<div class="space"></div>
					<div class="ry">
						<div style="width: 4.5rem;" class="text">收益区</div>
						<div class="text">RYB</div>
						<div id="rybTotal" class="num">0.000000</div>
                        <div v-on:click="tosc()" class='rtArrowBox'>
                            <div  class="right_arrow"></div>
                            <div class="right_arrow"></div>
                        </div>
					</div>
					<div class="space"></div>
					<div class="info">
						<div class="container">
							<div>锁仓<span id="rybLock">0.000000</span></div>
							<div>自由<span id="rybFlow">0.000000</span></div>
							<div>受限<span id="rybUnget">0.000000</span></div>
						</div>
					</div>
				</div>
			</div>
			<a class="line" href="/m/ryb/exchange.html">CNY兑换RYB</a>
			<!-- <a class="line" href="/m/ryb/exchange_1.html">RYB兑换RYH</a> -->
			<a class="line" href="/m/ryb/proceedsToTrade.html">收益区转交易区</a>
			<a class="line" href="/m/ryb/transactionToEarnings.html">交易区转收益区</a>
			<a class="line" href="/m/ryb/transferAccounts.html">转账</a>
			<!-- <%--<a class="line" href="/m/ryb/transferSell.html">商城转账</a>--%> -->
			<a class="line" href="javascript:void(0);" onclick="toappversion()">商城转账</a>
			<!-- <div class="block">
				<div class="row">
					<div class="space"></div>
					<div class="title">精品推荐</div>
					<div class="space"></div>
				</div>
				<a class="row important" href="/m/ryb/staticRelease.html"><div class="space"></div>
					<div>静态释放100天</div>
					<div class="space"></div>
					<div class="important_text">预计汇报280%</div>
					<div class="space"></div></a>
			</div> -->
			<!-- <div class="block">
                <template v-for="item in items">
                    <div class="row" style='background:#fff4e0;'>
                        <div class="space"></div>
                        <div class="title">{{ item.title }}</div>
                    	<div class="space"></div>
                    </div>
                    <a v-if="item.isLink" :href="item.link" class="row important button" style='margin-bottom:0.6rem;'>
                        <div class="space"></div>
                        <template v-for="t in item.text">
                            <div>{{t}}</div>
                            <div class="space"></div>
                        </template>
                    </a>
                    <div v-else class="row important button">
                        <div class="space"></div>
                        <template v-for="t in item.text">
                        	<div>{{t}}</div>
                        	<div class="space"></div>
                        </template>
                    </div>
                </template>
			</div> -->
			<div class="obligate"></div>
		</div>
		<div class="menu"></div>
	</div>
	<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
	<script type="text/javascript">
		$(function() {
            var lock_depot_data=new Vue({
                el: '#data',
                data: {
                    items: [],
                    suo : [],
                    zcqb : '',
                    scqb : ''
                },
                methods: {
                	tozc : function () {
                		window.location.href ='${oss_url}/m/trade/SCwellet.html';
                	},
                	tosc : function () {
                		window.location.href ='${oss_url}/m/ryb/RYBWellet.html';
                	}
                }
            });
			var msg = '${msg}';
			if (msg != null && msg != '') {
				layer.msg(msg);
			}
			var tid = '${tid}';
            $.post("/api/trading/getjson2.html",{"tid":tid,"uri":"/api/app/getCoinTotal"},function(data){
            	if(data.data){
                $("#ryhLock").html(util.numFormat(parseFloat(data.data.ryhLock),6));
                $("#ryhFlow").html(util.numFormat(parseFloat(data.data.ryhFlow),6));
                $("#ryhUnget").html(util.numFormat(parseFloat(data.data.ryhFree),6));
                $("#ryhTotal").html(util.numFormat(parseFloat(data.data.ryhTotal),6));
                $("#rybLock").html(util.numFormat(parseFloat(data.data.rybLock),6));
                $("#rybFlow").html(util.numFormat(parseFloat(data.data.rybFlow),6));
                $("#rybUnget").html(util.numFormat(parseFloat(data.data.rybFree),6));
                $("#rybTotal").html(util.numFormat(parseFloat(data.data.rybTotal),6));
            	}
            });
            $.post("/api/trading/getjson2.html",{"uri":"/api/app/userLockProject"},function(data){
                lock_depot_data.suo=data.data;
                for(var i = 0;i<data.data.length;i++){
                	if(data.data[i].projectName.indexOf('RYH') != -1){
                		lock_depot_data.zcqb = data.data[i].walletId;
                		break;
                	}
                };
                for(var i = 0;i<data.data.length;i++){
                	if(data.data[i].projectName.indexOf('RYB') != -1){
                		lock_depot_data.scqb = data.data[i].walletId;
                		break;
                	}
                };
              });
            /* $.post("/api/trading/getjson2.html",{"uri":"/api/app/getLockProduct"},function(data){
                for (var i = 0; i < data.data.length; i++) {
                    var title="最新"+(i+1)+" ("+data.data[i].currencyType+")";
                    lock_depot_data.items.push({
                        title:title,
                        isLink:true,
                        link:"/m/ryb/details.html",
                        text:[data.data[i].projectName]
                    });
                }
            }); */
			var timer = function(opj) {
				$(opj).find('ul').animate({
					marginTop : "-6.511rem"
				}, 500, function() {
					$(this).css({
						marginTop : "0"
					}).find("li:first").appendTo(this);
				})
			};
			var num = $('.notice_active').find('li').length;
			if (num > 1) {
				var time = setInterval(function() {
					timer(".notice_active")
				}, 3500);
				$('.gg_more a').mousemove(function() {
					clearInterval(time);
				}).mouseout(function() {
					time = setInterval(function() {
						timer(".notice_active")
					}, 3500);
				});
			}
			
			var mySwiper = new Swiper('.swiper-container', {
				autoplay: 1,
				speed:8000,
				loop:true,
				freeMode:true,
				autoplayDisableOnInteraction :false,
				freeModeSticky:true,
				spaceBetween:30
			})
		})
		function iphoneX(type) {//页面已修改,该代码可能无法产生正确效果,已删除
			/* if(type==iphoneX){
			    $(".munu").css("height","17.578rem");
			    $(".radio").css("bottom","17.578rem");
			}else{
			    $(".munu").css("height","11.953rem");
			    $(".radio").css("bottom","11.953rem");
			} */
		}

        function toappversion(){
            $.post("/m/trade/transferMallCheck.html",function(obj){
            	if(obj.code == 200){
            window.location.href="/m/ryb/transferSell.html";
            	}else{
            		layer.msg(obj.message);
            	}
            });
        }
	</script>
</body>
</html>