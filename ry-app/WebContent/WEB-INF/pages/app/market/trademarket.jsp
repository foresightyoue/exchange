<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!doctype html>
<html>
<head>
<jsp:include page="../comm/header.jsp"></jsp:include>
<meta charset="utf-8" />
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
<base href="${basePath}"/>
<title>k线图</title>
<link rel="stylesheet" href="/static/front/app/css/style.css" />
<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        
<script type="text/javascript" src="/static/front/charting_library/charting_library.min.js"></script>
<script type="text/javascript" src="/static/front/datafeeds/udf/dist/polyfills.js"></script>
<script type="text/javascript" src="/static/front/datafeeds/udf/dist/bundle.js"></script>
</head>
<style>
    html,body{
        height: 100%;
        margin: 0;
        padding: 0;
        background-color: #222;
    }
    .chart-btn{
        color: #fff;
        position: absolute;
        top: 0.8rem;
        right: 0.7rem;
        z-index: 99999999;
        width: 96%;
        text-align: center;
    }
    .chart-btn img{
        width: auto;
        height: 2.2rem;
        vertical-align: middle;
        /* margin-top: -2px;
        margin-right: 20px; */
    }
    .chart-btn a{
        color: #fff;
        text-decoration: none;
    }
    .header-tool{
        width:100%;
        height: 2.4rem;
        background: #31435b;
    }
    .header-chart-panel-content {
        color: #fff;
        height:2rem;
        line-height: 2rem;
        /* background: #31435b; */
        background-color:#222;
        padding: 0 0 0 8px
    }
    .header-chart-panel-content .right {
        float: right;
    }
    .header-chart-panel-content .group {
        float: left;
    }
    .space-single {
        padding: 0 10px 0 0;
        cursor: pointer;
    }
    
    .space-single-header {
        color: skyblue;
        font-size: 17px;
        font-family: "宋体";
        margin-top: 5px;
    }
    
    .more-resolution {
        z-index:9999;
        width: 98%;
        position: fixed;
        display: none;
    }
    
    .more-indicator {
        z-index:9999;
        width: 98%;
        position: fixed;
        display: none;
    }
    
    .more-indicator-affi {
        z-index:9998;
        padding-top: 35px;
    }
    .fanhui{
        position: absolute;
        left: 0;
        top: -0.8rem;
        line-height: 2.5rem;
        display: block;
        height: 100%;
    }
    @media screen and (orientation: portrait) {
 	 /*竖屏 css*/
	.portrait {
		display:block;
	}
	.landscape {
		display:none;
	}
} 
@media screen and (orientation: landscape) {
  	/*横屏 css*/
	.portrait {
		display:none;
	}
	.landscape {
		display:block;
	}
}
    .average_data{
        /* position: absolute;
        top: 2.8rem;
        left: 0; */
        line-height: 1rem;
        padding: .62rem 0 .62rem .62rem;
        background-color: black;
        width: 100%;
        color: white;
        height: 4rem;
        box-sizing:border-box;
        -webkit-box-sizing:border-box;
    }
    .average_dataLeft{
        float: left;
        /* line-height: 2.62rem; */
        color: red;
        /* font-size: 1.2rem; */
        font-size: 1rem;
    }
    .average_dataRight{
        float: right;
        /* font-size: .87rem; */
        font-size: .77rem;
        margin-right: .62rem;
        line-height: 1.4rem;
    }
    .ml_10{
        margin-left: .62rem;
    }
    .flexLayout_data{
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        justify-content: space-between;
    }
    .average_data2{
        background-color: #222;
        padding: .62rem .62rem 0;
        height: 2.4rem;
        width: 100%;
        color: white;
        font-size: .87rem;
        box-sizing:border-box;
        -webkit-box-sizing:border-box;
        /* line-height: 1.5rem; */
        display: none;
    }
    .average_data2_left{
        float: left;
    }
    .average_data2_right{
        float: right;
        margin-right: 2rem;
        /* margin-top: .3rem; */
    }
    .average_data2 .data_hint{
        color: #6D87A8;
        margin-left: 1rem;
    }
    .average_data2 .ml_10{
        color: white;
    }
    .average_data2 img{
        width: 1rem;
        height: 1rem;
    }
    @media only screen and (min-width:500px){
        /* .average_data2{
            display: block;
        } */
        .average_data{
            display: none;
        }
        .bodyStyle{
            padding-top: 0;
        }
        .chart-btn{
            color: #fff;
		    position: fixed;
		    bottom: 0.8rem;
		    left: 0.7rem;
		    z-index: 99999999;
		    text-align: center;
	        top: auto;
            right: auto;
            width: 20px;
            height: 20px;
        }
        .fanhui{
            display: none;
        }
        .header-tool {
            width: 100%;
            height: 0rem;
            background: #31435b;
        }
        .title_content{
            display: none;
        }
        .space-single{
        	margin-top: 0;
        }
        .left-tool{
            width: 3rem;
            height: 100%;
            background: #4d4d4d;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 999;
            color: #fff;
            text-align: center;
        }
        .left-tool .left-tool-item{
            width: 100%;
            height: 3rem;
            line-height: 3rem;
        }
        .left-tool .left-tool-item.active{
            background: #111;
        }
        .left-tool-content{
            background: #111;
            height: 3rem;
            line-height: 3rem;
        }
        .left-tool-content .group{
            float: left;
        }
        .more-resolution{
            top: 0;
            left: 3rem;
            width: auto;
            padding: 0 0.7rem;
        }
        .more-indicator{
            top: 3rem;
            left: 3rem;
            width: auto;
        }
        .more-indicator .futu,.more-indicator .zhutu{
            width: 100%;
            display: block;
            height: 3rem;
            line-height: 3rem;
            background: #111;
            padding: 0 0.7rem;
        }
    }
    @media only screen and (max-width:500px){
        .average_data2{
            display: none;
        }
        .average_data,nav{
            display: block;
        }
        .bodyStyle{
            padding-top: 6.8rem;
        }
        .chart-btn{
            color: #fff;
            position: absolute;
            top:0.8rem;
            right: 0.7rem;
            z-index: 99999999;
            line-height: 1rem;
        }
        .space-single{
        	margin-top: 0px;
        }
        .ml_10{
            margin-left: .3rem;
        }
    }
    .panel-item.active,.portrait-item.active{
        color: #EB4D5C;
    }
    .line_hint{
        font-size: .7rem;
    }
    .line_style{
        height: 0.15rem;
        width: 1.5rem;
        float: right;
        margin-right: .75rem;
        margin-top: 0.3rem;
        margin-bottom: .62rem;
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
    .chart-btn .fanhui{
        height: 2.5rem;
    }

    
</style>   
<script type="text/javascript">
$(function(){
    if($(window).width() >= 500){
        $("#tv_chart_container").css({"width":$(window).width() - $(".left-tool").width(),"height":$(window).height(),"padding-left":"3rem"});
    }else{
    	$("#tv_chart_container").css({"width":"100%","height":$(window).height() - $(".header-tool").height()-$(".header-chart-panel-content").height()-$(".average_data").height()-20,"padding-left":"0rem"});
    }
});
/* $(function(){
	$("#tv_chart_container").height($(window).height() - $(".header-tool").height() -$(".header-chart-panel-content").height());
}); */
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
            "mainSeriesProperties.style": 8,//设置默认“平均k线图”
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
/* $(window).resize(function(){
	$("#tv_chart_container").height($(window).height() - $(".header-tool").height()-$(".header-chart-panel-content").height());
}); */

$(window).resize(function(){
	if($(window).width() >= 500){
		$("#tv_chart_container").css({"width":$(window).width() - $(".left-tool").width(),"height":$(window).height(),"padding-left":"3rem"});
	}else{
		$("#tv_chart_container").css({"width":"100%","height":$(window).height() - $(".header-tool").height()-$(".header-chart-panel-content").height()-$(".average_data").height()-20,"padding-left":"0rem"});
	}
});

$(window).on('orientationchange', function(evt) {
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

<body>
    <div class="header-tool"></div>
    <div class="average_data data-item">
        <div class="average_dataLeft zzprice" id="zzprice" style="line-height: 2.62rem;"></div>
           <div class="average_dataRight">
               <div>高<span class="ml_10 gao"></span></div>
               <div>低<span class="ml_10 di"></span></div>
           </div>
           <div class="average_dataRight">
               <div>幅<span class="ml_10 fudu"></span><span></span></div>
               <div>量<span class="ml_10 total"></span></div>
           </div>
    </div>
    <div class="average_data2 flexLayout_data data-item">
        <div class="average_data2_left">
            <span style="font-size: 1rem;font-weight: 600;float: left;">${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName}/${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName}</span>
            <span style="font-size: 1rem;font-weight: 600; margin-left: .62rem;" class="zzprice"></span>
            <span style="margin-left: .62rem;font-size: .87rem;" class="fudu"></span><span></span>
        </div>
        <div class="average_data2_right">
            <span class="data_hint">高<span class="ml_10 gao" style="font-size: 0.7rem;"></span></span>
            <span class="data_hint">低<span class="ml_10 di" style="font-size: 0.7rem;"></span></span>
            <span class="data_hint">量<span class="ml_10 total" style="font-size: 0.7rem;"></span></span>
            <%-- <a href="javascript:iphoneReturn();" onClick="javascript:history.go(-1);"><img alt="" src="${cdn }/static/front/app/images/close_m.png"></a> --%>
        </div>
    </div>
    
    <div class="landscape left-tool">
        <div class="button left-tool-item active" title="more" id="more-resolution-landscape" onclick="toggleMoreContent('more-resolution')"><span>时间</span></div>
        <div class="button left-tool-item" title="indicator" id="more-indicator" onclick="toggleMoreContent('more-indicator')"><span>指标</span></div>
        <div><span class="line_hint">MA 5</span><span class="line_style line_style_y"></span></div>
        <div><span class="line_hint">MA10</span><span class="line_style line_style_g"></span></div>
        <div><span class="line_hint">MA30</span><span class="line_style line_style_p"></span></div>
        <div class="left-tool-content more-resolution">
            <div class="group space-single panel-item">
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
        </div>
        
        <div class="left-tool-content more-indicator">
	        <div class="zhutu">
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
	           <div class="group space-single" style="float: right">
	              <div class="button" title="more" onclick="removeStudy(true)"><span>隐藏</span></div>
	           </div>
	           <div class="group space-single">
	              <span> </span>
	           </div>
           </div>
           <div class="futu">
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
               <div class="group space-single" style="float: right">
                  <div class="button" title="more" onclick="removeStudy()"><span>隐藏</span></div>
               </div>
           </div>
        </div>
    </div>
    
    <div class="header-chart-panel-content portrait">
		<div class="right">
		   <div class="group space-single portrait panel-item1">
		      <div class="button" title="more" id="more-resolution" onclick="toggleMoreContent('more-resolution')"><span>更多</span></div>
		   </div>
		   <div class="group space-single">
		      <div class="button" title="indicator" id="more-indicator" onclick="toggleMoreContent('more-indicator')"><span>指标</span></div>
		   </div>
		</div>
		<div class="left">
		   <div class="group space-single portrait-item">
		      <div class="button" title="1min" onclick="changeResolution('1');"><span>分时</span></div>
		   </div>
		   <div class="group space-single portrait-item">
		      <div class="button" title="1min" onclick="changeResolution('1', null, 'kline');"><span>1分钟</span></div>
		   </div>
		    <!-- <div class="landscape"> -->
		   <div class="group space-single portrait-item  active">
		      <div class="button" title="5min" onclick="changeMoreResolution('5','5分钟');"><span>5分钟</span></div>
		   </div>
			<!-- </div> -->
		   <div class="group space-single portrait-item">
		      <div class="button selected" title="15min" onclick="changeResolution('15');"><span>15分钟</span></div>
		   </div>
			<!-- <div class="landscape"> -->
		   <div class="group space-single portrait-item">
		      <div class="button selected" title="30min" onclick="changeMoreResolution('30','30分钟');"><span>30分钟</span></div>
		   </div>
			<!-- </div> -->
			<div class="landscape">
			   <div class="group space-single portrait-item">
			      <div class="button" title="1hour" onclick="changeResolution('60');"><span>1小时</span></div>
			   </div>
			   <div class="group space-single portrait-item">
			      <div class="button" title="4hour" onclick="changeResolution('240');"><span>4小时</span></div>
			   </div>
			</div>
			<div class="landscape">
			   <div class="group space-single">
			      <div class="button" title="1day" onclick="changeResolution('D');"><span>1天</span></div>
			   </div>
			   <!-- <div class="group space-single">
			      <div class="button" title="1week" onclick="changeMoreResolution('W','1周');"><span>1周</span></div>
			   </div>
			   <div class="group space-single">
			      <div class="button" title="1month" onclick="changeMoreResolution('M','1月');"><span>1月</span></div>
			   </div> -->
			   
			</div>
		</div>
	</div>
	<div class="portrait">
	    <div class="header-chart-panel-content more-resolution">
			<div class="left">
			   <!-- <div class="group space-single portrait-item">
			      <div class="button" title="5min" onclick="changeMoreResolution('5','5分钟');"><span>5分钟</span></div>
			   </div>
			   <div class="group space-single portrait-item">
			      <div class="button selected" title="30min" onclick="changeMoreResolution('30','30分钟');"><span>30分钟</span></div>
			   </div> -->
			   <div class="group space-single portrait-item">
			      <div class="button" title="1hour" onclick="changeResolution('60');"><span>1小时</span></div>
			   </div>
			   <div class="group space-single portrait-item">
			      <div class="button" title="4hour" onclick="changeResolution('240');"><span>4小时</span></div>
			   </div>
			   <div class="group space-single portrait-item">
                  <div class="button" title="1day" onclick="changeMoreResolution('D','1天');"><span>1天</span></div>
               </div>
			   <!-- <div class="group space-single portrait-item">
			      <div class="button" title="1week" onclick="changeMoreResolution('W','1周');"><span>1周</span></div>
			   </div>
			   <div class="group space-single portrait-item">
			      <div class="button" title="1month" onclick="changeMoreResolution('M','1月');"><span>1月</span></div>
			   </div> -->
			   <div class="group">
	               <span style="color: rgb(255, 218, 47);">MA5</span>
	               <span style="color: rgb(124, 255, 126);margin: 0 .3rem;">MA10</span>
	               <span style="color: rgb(217, 91, 255);">MA30</span>
	           </div>
			</div>
		</div>
	</div>
	<div class="portrait">
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
			</div>
		</div>
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
		</div>
	</div>
	</div>
    <!-- <span class="chart-btn">
    <img src="/static/front/app/images/changesScreen.png">
        <a href="javascript:iphoneReturn();" onClick="javascript:history.go(-1);">返回</a>
    </span> -->
    <div class="chart-btn">
        <a href="javascript:iphoneReturn();" onClick="javascript:history.go(-1);" class="fanhui">
            <img alt="" src="${cdn }/static/front/app/images/prev.png" style="float: left;margin-top: .1rem;">
            <span style="float: left;"><!-- 返回 --></span>
        </a>
        <span class="title_content">${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName}/${ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName}</span>
        <img src="/static/front/app/images/changesScreen.png" style="position: absolute;right: 0;">
    </div>
    
    <div id="tv_chart_container"></div>
<input type="hidden" id="coinId" value="${ftrademapping.fid}"/>
<input type="hidden" id="coinName" value="${ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName}"/>
<input id="fistiger" type="hidden" value="${fuser.fistiger}">
<input id="state" type="hidden" value="${ftrademapping.state }">
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
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
		 var flag = $("#fistiger").val();
        if(flag == "true"){
            tvWidget.chart().setChartType(9);
        }else{
            tvWidget.chart().setChartType(9)
        } 
		/* var state = $("#state").val();
		if(state == 0){
			tvWidget.chart().setChartType(9);
		}else{
			tvWidget.chart().setChartType(8);
		} */
	//	tvWidget.chart().setChartType(8)
	}
	createMAorBoll()
	
	tvWidget.chart().setResolution(resolution)
	if (!isMore) {
		$("#more-resolution>span").text("更多")
	}
}

function changeMoreResolution(resolution, text) {
	changeResolution(resolution, true)
	//$("#more-resolution>span").text(text)
	//$("#more-resolution").parent('div').addClass("active");
}

function toggleMoreContent(cls) {
	var isIndicatorContent = cls === "more-indicator"
	var moreContent = $("."+cls);
	if (!moreContent) {
		return;
	}
	
	if (isIndicatorContent) {
		$(".more-resolution").css("display","none");
		if($(".more-resolution .group").hasClass("active")){
			$("#more-resolution").parent('div').addClass("active");
		}
	} else {
		$(".more-indicator").css("display","none");
	}
	
	if (moreContent.css("display") === "none") {
		moreContent.css("display", "block") ;
	} else {
		moreContent.css("display","none");
		if($(".more-resolution .group").hasClass("active")){
            $("#more-resolution").parent('div').addClass("active");
        }
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
	$(".portrait-item").on("click",function(){
		$(".portrait-item").removeClass("active");
		$("#more-resolution").parent("div").removeClass("active");
		$(this).addClass("active");
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


  $(function(){
	 var lastprice = 0;
	 var fristprice = true;
    function getfNewPrice(){
      var symbol = "${ftrademapping.fid}";
      var marketPrice = $("#zzprice").html();
      if(marketPrice == null ||marketPrice == ""){
    	  marketPrice = 0;
      }else{
    	  marketPrice = marketPrice.substring(0,marketPrice.length -1).trim();
      } 
      $.getJSON("/json/fNewPrice1.html",{"symbol":symbol,"marketPrice":marketPrice},function(data){
            if(data.rose < 0){
            	$(".fudu").text(parseFloat(data.rose).toFixed(4)+"%");
            }else{
            	$(".fudu").text(parseFloat(data.rose).toFixed(4)+"%");
            }
            var p_new =parseFloat(data.p_new).toFixed(6);
            $(".zzprice").html(p_new);
            $(".gao").html(parseFloat(data.high).toFixed(6));
            $(".di").html(parseFloat(data.low).toFixed(6));
            $(".total").html(parseFloat(data.total).toFixed(4));
            if(p_new >= lastprice){
                $(".zzprice").text(p_new + " ↑");
                $(".zzprice").css("color","#1db677");
                $(".zzprice").parent().removeClass("greentips").addClass("redtips");
            }else if(p_new < lastprice){
            	$(".zzprice").text(p_new + " ↓");
                $(".zzprice").css("color","red");
                $(".zzprice").parent().removeClass("redtips").addClass("greentips");
            }
            lastprice = p_new;
      })
    }
  setInterval(getfNewPrice, 5000);
  })
</script>
</html>
