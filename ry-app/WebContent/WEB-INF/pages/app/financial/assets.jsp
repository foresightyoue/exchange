<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
		<meta name="format-detection" content="telephone=no">
		<meta name="format-detection" content="email=no">
		<base href="${basePath}" />
		<title>个人资产</title>
        <link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
		<link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
		<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
        <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
        <script type="text/javascript" src="/static/front/app/js/rem.js"></script>
        <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
		<style type="text/css">


			.RYH {
				
			}

			.RYB {
			}

			.coin {
				display: flex;
				flex-flow:nowrap column;
				line-height: 2.5rem;
				background-color: #ffffff;
				flex:0  0 50%;
				box-sizing:border-box;
			}

			.coin .t {
				font-size: 1rem;
				text-align: center;
				color: #e1c00b;
			}

			.v_line {
				width: 1px;
				height: 2.632rem;
				margin: auto 0;
				background-color: #CCCCCC;
			}

			.announcement {
				line-height: 1.667rem;
				background-color: #ffffff;
				padding-left: 1rem;
				font-size: 0.734rem;
				color: #333333;
				margin-top: 0.332rem;
				margin-bottom: 0.167rem;
				white-space: nowrap;
				overflow: hidden;
				padding-right: 1rem;
				display:flex;
			}

			.control {
				display: flex;
				flex-wrap: wrap;
				justify-content:space-evenly;
			}

			.control>a {
				display: flex;
			    flex-flow: nowrap column;
			    justify-content: space-around;
			    align-items: center;
			    flex: 0 0 31.5%;
			    height: 8rem;
			    text-decoration: none;
			    font-size: 1rem;
			    color: #333333;
			    margin: 0.199rem 0.15rem;
			    background-color: #ffffff;
			}

			.control>a>img {
				width: 2.343rem;
			}

			.notice_active {
				display: inline-block;
				overflow: hidden;
				font-size: 0;
				height: 1.667rem;
				vertical-align: middle;
				flex:1;
			}

			.notice_active li {
				line-height: 1.667rem;
				list-style-type: none;
				overflow: hidden;
				height: 1.66796875rem;
				text-overflow:ellipsis;
				white-space:nowrap;
			}

			.notice_active li a {
				font-size: 0.734rem;
				color: #333333;
			}
			.district{
				display:flex;
				padding:0.5rem 0;
				background:#fff;
			}
		</style>
	</head>
	<body>
		<div class="content">
			<div class="head">
				<div class="text_center">个人资产</div>
			</div>
			<div class="main">
				<div class='district'>
					<div class="RYH coin" style='border-right:1px solid #efefef;'>
						<div class='t'>收益区</div>
						<div class="t name">RYH：<font id="syryh">0.000000</font></div>
						<div class="t number">RYB： <font id="syryb">0.000000</font></div>
					</div>
					<div class="RYB coin">
						<div class='t'>交易区</div>
						<div class="t name">RYH： <font id="jyryh">0.000000</font></div>
						<div class="t number">RYB： <font id="jyryb">0.000000</font></div>
					</div>
				</div>
				
				<div class="announcement">公告：
					<div class="notice_active swiper-container">
						<ul class="swiper-wrapper">
							<c:forEach items="${requestScope.constant['news']}" var="v">
								<li class="notice_active_ch swiper-slide"><a href="${oss_url}/m${v.url }">${v.ftitle }</a></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="control">
					<a href="${oss_url}/m/financial/zichan.html">
						<div></div><img src="/static/front/app/images/menu/icon_qianbao.png" /><span>钱包</span>
					</a>
					<a href="${oss_url}/m/ryb/index.html">
						<div></div><img src="/static/front/app/images/menu/icon_ruiyinbao.png" /><span>瑞银宝</span>
					</a>
					<a href="${oss_url}/m/trade/SCwellet.html">
						<div></div><img src="/static/front/app/images/menu/icon_shuocangqiangbao.png" /><span>锁仓钱包</span>
					</a>
					
					<a onclick="alertMsg()">
							<div></div><img src="/static/front/app/images/menu/icon_ryg.png" /><span>RYG</span>
					</a>
					<a href="${oss_url}/m/trade/ToBill.html">
						<div></div><img src="/static/front/app/images/menu/icon_meirishouyi.png" /><span>账单</span>
					</a>
					<!-- <a href="${oss_url}/m/trade/ToBill.html">
						<div></div><img src="/static/front/app/images/menu/icon_zhangdan.png" /><span>账单</span>
					</a> -->
					<a href="javascript:void(0);" onclick="toCard()">
						<div></div><img src="/static/front/app/images/menu/icon_zhifu.png" /><span>支付</span>
					</a>
					<a onclick="sellAsst()">
						<div></div><img src="/static/front/app/images/menu/icon_meirishouyi.png" /><span>商城</span>
					</a>
					<a onclick="alertMsg()">
						<div></div><img src="/static/front/app/images/menu/icon_tour.png" /><span>旅游</span>
					</a>
					<a href='#' onclick="alertMsg()">
							<div></div><img src="/static/front/app/images/menu/icon_house.png" /><span>房地产</span>
						</a>
				</div>
                <div class="obligate"></div>
			</div>
			<div class="menu">
				<jsp:include page="../comm/bottom_menu.jsp"></jsp:include>
			</div>
		</div>
		<script type="text/javascript">
			function iphoneX(type){//页面已修改,该代码可能无法产生正确效果,已删除
			}
			function alertMsg() { 
				layer.msg('暂未开放,敬请期待')
			 }
			$(function() {
				var msg = '${msg}';
				if (msg != null && msg != '') {
					layer.msg(msg);
				}
				var tid = '${tid}';
                $.post("/api/trading/getjson2.html",{"tid":tid,"uri":"/api/app/getCoinTotal"},function(data){
                    $("#syryh").html(util.numFormat(parseFloat(data.data.ryhTotal),6));
                    $("#syryb").html(util.numFormat(parseFloat(data.data.rybTotal),6));
                });
                $.post("/api/trading/getjson3.html",{"uri":"/api/trading/queryBlance.html"},function(data){
                    $("#jyryb").html(util.numFormat(data.data.RYB.fTotal,6));
                    $("#jyryh").html(util.numFormat(data.data.RYH.fTotal,6));
                });
				var timer = function(opj) {
					$(opj).find('ul').animate({
						marginTop: "-0.427rem"
					}, 500, function() {
						$(this).css({
							marginTop: "0"
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

			});
			function toCard(){
				$.post("/m/trade/IsCheckIdentity.html",function(obj){
					if(obj.code == 200){
						window.location.href="/m/bank/card.html";
					}else{
						layer.msg(obj.message);
					}
				});
			}
			function sellAsst() {
				$.post("/api/trading/goToShopping.html",function(obj){
				    console.log(obj.status,"163545sadeasdas")
					if(obj.status == 200){
						window.location.href="http://m.7dingdong.com/store/integralMall?store_id=4749447&from=groupmessage";
					}else{
						alertMsg()
					}
				});
			}
		</script>
	</body>
</html>
