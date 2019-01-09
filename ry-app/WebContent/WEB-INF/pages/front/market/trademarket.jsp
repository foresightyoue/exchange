<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!doctype html>
<html>
<head> 
<base href="${basePath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<base href="${basePath}"/>
<%@include file="../comm/link.inc.jsp" %>

<link rel="stylesheet" href="${oss_url}/static/front/css/market/trademarket.css" type="text/css"></link>
<link rel="stylesheet" href="${oss_url}/static/front/css/market/loader.css" type="text/css"></link>

<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        
<script type="text/javascript" src="/static/front/charting_library/charting_library.min.js"></script>
<script type="text/javascript" src="/static/front/datafeeds/udf/dist/polyfills.js"></script>
<script type="text/javascript" src="/static/front/datafeeds/udf/dist/bundle.js"></script>
<style type="text/css">
html,body{
    height: 100%;
    margin: 0;
    padding: 0;
}
.chart-btn{
    color: #fff;
    position: absolute;
    top: 0.8rem;
    right: 0.7rem;
    z-index: 99999999;
}
.chart-btn img{
    width: auto;
    height: 20px;
    vertical-align: middle;
    margin-top: -2px;
    margin-right: 20px;
}
.chart-btn a{
    color: #fff;
    text-decoration: none;
}
.header-tool{
    width:100%;
    height:2.4rem;
    background: #31435b;
}
.header-chart-panel-content {
    color: #fff;
    height:2.8rem;
    background: #222;
    padding: 0 0 0 60px;
}
.header-chart-panel-content .right {
	float: right;
}
.header-chart-panel-content .group {
	float: left;
}
.space-single {
	margin-top: 10px;
    padding: 0 10px 0 0;
	cursor: pointer;
}

.space-single-header {
    color: skyblue;
    font-size: 20px;
    margin-top: 3px;
}
.space-single-header span{
    font-size: 14px;
    font-family: "宋体";
}

.more-resolution {
	z-index:9999;
	width: 99%;
	position: fixed;
	display: none;
}

.more-indicator {
	z-index:9999;
	/* width: 99%;
	position: fixed; */
	display: none;
}

.more-indicator-affi {
	z-index:9998;
	padding-top: 35px;
}
.portrait {
	display:none;
}
.landscape {
	display:block;
}
.left{
	width: 85%;
}
.panel-item.active{
    color: #EB4D5C;
}
.line_style{
    height: 2px;
    width: 20px;
    float: left;
    margin-top: 8px;
    margin-right: 5px;
}
.line_style_y{
    background-color: rgb(255, 218, 47);
}
.line_style_g{
    background-color: rgb(124, 255, 126);
}
.line_style_p{
    background-color: rgb(217, 91, 255);
}
/* #entrutsHis{
    width: 99% !important;
} */
.market-entruts .entruts-data-data {
    height: 40px;
    border-bottom: 1px solid #2b2929;
}
/* .Personal-title span em{
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top:0.65rem;
            }
.Personal-title span em i:last-child{
    left:0.22rem;
} */
</style>
<script type="text/javascript">
$(function(){
	$("#tv_chart_container").height($(window).height()-$("#marketEntruts").height() -$(".header-chart-panel-content").height());
});
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

