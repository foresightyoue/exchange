<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = "http" + "://" + request.getServerName() + path;
session.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html lang="ko" class=" js ">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="${basePath}"/>
<%@include file="comm/link.inc.jsp"%>
<title>Korbit - Bitcoin, Ethereum, Remittance</title>

<link href="${oss_url}/static/front/images/bitbs/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon">
<link href="${oss_url}/static/front/css/homePage.css" media="screen" rel="stylesheet">
<link href="${oss_url}/static/front/css/bitbs/webpack-public-style.css" media="screen" rel="stylesheet">
<link rel="stylesheet" href="/static/front/app/css/verify.css" />
<link href="${oss_url}/static/front/css/index/index.css" media="screen" rel="stylesheet">
<link rel="stylesheet" href="${oss_url}/static/front/app/css/swiper-3.4.2.min.css" />
<style type="text/css">
.form-group {
    position: relative;
}
</style>
</head>
<style>
.market {
    background-color: #f5f8fb;
}

#btn-imgcode {
    width: 55px;
    height: 25px;
    position: absolute;
    right: 10px;
    top: 5px;
}
html{
    height: 100%;
}
.transaction {
    width: 21px;
    height: 15px;
    display: inline-block;
    /* margin-right: 10px; */
    vertical-align: middle;
    font-size: 0px;
    /* margin-top: -2px; */
    float: right;
}
.hangqing{
    position: absolute;
    right: -15px;
    top: 25px;
    width: 21px;
    height: 15px;
    display: inline-block;
}
.more_service{
    position: fixed;
    top: 45%;
    right: 0;
    text-align: right;
    max-width: 200px;
    z-index: 10000;
}
.contact_service{
    display: block;
    box-sizing: border-box;
    font-size: 13px;
    color: rgb(255, 255, 255);
    cursor: pointer;
    text-decoration: none;
    width: 136px;
    height: 38px;
    line-height: 38px;
    margin-right: 10px;
    background-color: rgba(255,0,0,0.57);
    text-align: center;
    float: right;
    position: fixed;
    right: 0;
    top: 40%;
    z-index: 10000;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -ms-border-radius: 4px;
    -o-border-radius: 4px;
}
.contact_service:hover{
    color: white;
    text-decoration: none;
}
.QC_code{
    background-color: white;
    padding: 0 10px;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
}
.QC_code div{
    width: 100%;
    color: #D00;
    line-height: 20px;
    text-align: center
}
.QC_code canvas{
    margin-top: 10px;
}
#qrcodeCanvas #qrCodeIco,#qrcodeCanvasBig #qrCodeIco {
    position: absolute;
    display: block;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    width: 50px;
    height: 50px;
    background-color: #fff;
    background-repeat: no-repeat;
    background-position: center center;
    background-size: 80% auto;
    border-radius: 15px;
}
.radio {
    width: 100%;
    height: 3rem;
    line-height: 3rem;
    background-color: transparent;
    z-index: 100;
    margin: 0 auto;
}
.notice_active{
    width: 1170px;
    margin: 0 auto;
    position: relative;
    /* background: url(/static/front/images/voice.png) 0.1rem no-repeat; */
    padding-left: 3rem;
    /* background-size: 1.5rem;
    background-position: 0.6rem; */
    
}
.notice_active .more{
    position: absolute;
    top: 0;
    right: 5em;
    display: inline-block;
    width: 3em;
    z-index: 1000;
}
.notice_active .more a{
    color: #fff;
}
.notice_active ul{
    width:100%;
    padding-right: 8em;
}
.notice_active ul li{
    width: 100%;
    text-align: left;
}
.notice_active ul li a{
    color: #fff;
}
.verify-box{
    position: fixed;
    top: 50%;
    left: 50%;
    width: 410px;
    height: 250px;
    transform: translate(-50%,-50%);
    -webkit-transform: translate(-50%,-50%);
    background: #fff;
    border: 1px solid #e6e6e6;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    display: none;
    padding: 10px;
    z-index: 9999;
}
.verify-box-cover{
   position: fixed;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   background: rgba(0,0,0,0.5);
   display: none;
   z-index: 9998;
}
.btn-danger{
    background-color: #1db3b4;
    border-color: #1db3b4;
}
.btn-danger:hover{
    background-color: rgba(29, 179, 180,0.5);
    border-color: rgba(29, 179, 180,0.5);
}
.form-group>a{
    color: #1db3b4;
}
.radio{
    position: absolute;
    bottom: 0;
    /* background: rgba(255, 255, 255, 0.11); */
    background: rgba(12, 26, 52, 1);
}
.advertisement_img{
    position: absolute;
    left: 5px;
    width: 16px;
    top: 10px;
}
.swiper-container1 .swiper-slide img{
	height: 100%;
}
</style>
<body class="static_view eng">

    <%@include file="comm/headerIndex.jsp"%>

    <div id="main-content">
        <div class="landing-container">
            <div class="main-content-area no-submenus">
                <c:if test="${sessionScope.login_user==null }">
                    <div class="container login-box">
                        <div class="login">
                            <div class="login-bg"></div>
                            <div class="login-cn">
                                <div class="form-group">
                                    <h3 class="margin-top-clear login-cn-title">登录RYH</h3>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" id="indexLoginName"
                                        placeholder="输入手机" type="text">
                                </div>
                                <div class="form-group">
                                    <input class="form-control" id="indexLoginPwd"
                                        placeholder="输入密码" type="password">
                                </div>
                                <!-- <div class="form-group">
                                    <input id="index-imgCode" class="form-control" type="text"
                                        value="" placeholder="请输入验证码" /> <span><img
                                        id="btn-imgcode" class=""
                                        src="/servlet/ValidateImageServlet?r=1501754647473"></span>
                                </div> -->
                                <div class="form-group has-error">
                                    <span id="indexLoginTips"
                                        class="errortips text-danger help-block"></span>
                                </div>
                                <div class="form-group">
                                    <button id="loginbtn" class="btn btn-block btn-danger">登录
                                    </button>
                                </div>
                                <div class="form-group">
                                    <a style="float: left;" href="/validate/reset.html">忘记密码？ </a> 
                                    <a href="${oss_url}/user/register.html" class="pull-right">注册 </a>
                                    <div style="clear: both;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div>
                    <div class="landing-page">
                        <section class="landing-page">
                            <section class="landing-page-main">
                                <%-- <img class="landing-background" src="${oss_url}/static/front/images/bitbs/banana3.jpg" alt="korbit-background"> --%>
                                <div class=" swiper-container1" style="height: 100%">
                                    <ul class="swiper-wrapper">
                                        <%-- <c:forEach items="${requestScope.constant['news']}" var="v"> --%>
                                            <%-- <li class="swiper-slide"><img src="${oss_url}/static/front/images/bitbs/banana1.jpg" alt="korbit-background"></li>
                                            <li class="swiper-slide"><img src="${oss_url}/static/front/images/bitbs/banana2.jpg" alt="korbit-background"></li> --%>
                                            <li class="swiper-slide"><img src="${oss_url}/static/front/images/bitbs/banana3.jpg" alt="korbit-background"></li>
                                            <li class="swiper-slide"><img src="${oss_url}/static/front/images/bitbs/banana4.jpg" alt="korbit-background"></li>
                                        <%-- </c:forEach> --%>
                                    </ul>
						        </div>
                                <div class="landing-page-main-contents">
                                    <h1 class="text-center">
                                        <span></span>
                                    </h1>
                                </div>
	                            <div class="radio clearfix">
	                                <div class="notice_active">
								        <img alt="" src="${oss_url }/static/front/images/voice.png" class="advertisement_img">
								        <div class=" swiper-container">
		                                    <ul class="swiper-wrapper">
		                                        <c:forEach items="${requestScope.constant['news']}" var="v">
		                                            <li class="notice_active_ch swiper-slide"><a href="${oss_url}/service/article.html?id=${v.fid }">${v.ftitle }</a></li>
		                                        </c:forEach>
		                                    </ul>
								        </div>
		                                <div class="more">
		                                    <a href="${oss_url}/service/ourService.html?id=1&menuFlag=article">更多</a>
		                                </div>
	                                </div>
	                            </div>
                            </section>

                            <div class="container-full index market">
                                <div class="container">
                                    <div class="trade-navs">
                                        <div class="trade-tabs">
                                            <c:forEach var="coin" items="${coins}">
                                                <span id="${coin.fShortName}_market"
                                                    class="trade-tab ${coin.fShortName == 'BTC'?'active':'' }"
                                                    data-key="${coin.fShortName}" data-max="3">${coin.fShortName}区</span>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <div class="row market-top text-center">
                                        <span class="col-xs-2">币种</span> <span class="col-xs-2">最新成交价</span>
                                        <span class="col-xs-2">24H最高价</span> <span class="col-xs-2">24H最低价</span>
                                        <span class="col-xs-2">24H成交量</span> <span class="col-xs-2">今日涨跌</span>
                                         <span class="col-xs-2 text-center">价格趋势</span> 
                                        <span class="col-xs-2"></span>
                                    </div>
                                    <c:forEach var="vv" items="${fMap }" varStatus="vn">
                                        <c:forEach items="${vv.value }" var="v" varStatus="vs">
                                            <div
                                                class="row market-con ${v.fvirtualcointypeByFvirtualcointype1.fShortName }_market_list"
                                                data-key="${v.fvirtualcointypeByFvirtualcointype1.fShortName }"
                                                style="display: ${v.fvirtualcointypeByFvirtualcointype1.fShortName == 'BTC'?'block':'none' };">
                                                <a class="text-danger"
                                                    href="/trade/coin.html?coinType=${v.fid }&tradeType=0">
                                                    <span class="col-xs-2" style="font-size: 15px;"> <i
                                                        class="coin-logo"
                                                        style="background: url(${v.fvirtualcointypeByFvirtualcointype2.furl });background-size:100% 100%; "></i>
                                                        ${v.fvirtualcointypeByFvirtualcointype2.fShortName }
                                                </span>
                                                </a> <span class="col-xs-2" id="${v.fid }_price"> -- </span> <span
                                                    class="col-xs-2" id="${v.fid }_high"> -- </span> <span
                                                    class="col-xs-2" id="${v.fid }_low"> -- </span> <span
                                                    class="col-xs-2" id="${v.fid }_total"> -- </span> <span
                                                    class="col-xs-2" id="${v.fid }_rose">-- </span>
                                                 <span class="col-xs-2 text-center">
                                            <div id="${v.fid }_plot" style="width:100%;height:64px;display: inline-block;float: left;"></div>
                                        </span>  
                                                <span class="col-xs-2" style="position: relative;"> 
                                                    <a class="btn market-trading" href="/trade/coin.html?coinType=${v.fid }&tradeType=0">去交易</a>
                                                    <a class="hangqing" href="/trademarket.html?symbol=${v.fid}"><i id="transaction" class="transaction" style="background: url(/static/front/images/index/transaction2.png);background-size:100% 100%;"></i></a>   
                                                </span>
                                            </div>
                                        </c:forEach>
                                    </c:forEach>
                                </div>
                            </div>
                        </section>

                        <%-- <section class="korbit-introduce">
                        <table class="horizontal-headline">
                            <tbody>
                            <tr>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>
                                    <hr>
                                </td>
                                <td>
                                    <h6 class="text-center">我们的优势</h6></td>
                                <td>
                                    <hr>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="korbit-introduce-wrapper">
                            <figure class="korbit-introduce-content text-center">
                                <img alt="Diverse Assets" src="${oss_url}/static/front/images/bitbs/exchange.png" width="65" height="56">
                                <h4><span>不同的资产</span></h4>
                                <figcaption style="text-align: center;"><span>100%保证金，钱包冷热隔离，确保用户资金安全</span></figcaption>
                            </figure>
                            <figure class="korbit-introduce-content text-center">
                                <img alt="Secure Storage" src="${oss_url}/static/front/images/bitbs/safety.png" width="62" height="56">
                                <h4><span>安全存储</span></h4>
                                <figcaption style="text-align: center;"><span>专业的服务团队，在线客服及技术支持</span></figcaption>
                            </figure>
                            <figure class="korbit-introduce-content text-center"><img alt="Most Reliable" src="${oss_url}/static/front/images/bitbs/technology.png" width="56" height="56">
                                <h4><span>最可靠</span></h4>
                                <figcaption style="text-align: center;"><span>充值即时、提现迅速，每秒万单高性能交易引擎</span></figcaption>
                            </figure>
                            <figure class="korbit-introduce-content text-center"><img alt="Best Customer Service" src="${oss_url}/static/front/images/bitbs/cscenter.png" width="53" height="56">
                                <h4><span>最佳客户服务</span></h4>
                                <figcaption style="text-align: center;"><span>SSL、动态身份证等银行级安全技术，保障交易安全</span></figcaption>
                            </figure>
                        </div>
                    </section> --%>
                        <%-- <div>
                        <section class="korbit-products">
                            <table class="horizontal-headline">
                                <tbody>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>
                                        <hr>
                                    </td>
                                    <td>
                                        <h6 class="text-center">我们的产品</h6></td>
                                    <td>
                                        <hr>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <div class="slider-wrapper">
                                <div class="slick-initialized slick-slider">
                                    <div class="slick-list">
                                        <div class="slick-track" style="opacity: 1; transform: translate3d(0px, 0px, 0px); width: 3030px;">
                                            <section data-index="0" class="slick-slide slick-active" tabindex="-1" style="outline: none; width: 1010px;">
                                                <figure><img alt="exchange" src="${oss_url}/static/front/images/bitbs/prod-exchange.jpg" width="390" height="293">
                                                    <!-- react-empty: 220 -->
                                                    <figcaption>
                                                        <h4><span>值得信赖的交流</span></h4>
                                                        <p><span>世界上第一个 数字货币-USDT-各国法币 交易中心</span></p>
                                                        <c:if test="${sessionScope.login_user==null }">
                                                            <a class="btn btn-signup" href="/user/register.html"><span>开始使用</span></a>
                                                        </c:if>
                                                    </figcaption>
                                                </figure>
                                            </section>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>
                        <!-- react-empty: 228 -->
                    </div> --%>
                    </div>
                </div>

            </div>
            <!-- END div main-content-area -->
        </div>
    </div>
    <div class="container-full" style="background: url('${oss_url}/static/front/images/bitbs/lc-bg.png') no-repeat scroll center;background-size:100% 100%;">
        <div class="container text-center STARTED">
            <div class="inner">
                <dl class="dl_1">
                    <dt></dt>
                    <dd>
                        <p>创建账户</p>
                        <p>手机<!-- /邮箱注册 -->，快速创建账户</p>
                    </dd>
                </dl>
                <dl class="dl_2">
                    <dt></dt>
                    <dd>
                        <p>充值入账</p>
                        <p>充值自动到账，到账即可交易</p>
                    </dd>
                </dl>
                <dl class="dl_3">
                    <dt></dt>
                    <dd>
                        <p>开始交易</p>
                        <p>币币之间交易，简单快捷方便</p>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
    <div class="bk-anyEq on">
    <div class="container">
        <div class="row">
            <div class="col-xs-7 eqLeft">
                <a href="https://ethint.lucland.com/">
                    <img src="/static/front/images/homeapp-pic.png" class="imgpicn">
                </a>
            </div>
            <div class="col-xs-5 eqRight">

                <h3>浏览行情   酣畅交易   指尖掌握</h3>
                <div class="ecode">
                    <div class="sbtn">
                        <button type="button" class="btn btn-primary androidbtn"><img src="/static/front/images/Android.png" alt="">Android</button>
                        <button type="button" class="btn btn-primary iphonebtn"><img src="/static/front/images/iOS.png" alt="">iOS</button>
		                <h4><a href="${oss_url}/about/index.html?id=83" style="color:#FFF; text-decoration: none;">查看APP详情介绍</a></h4>
                    </div>
                    <div class="ecodeimg">
                        <div id="qrcodeCanvasBig" class="erMigs" style="display:none;position:relative;">
                              <span id="qrCodeIco">
                                   <img src="/static/front/images/ysh.png" style="padding: 5px; height: 50px; width: 50px;">
                              </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container-full" style="height: 600px;background: url('${oss_url}/static/front/images/bitbs/xs-bg.png') no-repeat scroll center;background-size:100% 100%;">
    <div class="container" style="height: 600px;position:relative;">
        <div class="row" style="width: 100%;position: absolute;top:50%;transform:translateY(-50%);-webkit-transform:translateY(-50%);-moz-transform:translateY(-50%);">
            <div class="col-xs-7 novice-left">
                <h5>HELP CENTER</h5>
                <h6>在这里我们为你准备了很多实例视频和教学</h6>
            </div>
            <div class="col-xs-5 novice-right">
                <a href="${oss_url}/about/index.html">新手帮助 &gt;</a>
            </div>
         </div>
    </div>