TradingView.onready(function()
{
    var udf_datafeed = new Datafeeds.UDFCompatibleDatafeed("/tradeView");
    var widget = window.tvWidget = new TradingView.widget({
        // debug: true, // uncomment this line to see Library errors and warnings in the console
        autosize: true,
        width : "100%",
        symbol: '${ftrademapping.fid}',//币种
        interval: '15',//周期 天
        style: '1',//1默认为蜡烛图
        theme : "Light",
        toolbar_bg: '#222222',//工具栏底色
        container_id: "tv_chart_container",//id
        //  BEWARE: no trailing slash is expected in feed URL
        datafeed: udf_datafeed,//数据
        library_path: "/static/front/charting_library/",//静态文件路径
        locale:"zh",//语言
        timezone:"Asia/Shanghai",
        //  Regression Trend-related functionality is not implemented yet, so it's hidden for a while
        drawings_access: { type: 'black', tools: [ { name: "Regression Trend" } ] },
        disabled_features: ["save_chart_properties_to_local_storage", "volume_force_overlay","use_localstorage_for_settings","header_widget","control_bar"],
        enabled_features: ["move_logo_to_main_pane", "study_templates"],
      //  preset: "mobile",//手机版
        studies: [
            "MACD@tv-basicstudies"//MACD指标
          ],
        overrides: {
            "paneProperties.background": "#222222",//K线底色
            "paneProperties.vertGridProperties.color": "#454545",
            "paneProperties.horzGridProperties.color": "#454545",	
            "symbolWatermarkProperties.transparency": 90,
            "scalesProperties.textColor" : "#AAA",
            "mainSeriesProperties.style": 0,//设置默认“平均k线图”
            "paneProperties.topMargin": 14,
            'paneProperties.legendProperties.showLegend': false,
            'paneProperties.leftAxisProperties.autoScale': false,
        },
        studies_overrides: {
            "volume.volume.color.0": "#EB4D5C",
            "volume.volume.color.1": "#53B987",
            "volume.volume ma.plottype" : "line",
            "volume.precision" : 2,
            "volume.volume.transparency": 70,
            "volume.volume ma.color": "#FF0000",
            "volume.volume ma.transparency": 30,
            "volume.volume ma.linewidth": 5,
            "volume.show ma": true,
            "bollinger bands.median.color": "#33FF88",
            "bollinger bands.upper.linewidth": 7,
            'MACD.MACD.color': '#2F95FD',
            'Stoch.%K.color': '#2F95FD',
            'Stoch.hlines background.color': '#BF43BF',
            'Relative Strength Index.plot.color': '#BF43BF',
            'Relative Strength Index.hlines background.color': '#BF43BF',
        },
        time_frames: [
            { text: "50y", resolution: "6M" },
            { text: "3y", resolution: "W" },
            { text: "8m", resolution: "D" },
            { text: "2m", resolution: "D" },
            { text: "1m", resolution: "60" },
            { text: "1w", resolution: "30" },
            { text: "7d", resolution: "30" },
            { text: "5d", resolution: "10" },
            { text: "3d", resolution: "10" },
            { text: "2d", resolution: "5" },
            { text: "1d", resolution: "5" }
        ],
        //charts_storage_url: 'http://saveload.tradingview.com',
        charts_storage_api_version: "1.1",
        client_id: 'tradingview.com',
        user_id: 'public_user',
        favorites: {
        	//intervals: ["1","5","30"] 
            //intervals: ["1D", "3D", "3W", "W", "M"],
            //chartTypes: ["Area", "Line"]
        },
        enable_publishing : false,
        allow_symbol_change : false,
        hide_top_toolbar: true
    });
    tvWidget.onChartReady(function() {
    	changeResolution('5',null,'kline')
    	createStudy('MACD')
      });
});
$(window).resize(function(){
	$("#tv_chart_container").height($(window).height() -$("#marketEntruts").height()-$(".header-chart-panel-content").height());
});

$(window).on('orientationchange', (evt) => {
  var angle = (screen && screen.orientation && screen.orientation.angle) || window.orientation || 0;
  if(angle % 180 !== 0) {
	  $(".portrait").css('display','none');
	  $(".landscape").css('display','block');
  } else {
	  $(".portrait").css('display','block');
	  $(".landscape").css('display','none');
  }
}).trigger('orientationchange');
        </script>
</head>
<body style="background: #1e1e1e;">
	<div id="loader-wrapper">
		<div class="loader-inner ball-scale-ripple-multiple">
			<div></div>
			<div></div>
			<div></div>
		</div>
		<div class="loader-section section-left"></div>
		<div class="loader-section section-right"></div>
	</div>
	<div class="container-full market-head">
		<h1 class="head-logo">
			<a class="marketplist login" href="/"></a>
		</h1>
		<div class="head-nav">
		  <div name="tradeType1" id="tradeType1" class="tradeType">
			<div>交易对</div>
			<ul class="list-unstyled">
			<c:forEach items="${ftrademappings }" var="v">
			<c:if test="${v.ftype ==1 }">
					<li class="tradeTypeItem">
						<a href="${oss_url}/trademarket.html?symbol=${v.fid }">${v.fvirtualcointypeByFvirtualcointype2.fShortName }/${v.fvirtualcointypeByFvirtualcointype1.fShortName }</a>
					</li>
			</c:if>		
			</c:forEach>	
			</ul>
		</div>
		<span class="tradeTypeStatus">
			当前：${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName }/${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName }
		</span>
		<span class="trade">
		</span>
		</div>

		<c:if test="${sessionScope.login_user == null }">
			<div class="head-login">
				<input id="login_acc" placeholder="请输入手机">
				<input id="login_pwd" placeholder="请输入密码" type="password">
				<button id="login_sub">登录</button>
		    </div>
		</c:if>
		
		<c:if test="${sessionScope.login_user != null }">
		<div class="head-login">
				<span>您好，${login_user.fnickName}</span>
				<span>|</span>
				<a id="login_logout" href="/user/logout.html" style="color:#fff;">退出登录</a>
		</div>
		</c:if>
	</div>
		<div class="container-full clearfix">
		<div id="marketLeft" class="market-left">
			<%-- <div id="marketStart" class="market-start">
				<iframe frameborder="0" border="0" width="100%" height="100%" id="klineFullScreen" src="/kline/fullstart.html?symbol=${symbol }&themename=dark"></iframe>
			</div> --%>
            <div class="header-chart-panel-content">
		<div class="right" style="float: left;">
		   <div class="group space-single portrait">
		      <div class="button" title="more" id="more-resolution" onclick="toggleMoreContent('more-resolution')"><span>更多</span></div>
		   </div>
		   <div class="group space-single">
		      <div class="button" title="indicator" id="more-indicator" onclick="toggleMoreContent('more-indicator')"><span>指标</span></div>
		   </div>
		</div>
		<div class="left">
		   <div class="group space-single">
		      <div class="button" title="1min" onclick="changeResolution('1');"><span>分时</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1min" onclick="changeResolution('1', null, 'kline');"><span>1分钟</span></div>
		   </div>
		   <div class="group space-single panel-item active">
		      <div class="button" title="5min" onclick="changeMoreResolution('5','5分钟');"><span>5分钟</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button selected" title="15min" onclick="changeResolution('15');"><span>15分钟</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button selected" title="30min" onclick="changeMoreResolution('30','30分钟');"><span>30分钟</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1hour" onclick="changeResolution('60');"><span>1小时</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="4hour" onclick="changeResolution('240');"><span>4小时</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1day" onclick="changeResolution('D');"><span>1天</span></div>
		   </div>
		   <!-- <div class="group space-single panel-item">
		      <div class="button" title="1week" onclick="changeMoreResolution('W','1周');"><span>1周</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1month" onclick="changeMoreResolution('M','1月');"><span>1月</span></div>
		   </div> -->
		   <div class="group space-single">
		      <span class="line_hint">MA5</span><span class="line_style line_style_y"></span>
		   </div>
		   <div class="group space-single">
		       <span class="line_hint">MA10</span><span class="line_style line_style_g"></span>
		   </div>
           <div class="group space-single">
               <span class="line_hint">MA30</span><span class="line_style line_style_p"></span>
           </div>
		</div>
	</div>
	<!-- <div class="portrait">
	    <div class="header-chart-panel-content more-resolution">
			<div class="left">
			   <div class="group space-single">
			      <div class="button" title="5min" onclick="changeMoreResolution('5','5分钟');"><span>5分钟</span></div>
			   </div>
			   <div class="group space-single">
			      <div class="button selected" title="30min" onclick="changeMoreResolution('30','30分钟');"><span>30分钟</span></div>
			   </div>
			   <div class="group space-single">
			      <div class="button" title="1week" onclick="changeMoreResolution('W','7D');"><span>1周</span></div>
			   </div>
			   <div class="group space-single">
			      <div class="button" title="1month" onclick="changeMoreResolution('M','1月');"><span>1月</span></div>
			   </div>
			</div>
		</div>
	</div> -->
    <div class="header-chart-panel-content more-indicator">
		<div class="right">
		   <div class="group space-single portrait">
		      <div class="button" title="more" onclick="removeStudy(true)"><span>隐藏</span></div>
		   </div>
		</div>
		<div class="left">
		   <div class="group space-single space-single-header">
		      <span>主图</span>
		   </div>
		   <div class="group space-single">
		      <span>|</span>
		   </div>
		   <div class="group space-single panel-item active">
		      <div class="button selected" title="30min" onclick="createStudy('Moving Average', true);"><span>MA</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1week" onclick="createStudy('bollinger bands', true);"><span>BOLL</span></div>
		   </div>
		   <div class="landscape">
			   <div class="group space-single">
			      <div class="button" title="more" onclick="removeStudy(true)"><span>隐藏</span></div>
			   </div>
			   <div class="group space-single">
			      <span> </span>
			   </div>
			   <div class="group space-single space-single-header">
			      <span>副图</span>
			   </div>
			   <div class="group space-single">
			      <span>|</span>
			   </div>
			   <div class="group space-single panel-item active">
			      <div class="button selected" title="MACD" onclick="createStudy('MACD');"><span>MACD</span></div>
			   </div>
			   <div class="group space-single panel-item">
			      <div class="button" title="1week" onclick="createStudy('Stochastic');"><span>KDJ</span></div>
			   </div>
			   <div class="group space-single panel-item">
			      <div class="button" title="1week" onclick="createStudy('Relative Strength Index');"><span>RSI</span></div>
			   </div>
			   <div class="group space-single panel-item">
			      <div class="button" title="1week" onclick="createStudy('Williams %R');"><span>WR</span></div>
			   </div>
			   <div class="group space-single">
			      <div class="button" title="more" onclick="removeStudy()"><span>隐藏</span></div>
			   </div>
		   </div>
		</div>
	</div>
	<div class="portrait">
	   <div class="header-chart-panel-content more-indicator more-indicator-affi">
		<div class="right">
		   <div class="group space-single">
		      <div class="button" title="more" onclick="removeStudy()"><span>隐藏</span></div>
		   </div>
		</div>
		<div class="left">
		   <div class="group space-single space-single-header">
		      <span>副图</span>
		   </div>
		   <div class="group space-single">
		      <span>|</span>
		   </div>
		   <div class="group space-single  panel-item active">
		      <div class="button selected" title="MACD" onclick="createStudy('MACD');"><span>MACD</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1week" onclick="createStudy('Stochastic');"><span>KDJ</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1week" onclick="createStudy('Relative Strength Index');"><span>RSI</span></div>
		   </div>
		   <div class="group space-single panel-item">
		      <div class="button" title="1week" onclick="createStudy('Williams %R');"><span>WR</span></div>
		   </div>
		</div>
	</div>
	</div>
            <div id="tv_chart_container"></div>
			
				<div id="marketEntruts" class="market-entruts">
					<div id="entrutsHead" class="entruts-head">
						<span class="entruts-head-nav-full" data-show="entrutsCur" data-hide="entrutsHis">当前委托</span><span class="entruts-head-nav-full" data-show="entrutsHis" data-hide="entrutsCur">历史委托</span>
					</div>
					<div class="entruts-data" id="entrutsCur">
						<div class="entruts-data-head">
							<span class="col-1">委托时间</span> 
							<span class="col-2">类型</span> 
							<span class="col-3">委托价格</span> 
							<span class="col-3">委托数</span> 
							<span class="col-3">已成交</span> 
							<span class="col-3">状态</span> 
							<span class="col-2">操作</span>
						</div>
						<div class="entruts-data-data" id="entrutsCurData"></div>
					</div>
					<div class="entruts-data" id="entrutsHis">
						<div class="entruts-data-head">
							<span class="col-1">委托时间</span> 
							<span class="col-2">类型</span> 
							<span class="col-3">委托价格</span> 
							<span class="col-3">委托数</span> 
							<span class="col-3">已成交</span> 
							<span class="col-3">状态</span> 
						</div>
						<div class="entruts-data-data" id="entrutsHisData"></div>
					</div>
				</div>
			
		</div>
		<div class="market-right">
			<div id="marketData" class="market-data">
				<div class="market-depth">
					<div class="market-depth-head">
						<span class="depth-des">&nbsp;</span>
						<span class="depth-price">价格(${fSymbol})</span>
						<span class="depth-amount">数量</span>
					</div>
					<div class="market-depth-data" id="marketDepthData">
						<div class="market-depth-price">
							<div class="market-depth-sell" id="marketDepthSell"></div>
							<div class="depth-price text-left">
								&nbsp;<span id="marketPrice" class="market-font-sell"></span>
							</div>
							<div class="depth-price right">
								&nbsp;<span class="market-font-sell" id="marketRose">0%</span>
							</div>
							<div class="market-depth-buy" id="marketDepthBuy"></div>
						</div>
					</div>

				</div>
				<div class="market-success">
					<div class="market-success-head">
						<span class="success-time">时间</span>
						<span class="success-price">价格(${fSymbol})</span>
						<span class="success-amount">成交量</span>
					</div>
					<div class="market-success-data" id="marketSuccessData"></div>
				</div>
			</div>
			
				<div id="marketTrade" class="market-trade">
					<div class="trade-table left">
						<div class="trade-tr">
							<span>可用：<span class="market-font-buy" id="totalCny">0</span>&nbsp${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName }
							</span>
						</div>
						<div class="trade-tr">
							<label for="buy-price" class="tr-tips">买入价${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName }</label>
							<input id="buy-price" />
						</div>
						<div class="trade-tr">
							<label for="buy-amount" class="tr-tips">买入量${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName }</label>
							<input id="buy-amount" />
						</div>
						<div class="trade-tr tr-boder tr-slider">
							<span id="buyBar" class="col-xs-12 buysellbar">
								<div class="buysellbar-box">
									<div id="buyslider" class="slider" data-role="slider" data-param-marker="marker" data-param-complete="complete"
										data-param-type="0" data-param-markertop="marker-top"></div>
									<div class="slider-points">
										<div class="proportioncircle proportion0" data-points="0">0%</div>
										<div class="proportioncircle proportion1" data-points="20">20%</div>
										<div class="proportioncircle proportion2" data-points="40">40%</div>
										<div class="proportioncircle proportion3" data-points="60">60%</div>
										<div class="proportioncircle proportion4" data-points="80">80%</div>
										<div class="proportioncircle proportion5" data-points="100">100%</div>
									</div>
								</div>
							</span>
						</div>
						<div class="trade-tr tr-boder">
							<span class="tr-tips">交易额：</span><span class="tr-right"><span class="market-font-buy" id="buy-limit">0.0000</span>&nbsp;${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName }</span>
						</div>
						<div class="trade-tr tr-btn">
							<button id="buy_sub" class="buy">买入</button>
						</div>
					</div>
					<div class="trade-table right">
						<div class="trade-tr">
							<span>可用：<span class="market-font-sell" id="totalCoin">0</span>&nbsp;${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName }
							</span>
						</div>
						<div class="trade-tr">
							<label for="sell-price" class="tr-tips">卖出价${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName }</label>
							<input id="sell-price" />
						</div>
						<div class="trade-tr">
							<label for="sell-amount" class="tr-tips">卖出量${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName }</label>
							<input id="sell-amount" />
						</div>
						<div class="trade-tr tr-boder tr-slider">
							<span id="sellBar" class="col-xs-12 buysellbar">
								<div class="buysellbar-box">
									<div id="sellslider" class="slider" data-role="slider" data-param-marker="marker sell-marker"
										data-param-complete="complete" data-param-type="1" data-param-markertop="marker-top"></div>
									<div class="slider-points">
										<div class="proportioncircle proportion0" data-points="0">0%</div>
										<div class="proportioncircle proportion1" data-points="20">20%</div>
										<div class="proportioncircle proportion2" data-points="40">40%</div>
										<div class="proportioncircle proportion3" data-points="60">60%</div>
										<div class="proportioncircle proportion4" data-points="80">80%</div>
										<div class="proportioncircle proportion5" data-points="100">100%</div>
									</div>
								</div>
							</span>
						</div>
						<div class="trade-tr tr-boder">
							<span class="tr-tips">交易额：</span><span class="tr-right"><span class="market-font-sell" id="sell-limit">0.0000</span>&nbsp;${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName }</span>
						</div>
						<div class="trade-tr tr-btn">
							<button id="sell_sub" class="sell">卖出</button>
						</div>
					</div>
				</div>
			
		</div>
	</div>
	
	
			<div class="modal modal-custom fade" id="tradepass" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-mark"></div>
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<span class="modal-title" id="exampleModalLabel">交易密码</span>
					</div>
					<div class="modal-body form-horizontal">
						<div class="form-group">
							<div class="col-xs-3 control-label">
								<span>交易密码：</span>
							</div>
							<div class="col-xs-6 padding-clear">
								<input type="password" class="form-control" id="tradePwd" placeholder="交易密码">
							</div>
						</div>
						<div class="form-group">
							<div class="col-xs-6 padding-clear col-xs-offset-3">
								<span id="errortips" class="error-msg text-danger"></span>
							</div>
						</div>
						<div class="form-group margin-bottom-clear">
							<div class="col-xs-6 padding-clear col-xs-offset-3">
								<button id="modalbtn" type="button" class="btn btn-danger btn-block">确定</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	
	<input type="hidden" id="sellShortName" value="${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName }">
	<input type="hidden" id="coinshortName" value="${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName }">
	<input id="userid" type="hidden" value="${userid }">	
	<input type="hidden" id="cnyDigit" value="${ftrademapping.fcount1 }">
	<input type="hidden" id="coinDigit" value="${ftrademapping.fcount2 }">
	<input type="hidden" id="symbol" value="${ftrademapping.fid }">
	<input type="hidden" value="${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName }" id="coinshortName">
	<input id="minBuyCount" type="hidden" value="<ex:DoubleCut value="${ftrademapping.fminBuyCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/>">
    <input id="limitedType" type="hidden" value="0">
    <input id="lastprice" type="hidden" value="0">
	<input id="isopen" type="hidden" value="${needTradePasswd }">
	<input id="tradeType" type="hidden" value="${tradeType }">
	<input id="login" type="hidden" value="${login }">
	<input id="tradePassword" type="hidden" value="${tradePassword }">
	<input id="isTelephoneBind" type="hidden" value="${isTelephoneBind }">
	<input id="fhasRealValidate" type="hidden" value="${fuser.fhasRealValidate }">
	<input id="fistiger" type="hidden" value="${fuser.fistiger}">
	<input id="state" type="hidden" value="${ftrademapping.state }">


<script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.jslider.js"></script>

<script type="text/javascript" src="${oss_url}/static/front/js/market/trademarket.js?t=<%=new java.util.Date().getTime() %>"></script>
<script type="text/javascript">
 /* $(function(){
	alert($("#state").val())
});  */

//主图指标id
var mainIndicator = {
		ma: [],
		boll: null
}
// 副图指标id
var affiIndicatorId = null

function createMA() {
	removeStudy(true)
	// 折线图
	if (tvWidget.chart().chartType() === 2) {
		mainIndicator.ma.push(tvWidget.chart().createStudy("Moving Average", true, false, [60], null, {'plot.color': '#FFDA2F'}))
	} else {
		mainIndicator.ma.push(tvWidget.chart().createStudy("Moving Average", true, false, [5], null, {'plot.color': '#FFDA2F'}))
		mainIndicator.ma.push(tvWidget.chart().createStudy("Moving Average", true, false, [10], null, {'plot.color': '#7CFF7E'}))
		mainIndicator.ma.push(tvWidget.chart().createStudy("Moving Average", true, false, [30], null, {'plot.color': '#D95BFF'}))
	}
}

function createBoll() {
	removeStudy(true)
	// 折线图
	if (tvWidget.chart().chartType() === 2) {
		mainIndicator.boll = tvWidget.chart().createStudy('bollinger bands', true, false, null, null, { 
		    'plots background.visible': false,
		    'upper.visible': false,
		    'lower.visible': false,
			'median.color': '#FFDA2F',
		});
	} else {
		mainIndicator.boll = tvWidget.chart().createStudy('bollinger bands', true, false, null, null, { 
			'plots background.visible': false,
			'median.color': '#FFDA2F',
		    'upper.color': '#7CFF7E',
		    'lower.color': '#D95BFF',
		});
	}
}

function createMAorBoll() {
	if (mainIndicator.boll !== null) {
		createBoll();
	} else {
		createMA();
	}
}

function createStudy(name, isMain) {
	if (isMain) {
		if (name === "Moving Average") {
			createMA()
		} else {
			createBoll()
		}
	} else {
		removeStudy(isMain)
		affiIndicatorId = tvWidget.chart().createStudy(name, false);
	}
}

function removeStudy(isMain) {
	if (isMain) {
		mainIndicator.ma.forEach(function(o) {
			tvWidget.chart().removeEntity(o)
		});
		mainIndicator.ma = [];
		if(mainIndicator.boll !== null) {
			tvWidget.chart().removeEntity(mainIndicator.boll);
			mainIndicator.boll = null;
		}
	} else {
		if (affiIndicatorId) {
			tvWidget.chart().removeEntity(affiIndicatorId)
			affiIndicatorId = null;
		}
	}
}

function changeResolution(resolution, isMore, min1Type) {
	if (!tvWidget) {
		return;
	}
	if (resolution === "1" && !min1Type) {
		// 折线图
		tvWidget.chart().setChartType(2)
	} else {
		// k线图ß 
		 var flag = $("#fistiger").val()
		if(flag=="true"){
			tvWidget.chart().setChartType(9);
		}else{
			tvWidget.chart().setChartType(9);
		} 
		/* var state = $("#state").val();
		if(state == 0){
			tvWidget.chart().setChartType(9);
		}else{
			tvWidget.chart().setChartType(8);
		} */
	}
	createMAorBoll()
	tvWidget.chart().setResolution(resolution)
	if (!isMore) {
		$("#more-resolution>span").text("更多")
	}
}

function changeMoreResolution(resolution, text) {
	changeResolution(resolution, true)
	$("#more-resolution>span").text(text)
}

function toggleMoreContent(cls) {
	var isIndicatorContent = cls === "more-indicator"
	var moreContent = $("."+cls);
	if (!moreContent) {
		return;
	}
	
	if (isIndicatorContent) {
		$(".more-resolution").css("display","none");
	} else {
		$(".more-indicator").css("display","none");
	}
	
	if (moreContent.css("display") === "none") {
		moreContent.css("display", "block") ;
	} else {
		moreContent.css("display","none");
	}
}

function changesScreen(isScreen){
	var client = new ClientProxy();
    client.req = {
        'CutScreen' : isScreen,
    };
    if (!client.send("CutScreen")) {
        console.log(client.getMessage());
    }
}

function iphoneReturn(){
    var ua = navigator.userAgent.toLowerCase(); 
    if (/iphone|ipad|ipod/.test(ua)) {//iphone
        var client = new ClientProxy();
        client.req = {
            'iphoneReturn' : "true",
        };
        if (!client.send("iphoneReturn")) {
            console.log(client.getMessage());
        }     
    } else if (/android/.test(ua)) {//android
        changesScreen(false);
    }
}

function ReturnBtnClick(){
	window.location.href="javascript:history.back(-1)";
	changesScreen(false);
}

$(function(){
	$(".panel-item").on("click",function(){
        $(this).addClass("active").siblings().removeClass("active");
    });
	
	$(".chart-btn img").click(function(){
		if($(this).hasClass("clicked")){
			$(this).removeClass("clicked");
			changesScreen(false);
		}else{
			$(this).addClass("clicked");
			changesScreen(true);
		}
	});
})
    
 /* $(function(){
	function getNewPrice(){
		var p_new = $("#marketPrice").text().length != 0 ?$("#marketPrice").text().substring(0,$("#marketPrice").text().length -1).trim():"${p_new}";
		$.getJSON("/json/PCfNewPrice.html",{"symbol":"${ftrademapping.fid }","p_new":p_new},function(data){
			if(data.num  == 1){
			 $("#marketPrice").html(data.p_new+" ↑");
			 $("#marketPrice").css("color","#009900");
			}else{
			 $("#marketPrice").html(data.p_new + " ↓");
	         $("#marketPrice").css("color","#da2e22");
			}
		})
	}
	setInterval(getNewPrice,5000);
   }) */
	
</script>
</body>
</html>