</div>

<div class="container-full" style="background: #f5f8fb;">
    <div class="container" style="padding-top: 100px;padding-bottom: 100px;">
        <div class="help-list">
            <div class="help-item item1">
                <img src="${oss_url}/static/front/images/help1.png">
                <p>如何充值提现</p>
	            <div class="item-bg">
	                <h1><img src="${oss_url}/static/front/images/help1.png">如何充值提现</h1>
	                <a href="${oss_url}/about/index.html?id=66" target="_blank">数字资产充值</a>
	                <a href="${oss_url}/about/index.html?id=66" target="_blank">数字资产提现</a>
	            </div>
            </div>
            
            <div class="help-item item2">
                <img src="${oss_url}/static/front/images/help2.png">
                <p>认证</p>
	            <div class="item-bg">
	                <h1><img src="${oss_url}/static/front/images/help2.png">认证</h1>
	                <a href="${oss_url}/about/index.html?id=95" target="_blank">谷歌认证</a>
	                <a href="${oss_url}/about/index.html?id=94" target="_blank">实名认证</a>
	            </div>
            </div>
            
            <div class="help-item item3">
                <img src="${oss_url}/static/front/images/help3.png">
                <p>资产交易</p>
	            <div class="item-bg">
	                <h1><img src="${oss_url}/static/front/images/help3.png">资产交易</h1>
	                <a href="${oss_url}/about/index.html?id=91" target="_blank">上币申请</a>
	            </div>
            </div>
            
            <div class="help-item item4">
                <img src="${oss_url}/static/front/images/help4.png">
                <p>新手指南</p>
	            <div class="item-bg">
	                <h1><img src="${oss_url}/static/front/images/help4.png">新手指南</h1>
	                <a href="${oss_url}/about/index.html?id=1" target="_blank">如何注册</a>
	                <a href="${oss_url}/about/index.html?id=71" target="_blank">隐私政策</a>
	            </div>
            </div>
        </div>
    
        <!-- <h3>新手帮助</h3>
        <p style="margin-bottom:60px;">为您提供基本细致的操作方法</p>
        <div class="row">
            <div class="col-xs-4">
                <div class="row list">
                    <div class="col-xs-6 ">
                        <h4>如何充值提现</h4>
                        <a href="/about/index.html?id=66" target="_blank">数字资产充值</a>
                        <a href="/about/index.html?id=66" target="_blank">数字资产提现</a>
                    </div>

                    <div class="col-xs-6">
                        <h4>认证</h4>
                        <a href="/about/index.html?id=95" target="_blank">谷歌认证</a>
                        <a href="/about/index.html?id=94" target="_blank">实名认证</a>
                    </div>

                    <div class="col-xs-6">
                        <h4>资产交易</h4>
                        <a href="/about/index.html?id=91" target="_blank">上币申请</a>
                        <a href="#" target="_blank">计划买入/卖出</a>
                        <a href="#" target="_blank">API文档</a>
                    </div>

                    <div class="col-xs-6">
                        <h4>新手指南</h4>
                        <a href="/about/index.html?id=1" target="_blank">如何注册</a>
                        <a href="/about/index.html?id=71" target="_blank">隐私政策</a>
                    </div>

                </div>
            </div>
            <div class="col-xs-4">
                <div class="help-pic">
                    <img src="/static/front/images/home-tech-mpic.png">
                </div>
            </div>
            <div class="col-xs-4">
                <div class="help-note">
                    <h5>HELP CENTER</h5>
                    <h6>在这里我们为你准备了很多实例视频和教学</h6>
                    <a class="btn" href="/about/index.html">新手帮助 &gt;</a>
                </div>
            </div> 
        </div>-->
    </div>
</div>
    <a class="contact_service" href="${oss_url}/question/question.html">
        <img alt="" src="/static/front/images/service.png">
        <span>联系客服</span>
        <c:if test="${wques == '1' }">
            <span style="position:absolute;top:2px;right:2px; inline-block;width: 10px;height: 10px;background: red;border-radius: 50%;-webkit-border-radius: 50%;"></span>
        </c:if>
    </a>
    <div class="more_service">
        <div class="QC_code">
            <div id="qrcodeCanvas" class="erMigs" style="display:none">
                  <span id="qrCodeIco">
                       <img src="/static/front/images/ysh.png" style="padding: 5px; height: 50px; width: 50px;">
                  </span>
            </div>
            <div>扫一扫下载app</div>
        </div>
    </div>
    
    <div class="verify-box">
        <div id="verify"></div>
    </div>
    <div class="verify-box-cover"></div>
    
    <%@include file="comm/footerIndex.jsp"%>
    <input type="hidden" id="alert" value="${alert }" />
    <input type="hidden" id="errormsg" value="" />
    <input type="hidden" id="area" value="" />
    <input type="hidden" id="last" value="" />
    <script type="text/javascript" src="${oss_url}/static/front/js/index/index.js"></script>
    <script src="${oss_url}/static/front/js/index/intlInput.js"></script>
    <script src="${oss_url}/static/front/js/index/jquery.sortElements.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.flot.js"></script>
    <script type="text/javascript" src="/static/front/js/verify.js"></script>
    <script type="text/javascript" src="/static/front/js/plugin/jquery.qrcode.min.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    
    <!-- 广告轮播 -->
    <script>
        var swiper1 = new Swiper('.swiper-container', {
            direction:"vertical",
            pagination: '.swiper-pagination',
            /* paginationClickable: true,
            centeredSlides: true, */
            autoplay: 3000,
            /* autoplayDisableOnInteraction: false, */
            loop: true
        });
        
        /* 图片轮播 */
        var swiper1 = new Swiper('.swiper-container1', {
        	pagination: '.swiper-pagination',
	        paginationClickable: true,
	        centeredSlides: true,
	        autoplay: 5000,
	        autoplayDisableOnInteraction: false,
			loop: true
        });
    </script>
    
</body>
<script type="text/javascript">
$(function() {
	$(".help-item").hover(function(){
		$(this).find(".item-bg").stop().show(500);
	},function(){
		$(this).find(".item-bg").stop().hide(500);
	});
	
	
    $(".verify-box-cover").on('click',function(){
        $("body").css("overflow","auto");
        $(".verify-box").hide();
        $(".verify-box-cover").hide();
        $('#verify').empty();
    });

     $("#btn-imgcode").on(
            "click",
            function() {
                this.src = "/servlet/ValidateImageServlet?r="
                        + Math.round(Math.random() * 100);
            }); 
    
/*  var url="/indexs/srarchCoins.html";
    $.post(url,{},function(data){
        $.each(data,function(key,value){
            $(".subNav").append("<div> <a href='/trade/coin.html?tradeType=0&coinType="+key+"' style='font-size: 14px;'>"+value.fShortName+"区</a></div>");
        });
    }); */
     QRcode($('#qrcodeCanvas'),136);
     QRcode($('#qrcodeCanvasBig'),200);
});

function verify(callback){
	$("body").css("overflow","hidden");
	$(".verify-box").show();
	$(".verify-box-cover").show();
	if($(".verify-img-panel").length < 1){
		$('#verify').slideVerify({
	        type : 2,       //类型
	        vOffset : 5,    //误差量，根据需求自行调整
	        vSpace : 5, //间隔
	        imgName : ['1.jpg', '2.jpg','3.jpg', '4.jpg','5.jpg', '6.jpg','7.jpg', '8.jpg','9.jpg', '10.jpg'],
	        imgSize : {
	            width: '100%',
	            height: '180px',
	        },
	        blockSize : {
	            width: '40px',
	            height: '40px',
	        },
	        barSize : {
	            width : '100%',
	            height : '40px',
	        },
	        ready : function() {
	        },
	        success : function() {
	        	$("body").css("overflow","auto");
	        	layer.msg(language["comm.tips.20"]);
	        	$(".verify-box").hide();
	            $(".verify-box-cover").hide();
	            if(callback) callback();
	            $('#verify').empty();
	        },
	        error : function() {
	        	layer.msg(language["comm.tips.21"]);
	        	$(".verify-refresh").click();
	        }
	    });
	}
	
}

$(document).ready(function(){
    $(".market-trading").mouseover(function(){
        $(this).css("background","#fdf4f4");
        $(this).css("color","#da2e22");
        /* $(this).find(".transaction").css("background","url(/static/front/images/index/transaction1.png)");
        $(this).find(".transaction").css("background-size","100% 100%"); */
    });
    
    $(".market-trading").mouseout(function(){
        $(this).css("background","#fdf4f4");
        $(this).css("color","#da2e22");
        /* $(this).find(".transaction").css("background","url(/static/front/images/index/transaction2.png)");
        $(this).find(".transaction").css("background-size","100% 100%"); */
    });
    
    // 鼠标移上移下事件：改变折线图图标的背景色
    $(".hangqing").mouseover(function(){
        $(this).find(".transaction").css("background","url(/static/front/images/index/transaction1.png)");
        $(this).find(".transaction").css("background-size","100% 100%");
    });
    $(".hangqing").mouseout(function(){
        $(this).find(".transaction").css("background","url(/static/front/images/index/transaction2.png)");
        $(this).find(".transaction").css("background-size","100% 100%");
    });
});
function QRcode(e,size){
    e.show();//$("#qrcodeCanvas canvas")
    e.find("canvas").remove();
    e.qrcode({
        width:size,//宽度
        height:size,//高度
        text:"http://" + document.domain + "/m/appversion/appversion.html?WebDownApp=WebDownApp"
   });
}
</script>
</html>
